// Generates src/lib/commands.ts (the Keybindings page, the home page's keys, the palette demo) and
// src/lib/version.ts from a Mirada checkout: every constant of the Command enum (its label, its
// one-line description and its chord) and the heading Command.group() puts it under. The chords are
// the first key each command has in Mirada's own keymap, keymaps/cua.json, and on a Mac in
// cua.mac.json; a ref from before the keymaps (0.3.0 and older) has them in the enum instead, with
// the Mac chords Command.chord(boolean) swaps in, and is read that way.
//
//   java scripts/GenCommands.java [/path/to/Mirada] [--ref=v0.3.0]
//
// The checkout is the argument, else $MIRADA_REPO, else ../Mirada. Run it from the site's root.
// The files are read at a git ref, not from the working tree, so the site describes a release
// whatever branch the checkout is on: the newest vX.Y.Z tag by default, or --ref=<tag|branch|HEAD>.
// A chord expression it does not understand stops it rather than being guessed.
// A JDK 25 compact source file (JEP 512): no build step, nothing outside java.base.

static final Path OUT = Path.of("src/lib");

static final String COMMAND = "src/main/java/com/mirada/ui/Command.java";
static final String SETTINGS = "src/main/java/com/mirada/settings/Settings.java";
static final String KEYMAPS = "src/main/resources/com/mirada/keymaps/";
static final String MESSAGES = "src/main/resources/com/mirada/i18n/messages.properties";

/** One command: its label, what the palette says it does, and its chord off macOS and on it. */
record Cmd(String id, String title, String description, List<String> pc, List<String> mac) {}

/** A chord expression evaluated for both platforms: Mirada's SHORTCUT is Ctrl off macOS, Cmd on it. */
record Pair(List<String> pc, List<String> mac) {
    static final Pair NONE = new Pair(null, null);
}

void main(String[] args) throws IOException, InterruptedException {
    String repoArg = null;
    String ref = null;
    for (String a : args) {
        if (a.startsWith("--ref=")) ref = a.substring("--ref=".length());
        else repoArg = a;
    }
    Path repo = Path.of(repoArg != null
                    ? repoArg
                    : Objects.requireNonNullElse(System.getenv("MIRADA_REPO"), "../Mirada"))
            .toAbsolutePath()
            .normalize();
    if (!Files.exists(repo.resolve(".git"))) {
        System.err.println("No Mirada checkout at " + repo + " (pass its path, or set MIRADA_REPO)");
        System.exit(1);
    }
    if (ref == null) ref = newestTag(repo);

    String source = stripComments(show(repo, ref, COMMAND));
    int open = source.indexOf("enum Command {");
    if (open < 0) throw new IllegalStateException("enum Command not found in " + COMMAND + " at " + ref);

    // The constants run from the enum's brace to the first top-level semicolon.
    List<String> constants = constants(source, open + "enum Command {".length());
    // From 0.4.0 on, a constant is (id, keywords): its label, its description and its group's heading
    // are in the English catalog, as command.<id>, command.<id>.desc and group.<key>.
    boolean catalog = exists(repo, ref, MESSAGES);
    Properties messages = new Properties();
    if (catalog) messages.load(new StringReader(show(repo, ref, MESSAGES)));
    Map<String, String> groupOf = groupOf(method(source, "String group()"), messages);
    // From the keymaps on, a constant is (id, label, description, keywords) and its keys are the keymap's.
    boolean keymaps = exists(repo, ref, KEYMAPS + "cua.json");
    Map<String, String> pcKeys = keymaps ? firstKeys(show(repo, ref, KEYMAPS + "cua.json")) : Map.of();
    Map<String, String> macKeys = keymaps ? firstKeys(show(repo, ref, KEYMAPS + "cua.mac.json")) : Map.of();
    Map<String, Pair> macOverride =
            keymaps ? Map.of() : macOverrides(method(source, "KeyCombination chord(boolean mac)"));
    Pair palette = keymaps ? Pair.NONE : paletteDefault(show(repo, ref, SETTINGS));

    LinkedHashMap<String, List<Cmd>> groups = new LinkedHashMap<>();
    for (String constant : constants) {
        Matcher m = Pattern.compile("^(\\w+)\\s*\\(", Pattern.DOTALL).matcher(constant);
        if (!m.find()) throw new IllegalStateException("cannot read constant: " + constant);
        String name = m.group(1);
        List<String> a = arguments(constant, m.end());
        int expected = catalog ? 2 : 4;
        if (a.size() != expected)
            throw new IllegalStateException(name + " has " + a.size() + " arguments, not " + expected);
        String title;
        String description;
        Pair chord;
        if (catalog) {
            String id = string(a.get(0));
            title = message(messages, "command." + id);
            description = message(messages, "command." + id + ".desc");
            String pc = pcKeys.get(id);
            String mac = macKeys.get(id);
            chord = new Pair(pc == null ? null : tokenKeys(pc, false), mac == null ? null : tokenKeys(mac, true));
        } else if (keymaps) {
            String id = string(a.get(0));
            title = string(a.get(1));
            description = string(a.get(2));
            String pc = pcKeys.get(id);
            String mac = macKeys.get(id);
            chord = new Pair(pc == null ? null : tokenKeys(pc, false), mac == null ? null : tokenKeys(mac, true));
        } else {
            title = string(a.get(0));
            description = string(a.get(1));
            chord = name.equals("PALETTE") ? palette : chord(a.get(3));
            Pair mac = macOverride.get(name);
            if (mac != null) chord = new Pair(chord.pc(), mac.mac());
        }
        String group = groupOf.get(name);
        if (group == null) throw new IllegalStateException(name + " is in no group in Command.group()");
        // The site's anchors and palette demo know a command by its constant in camel case.
        groups.computeIfAbsent(group, g -> new ArrayList<>())
                .add(new Cmd(camel(name), title, description, chord.pc(), chord.mac()));
    }

    StringBuilder ts = new StringBuilder();
    ts.append("// AUTO-GENERATED by scripts/GenCommands.java — do not edit by hand.\n")
            .append("// Source: the Command enum in the Mirada repository, at ").append(ref).append(".\n")
            .append("// Re-run the script after a release, or after a command or a chord changes.\n\n")
            .append("// A chord is its keys in order, given for Linux and Windows (pc) and for macOS (mac): Mirada's\n")
            .append("// shortcut key is Ctrl off macOS and Cmd on it, and a Mac has its own keys for a few commands.\n")
            .append("// Modifiers are Ctrl, Shift and Alt on pc; Ctrl, Option, Shift and Cmd on mac. null: none.\n")
            .append("export type Chord = string[];\n")
            .append("export type Cmd = { id: string; title: string; description: string; pc: Chord | null; mac: Chord | null };\n")
            .append("export type CmdGroup = { title: string; commands: Cmd[] };\n\n")
            .append("export const commandGroups: CmdGroup[] = [\n");
    groups.forEach((title, cmds) -> appendGroup(ts, title, cmds));
    ts.append("];\n");
    Files.writeString(OUT.resolve("commands.ts"), ts.toString());

    String version = version(repo, ref);
    Files.writeString(
            OUT.resolve("version.ts"),
            "// AUTO-GENERATED by scripts/GenCommands.java — do not edit by hand.\n"
                    + "// Source: Mirada's pom.xml <version> at " + ref + ", or the newest released CHANGELOG.md\n"
                    + "// section while the pom is on a -SNAPSHOT between releases.\n\n"
                    + "export const version = " + js(version) + ";\n");

    int count = groups.values().stream().mapToInt(List::size).sum();
    System.out.println("Wrote " + count + " commands in " + groups.size() + " groups and version " + version
            + " from " + ref + ".");
}

static void appendGroup(StringBuilder ts, String title, List<Cmd> cmds) {
    ts.append("  {\n    title: ").append(js(title)).append(",\n    commands: [\n");
    for (Cmd c : cmds) {
        ts.append("      { id: ")
                .append(js(c.id()))
                .append(", title: ")
                .append(js(c.title()))
                .append(", description: ")
                .append(js(c.description()))
                .append(", pc: ")
                .append(chordJs(c.pc()))
                .append(", mac: ")
                .append(chordJs(c.mac()))
                .append(" },\n");
    }
    ts.append("    ],\n  },\n");
}

static String chordJs(List<String> chord) {
    return chord == null ? "null" : "[" + String.join(", ", chord.stream().map(x -> js(x)).toList()) + "]";
}

// --- reading the enum ---

/** The enum's constants, each as written ("NEXT(\"Next Image\", …, key(KeyCode.RIGHT))"). */
static List<String> constants(String s, int from) {
    List<String> out = new ArrayList<>();
    int depth = 0;
    boolean inString = false;
    StringBuilder cur = new StringBuilder();
    for (int i = from; i < s.length(); i++) {
        char ch = s.charAt(i);
        if (inString) {
            cur.append(ch);
            if (ch == '\\') cur.append(s.charAt(++i));
            else if (ch == '"') inString = false;
            continue;
        }
        if (ch == '"') inString = true;
        if (ch == '(' || ch == '{') depth++;
        if (ch == ')' || ch == '}') depth--;
        if (depth == 0 && (ch == ',' || ch == ';')) {
            if (!cur.toString().isBlank()) out.add(cur.toString().strip());
            cur.setLength(0);
            if (ch == ';') return out;
            continue;
        }
        cur.append(ch);
    }
    throw new IllegalStateException("the enum's constants never end");
}

/** The body of the method whose declaration contains {@code signature}, braces matched. */
static String method(String s, String signature) {
    int at = s.indexOf(signature);
    if (at < 0) throw new IllegalStateException(signature + " not found");
    int open = s.indexOf('{', at);
    int depth = 0;
    for (int i = open; i < s.length(); i++) {
        if (s.charAt(i) == '{') depth++;
        if (s.charAt(i) == '}' && --depth == 0) return s.substring(open + 1, i);
    }
    throw new IllegalStateException("unbalanced " + signature);
}

/**
 * {@code case A, B -> "Folder";} arms, or {@code case A, B -> tr("group.folder");} with the heading in
 * the catalog: each constant's heading.
 */
static Map<String, String> groupOf(String body, Properties messages) {
    Map<String, String> out = new HashMap<>();
    Matcher m = Pattern.compile("case\\s+([\\w\\s,]+?)\\s*->\\s*(?:tr\\(\\s*)?\"([^\"]+)\"(\\s*\\))?")
            .matcher(body);
    while (m.find()) {
        String heading = m.group(3) != null ? message(messages, m.group(2)) : m.group(2);
        for (String name : m.group(1).split("\\s*,\\s*")) out.put(name.strip(), heading);
    }
    return out;
}

/** The English catalog's string for {@code key}; a missing one stops the script rather than guessing. */
static String message(Properties messages, String key) {
    String value = messages.getProperty(key);
    if (value == null) throw new IllegalStateException("no " + key + " in " + MESSAGES);
    return value.strip();
}

/** The arms of chord(boolean mac)'s switch: the Mac's own chord for a command, as Finder's keys. */
static Map<String, Pair> macOverrides(String body) {
    Map<String, Pair> out = new HashMap<>();
    Matcher m = Pattern.compile("case\\s+([\\w\\s,]+?)\\s*->\\s*([^;]+);").matcher(body);
    while (m.find()) {
        Pair p = chord(m.group(2).strip());
        for (String name : m.group(1).split("\\s*,\\s*")) out.put(name.strip(), p);
    }
    return out;
}

/** The palette's first default key, from Settings.defaultPaletteKeys: "C-S-p" here, "Cmd-S-p" on a Mac. */
static Pair paletteDefault(String settings) {
    Matcher m = Pattern.compile("\\(mac \\? \"([^\"]+)\" : \"([^\"]+)\"\\)")
            .matcher(method(stripComments(settings), "defaultPaletteKeys(boolean mac)"));
    if (!m.find()) throw new IllegalStateException("defaultPaletteKeys not understood");
    return new Pair(keyChord(m.group(2), false), keyChord(m.group(1), true));
}

/** An Emacs-style KeyChord token ("C-S-p", "Cmd-S-p") as keys. */
static List<String> keyChord(String token, boolean mac) {
    String[] parts = token.split("-");
    List<String> mods = new ArrayList<>();
    for (int i = 0; i < parts.length - 1; i++) {
        mods.add(switch (parts[i]) {
            case "C" -> "Ctrl";
            case "S" -> "Shift";
            case "M" -> mac ? "Option" : "Alt";
            case "Cmd" -> "Cmd";
            default -> throw new IllegalStateException("unknown modifier in " + token);
        });
    }
    return ordered(mods, parts[parts.length - 1].toUpperCase(Locale.ROOT), mac);
}

// --- keymaps ---

static final Pattern BINDING = Pattern.compile("\"([a-z]+\\.[a-zA-Z0-9]+)\"\\s*:\\s*(\\[[^\\]]*\\]|\"(?:\\\\.|[^\"\\\\])*\")");

/** A keymap's "bindings" (not its "vertical" ones): each command id and the first of its keys, the one shown. */
static Map<String, String> firstKeys(String json) {
    int at = json.indexOf("\"bindings\"");
    if (at < 0) throw new IllegalStateException("a keymap without bindings");
    int open = json.indexOf('{', at);
    int depth = 0;
    int close = -1;
    for (int i = open; i < json.length() && close < 0; i++) {
        if (json.charAt(i) == '{') depth++;
        if (json.charAt(i) == '}' && --depth == 0) close = i;
    }
    Map<String, String> out = new HashMap<>();
    Matcher m = BINDING.matcher(json.substring(open + 1, close));
    while (m.find()) {
        Matcher first = LITERAL.matcher(m.group(2));
        if (first.find()) out.put(m.group(1), first.group(1).replace("\\\"", "\"").replace("\\\\", "\\"));
    }
    return out;
}

/** A KeyChord token from a keymap ("C-S-p", "Cmd-backspace", "plus", "C-?") as keys. */
static List<String> tokenKeys(String token, boolean mac) {
    List<String> mods = new ArrayList<>();
    String rest = token;
    while (true) {
        if (rest.startsWith("Cmd-") && rest.length() > 4) {
            mods.add("Cmd");
            rest = rest.substring(4);
        } else if (rest.startsWith("C-") && rest.length() > 2) {
            mods.add("Ctrl");
            rest = rest.substring(2);
        } else if (rest.startsWith("M-") && rest.length() > 2) {
            mods.add(mac ? "Option" : "Alt");
            rest = rest.substring(2);
        } else if (rest.startsWith("S-") && rest.length() > 2) {
            mods.add("Shift");
            rest = rest.substring(2);
        } else break;
    }
    String key = switch (rest) {
        case "right" -> "Right";
        case "left" -> "Left";
        case "up" -> "Up";
        case "down" -> "Down";
        case "home" -> "Home";
        case "end" -> "End";
        case "pageup" -> "Page Up";
        case "pagedown" -> "Page Down";
        case "enter" -> "Enter";
        case "escape" -> "Esc";
        case "delete" -> "Delete";
        case "backspace" -> "Backspace";
        case "space" -> "Space";
        case "tab" -> "Tab";
        case "plus", "add" -> "+";
        case "subtract" -> "-";
        default -> {
            if (rest.matches("f\\d{1,2}")) yield rest.toUpperCase(Locale.ROOT);
            if (rest.codePointCount(0, rest.length()) == 1) yield rest.toUpperCase(Locale.ROOT);
            throw new IllegalStateException("key not understood in " + token);
        }
    };
    return ordered(mods, key, mac);
}

static boolean exists(Path repo, String ref, String path) throws IOException, InterruptedException {
    Process p = new ProcessBuilder("git", "-C", repo.toString(), "cat-file", "-e", ref + ":" + path).start();
    return p.waitFor() == 0;
}

// --- chords ---

static final Pattern HELPER = Pattern.compile("^(key|shift|shortcut)\\(KeyCode\\.(\\w+)\\)$");
static final Pattern TEXT = Pattern.compile("^text\\(\"(.+)\"\\)$");
static final Pattern CODE_COMBINATION =
        Pattern.compile("^new KeyCodeCombination\\(KeyCode\\.(\\w+)((?:\\s*,\\s*KeyCombination\\.\\w+)*)\\)$");
static final Pattern CHAR_COMBINATION =
        Pattern.compile("^new KeyCharacterCombination\\(\"(.+?)\"((?:\\s*,\\s*KeyCombination\\.\\w+)*)\\)$");

static Pair chord(String expr) {
    expr = expr.replaceAll("\\s+", " ").strip();
    if (expr.equals("none()")) return Pair.NONE;
    Matcher m = HELPER.matcher(expr);
    if (m.matches()) {
        String key = keyName(m.group(2));
        return switch (m.group(1)) {
            case "key" -> both(List.of(), key);
            case "shift" -> both(List.of("SHIFT_DOWN"), key);
            default -> both(List.of("SHORTCUT_DOWN"), key);
        };
    }
    m = TEXT.matcher(expr);
    if (m.matches()) return both(List.of(), m.group(1));
    m = CODE_COMBINATION.matcher(expr);
    if (m.matches()) return both(modifiers(m.group(2)), keyName(m.group(1)));
    m = CHAR_COMBINATION.matcher(expr);
    if (m.matches()) return both(modifiers(m.group(2)), m.group(1));
    throw new IllegalStateException("chord expression not understood: " + expr);
}

static List<String> modifiers(String list) {
    return Arrays.stream(list.split(","))
            .map(String::strip)
            .filter(s -> !s.isEmpty())
            .map(s -> s.substring("KeyCombination.".length()))
            .toList();
}

static Pair both(List<String> modifiers, String key) {
    return new Pair(ordered(names(modifiers, false), key, false), ordered(names(modifiers, true), key, true));
}

static List<String> names(List<String> modifiers, boolean mac) {
    return modifiers.stream()
            .map(m -> switch (m) {
                case "SHORTCUT_DOWN" -> mac ? "Cmd" : "Ctrl";
                case "CONTROL_DOWN" -> "Ctrl";
                case "SHIFT_DOWN" -> "Shift";
                case "ALT_DOWN" -> mac ? "Option" : "Alt";
                case "META_DOWN" -> "Cmd";
                default -> throw new IllegalStateException("unknown modifier " + m);
            })
            .toList();
}

/** Modifiers in the order each platform writes them: Ctrl+Shift+P, and ⌃⌥⇧⌘ on a Mac. */
static List<String> ordered(List<String> modifiers, String key, boolean mac) {
    List<String> order = mac ? List.of("Ctrl", "Option", "Shift", "Cmd") : List.of("Ctrl", "Alt", "Shift");
    List<String> out = new ArrayList<>(order.stream().filter(modifiers::contains).toList());
    out.add(key);
    return out;
}

static String keyName(String code) {
    return switch (code) {
        case "COMMA" -> ",";
        case "EQUALS" -> "=";
        case "MINUS" -> "-";
        case "SLASH" -> "/";
        case "OPEN_BRACKET" -> "[";
        case "CLOSE_BRACKET" -> "]";
        case "UP" -> "Up";
        case "DOWN" -> "Down";
        case "LEFT" -> "Left";
        case "RIGHT" -> "Right";
        case "HOME" -> "Home";
        case "END" -> "End";
        case "ENTER" -> "Enter";
        case "ESCAPE" -> "Esc";
        case "DELETE" -> "Delete";
        case "BACK_SPACE" -> "Backspace";
        case "SPACE" -> "Space";
        case "PAGE_UP" -> "Page Up";
        case "PAGE_DOWN" -> "Page Down";
        default -> {
            if (code.startsWith("DIGIT")) yield code.substring(5);
            if (code.matches("[A-Z]|F\\d{1,2}")) yield code;
            throw new IllegalStateException("unknown key code " + code);
        }
    };
}

// --- strings and source ---

static final Pattern LITERAL = Pattern.compile("\"((?:\\\\.|[^\"\\\\])*)\"");

/** A string argument, concatenations ("a" + "b") joined. */
static String string(String expr) {
    StringBuilder out = new StringBuilder();
    Matcher m = LITERAL.matcher(expr);
    while (m.find()) out.append(m.group(1).replace("\\\"", "\"").replace("\\\\", "\\"));
    String rest = LITERAL.matcher(expr).replaceAll("").replaceAll("[\\s+]", "");
    if (!rest.isEmpty()) throw new IllegalStateException("not a string literal: " + expr);
    return out.toString();
}

/** NEXT_IMAGE → nextImage. */
static String camel(String name) {
    StringBuilder out = new StringBuilder();
    boolean upper = false;
    for (char ch : name.toLowerCase(Locale.ROOT).toCharArray()) {
        if (ch == '_') upper = true;
        else {
            out.append(upper ? Character.toUpperCase(ch) : ch);
            upper = false;
        }
    }
    return out.toString();
}

/** The source without its comments, strings left whole. */
static String stripComments(String s) {
    StringBuilder out = new StringBuilder();
    boolean inString = false;
    for (int i = 0; i < s.length(); i++) {
        char ch = s.charAt(i);
        if (inString) {
            out.append(ch);
            if (ch == '\\') out.append(s.charAt(++i));
            else if (ch == '"') inString = false;
        } else if (ch == '"') {
            inString = true;
            out.append(ch);
        } else if (s.startsWith("//", i)) {
            int eol = s.indexOf('\n', i);
            if (eol < 0) break;
            i = eol - 1;
        } else if (s.startsWith("/*", i)) {
            i = s.indexOf("*/", i) + 1;
        } else out.append(ch);
    }
    return out.toString();
}

/** The top-level arguments of the call whose argument list starts at {@code from}. */
static List<String> arguments(String s, int from) {
    List<String> out = new ArrayList<>();
    int depth = 0;
    boolean inString = false;
    StringBuilder cur = new StringBuilder();
    for (int i = from; i < s.length(); i++) {
        char ch = s.charAt(i);
        if (inString) {
            cur.append(ch);
            if (ch == '\\') cur.append(s.charAt(++i));
            else if (ch == '"') inString = false;
            continue;
        }
        switch (ch) {
            case '"' -> {
                inString = true;
                cur.append(ch);
            }
            case '(', '{', '[' -> {
                depth++;
                cur.append(ch);
            }
            case ')', '}', ']' -> {
                if (depth == 0) {
                    out.add(cur.toString().strip());
                    return out;
                }
                depth--;
                cur.append(ch);
            }
            case ',' -> {
                if (depth == 0) {
                    out.add(cur.toString().strip());
                    cur.setLength(0);
                } else cur.append(ch);
            }
            default -> cur.append(ch);
        }
    }
    throw new IllegalStateException("unbalanced call at " + from);
}

static String show(Path repo, String ref, String path) throws IOException, InterruptedException {
    Process p = new ProcessBuilder("git", "-C", repo.toString(), "show", ref + ":" + path).start();
    String out = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    String err = new String(p.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
    if (p.waitFor() != 0) throw new IllegalStateException("git show " + ref + ":" + path + " failed: " + err.strip());
    return out;
}

/** The newest vX.Y.Z tag, by version rather than by name ("v0.10.0" after "v0.9.0"). */
static String newestTag(Path repo) throws IOException, InterruptedException {
    Process p = new ProcessBuilder("git", "-C", repo.toString(), "tag", "--list", "v*", "--sort=-v:refname").start();
    String out = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    p.waitFor();
    return out.lines()
            .filter(t -> t.matches("v\\d+\\.\\d+\\.\\d+"))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("no release tag in " + repo));
}

static String version(Path repo, String ref) throws IOException, InterruptedException {
    String pom = show(repo, ref, "pom.xml");
    // The project's own <version> is the first one, ahead of the dependency list.
    Matcher m = Pattern.compile("<version>([^<]+)</version>").matcher(pom);
    String v = m.find() ? m.group(1).strip() : "";
    if (!v.isEmpty() && !v.endsWith("-SNAPSHOT")) return v;
    // "## [0.4.0] - date" (Keep a Changelog, from 0.4.0) or "## 0.3.0 — date" (older tags);
    // "## [Unreleased]" names no version and is passed over.
    Matcher released = Pattern.compile("(?m)^## \\[?(\\d+\\.\\d+\\.\\d+)\\b")
            .matcher(show(repo, ref, "CHANGELOG.md"));
    if (released.find()) return released.group(1);
    throw new IllegalStateException("no released version in pom.xml or CHANGELOG.md at " + ref);
}

static String js(String s) {
    return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
}
