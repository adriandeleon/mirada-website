// Generates src/content/docs/pauta.md, the Pauta conformance page (Pauta PAUTA-13 and PAUTA-14),
// from a Mirada checkout: the edition `pauta.version` names in the pom, the conformance its
// exceptions give, and every exception of `src/main/resources/com/mirada/pauta.json` under its
// kind, each rule ID linked to the edition's rule, with its reason and its link. The conformance is
// computed as com.mirada.PautaConformance does: Partial while a MUST is pending, Core while a SHOULD
// is, Complete otherwise; MAYs, deviations, app-only and not-applicable entries never count.
//
//   java scripts/GenPauta.java [/path/to/Mirada] [--ref=v0.4.0]
//
// The checkout is the argument, else $MIRADA_REPO, else ../Mirada. Run it from the site's root.
// The files are read at a git ref, not from the working tree: the newest vX.Y.Z tag that carries a
// pauta.json by default, else the checkout's HEAD (it says which), or --ref=<tag|branch|HEAD>.
// The output is committed, so the site builds without Mirada checked out. Re-run it after each
// release, and whenever pauta.json changes. Anything it does not expect stops it.
// A JDK 25 compact source file (JEP 512): no build step, nothing outside java.base.

static final Path OUT = Path.of("src/content/docs/pauta.md");

static final String PAUTA_JSON = "src/main/resources/com/mirada/pauta.json";

/** The kinds in the order the app's Pauta window lists them, with their headings there. */
static final List<String> KINDS = List.of("pending", "deviation", "app-only", "not-applicable");

static final Map<String, String> HEADING = Map.of(
        "pending", "Pending",
        "deviation", "Deviation",
        "app-only", "For each app made from it",
        "not-applicable", "Not applicable");

static final Map<String, String> INTRO = Map.of(
        "pending", "Rules Mirada means to meet and does not yet, each with the issue that tracks it.",
        "deviation", "Rules Mirada breaks on purpose, each with the decision that explains why.",
        "app-only", "Rules only an app made from a template can meet.",
        "not-applicable", "Rules that do not apply to an image viewer.");

record Entry(String rule, String level, String url, String kind, String reason, String link) {}

record Checked(String rule, String edition, String date, String version) {}

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
    if (ref == null) ref = defaultRef(repo);

    String edition = pautaVersion(git(repo, "show", ref + ":pom.xml"));
    Map<?, ?> root = object(new Json(git(repo, "show", ref + ":" + PAUTA_JSON)).parse(), "pauta.json");
    List<Entry> entries = new ArrayList<>();
    for (Object node : list(root.get("exceptions"), "exceptions")) entries.add(entry(object(node, "an exception"), edition));
    List<Checked> checked = new ArrayList<>();
    if (root.get("checked") != null) {
        for (Object node : list(root.get("checked"), "checked")) {
            Map<?, ?> o = object(node, "a checked rule");
            checked.add(new Checked(text(o, "rule"), text(o, "edition"), text(o, "date"), optional(o, "version")));
        }
    }

    String md = render(edition, entries, checked, ref);
    Files.writeString(OUT, md);
    System.out.printf("Wrote %s: Pauta %s, %s, %d exceptions, from %s%n",
            OUT, edition, conformance(entries), entries.size(), ref);
}

/** The newest release tag with a pauta.json, else HEAD: the site describes a release once there is one. */
static String defaultRef(Path repo) throws IOException, InterruptedException {
    for (String tag : git(repo, "tag", "--list", "v*", "--sort=-v:refname").split("\n")) {
        tag = tag.strip();
        if (!tag.matches("v\\d+\\.\\d+\\.\\d+")) continue;
        if (exists(repo, tag + ":" + PAUTA_JSON)) return tag;
        break;
    }
    String head = git(repo, "rev-parse", "--abbrev-ref", "HEAD").strip();
    System.out.println("No release tag carries " + PAUTA_JSON + " yet: reading HEAD (" + head + ")");
    return head;
}

static String pautaVersion(String pom) {
    Matcher m = Pattern.compile("<pauta\\.version>\\s*(\\d+\\.\\d+\\.\\d+)\\s*</pauta\\.version>").matcher(pom);
    if (!m.find()) throw new IllegalStateException("no <pauta.version> in pom.xml");
    return m.group(1);
}

static Entry entry(Map<?, ?> o, String edition) {
    Entry e = new Entry(text(o, "rule"), text(o, "level"), text(o, "url"), text(o, "kind"), text(o, "reason"),
            optional(o, "link"));
    if (!e.rule().matches("[A-Z]+-\\d+")) throw new IllegalStateException("not a rule ID: " + e.rule());
    if (!List.of("MUST", "SHOULD", "MAY").contains(e.level()))
        throw new IllegalStateException(e.rule() + ": unknown level " + e.level());
    if (!KINDS.contains(e.kind())) throw new IllegalStateException(e.rule() + ": unknown kind " + e.kind());
    String fixed = "https://pauta-project.dev/" + edition + "/";
    if (!e.url().startsWith(fixed) || !e.url().toLowerCase(Locale.ROOT).endsWith("#" + e.rule().toLowerCase(Locale.ROOT)))
        throw new IllegalStateException(e.rule() + ": its url is not the rule at " + fixed + ": " + e.url());
    return e;
}

/** As com.mirada.PautaConformance computes it. */
static String conformance(List<Entry> entries) {
    if (pending(entries, "MUST") > 0) return "Partial";
    if (pending(entries, "SHOULD") > 0) return "Core";
    return "Complete";
}

static long pending(List<Entry> entries, String level) {
    return entries.stream().filter(e -> e.kind().equals("pending") && e.level().equals(level)).count();
}

static String render(String edition, List<Entry> entries, List<Checked> checked, String ref) {
    String rules = "https://pauta-project.dev/" + edition + "/";
    String conformance = conformance(entries);
    long musts = pending(entries, "MUST");
    long shoulds = pending(entries, "SHOULD");
    StringBuilder b = new StringBuilder();
    b.append("---\n");
    b.append("title: Pauta conformance\n");
    b.append("description: The edition of Pauta, the adl desktop apps guidelines, Mirada is built to, how fully it conforms, and every rule it does not meet as written.\n");
    b.append("category: Help\n");
    b.append("order: 12\n");
    b.append("---\n\n");
    b.append("<!-- Generated by scripts/GenPauta.java from Mirada's pauta.json at ").append(ref)
            .append(". Do not edit by hand: re-run it. -->\n\n");
    b.append("Mirada is built to [Pauta ").append(edition).append("](").append(rules)
            .append("), the adl desktop apps guidelines.\n");
    b.append("**Help › Pauta Conformance** in the app shows the same list as this page, and About names the\n");
    b.append("conformance.\n\n");
    b.append("## Conformance: ").append(conformance).append("\n\n");
    b.append(switch (conformance) {
        case "Partial" -> "Mirada does not meet every MUST of this edition yet: " + plural(musts, "is", "are")
                + " pending" + (shoulds > 0 ? ", and " + plural(shoulds, "SHOULD is", "SHOULDs are") + " too" : "") + ".";
        case "Core" -> "Mirada meets every MUST of this edition; " + plural(shoulds, "SHOULD is", "SHOULDs are")
                + " pending.";
        default -> "Mirada meets every MUST and SHOULD of this edition.";
    });
    b.append("\n\nAn app conforms **Complete** when it meets every MUST and SHOULD of the edition, **Core** when\n");
    b.append("it meets every MUST, and **Partial** otherwise; a deviation, a rule that does not apply and a MAY\n");
    b.append("never count.\n\n");
    if (entries.isEmpty()) {
        b.append("No exceptions: Mirada meets every MUST and SHOULD of Pauta ").append(edition).append(".\n");
    }
    for (String kind : KINDS) {
        List<Entry> of = entries.stream().filter(e -> e.kind().equals(kind)).toList();
        if (of.isEmpty()) continue;
        b.append("## ").append(HEADING.get(kind)).append(" (").append(of.size()).append(")\n\n");
        b.append(INTRO.get(kind)).append("\n\n");
        String linkColumn = kind.equals("deviation") ? "Decision" : "Details";
        b.append("| Rule | Level | Why | ").append(linkColumn).append(" |\n");
        b.append("|---|---|---|---|\n");
        for (Entry e : of) {
            b.append("| [").append(e.rule()).append("](").append(e.url()).append(") | ").append(e.level())
                    .append(" | ").append(cell(e.reason())).append(" | ")
                    .append(e.link() == null ? "" : "[" + linkLabel(e.link()) + "](" + e.link() + ")")
                    .append(" |\n");
        }
        b.append("\n");
    }
    if (!checked.isEmpty()) {
        b.append("## Checked by hand\n\n");
        b.append("Rules a person checked in the running app and found met. A record only: they do not change the\n");
        b.append("conformance.\n\n");
        b.append("| Rule | Edition | Date | Version |\n|---|---|---|---|\n");
        for (Checked c : checked) {
            b.append("| ").append(c.rule()).append(" | ")
                    .append(c.edition()).append(" | ").append(c.date()).append(" | ")
                    .append(c.version() == null ? "" : c.version()).append(" |\n");
        }
        b.append("\n");
    }
    b.append("Every rule of the edition, with what it asks and why: [pauta-project.dev/").append(edition)
            .append("](").append(rules).append(").\n");
    return b.toString();
}

static String plural(long n, String one, String many) {
    return n == 1 ? "1 " + one : n + " " + many;
}

static String plural(long n, String word) {
    return n == 1 ? "1 " + word : n + " " + word + "s";
}

static String linkLabel(String link) {
    Matcher issue = Pattern.compile("github\\.com/[^/]+/[^/]+/issues/(\\d+)").matcher(link);
    if (issue.find()) return "Issue #" + issue.group(1);
    Matcher decision = Pattern.compile("/decisions/(\\d+)").matcher(link);
    if (decision.find()) return "Decision " + decision.group(1);
    return "Details";
}

static String cell(String text) {
    return text.replace("\\", "\\\\").replace("|", "\\|").replace("<", "&lt;").replace("\n", " ");
}

static String text(Map<?, ?> o, String key) {
    if (o.get(key) instanceof String s && !s.isBlank()) return s;
    throw new IllegalStateException("missing \"" + key + "\" in " + o);
}

static String optional(Map<?, ?> o, String key) {
    return o.get(key) instanceof String s && !s.isBlank() ? s : null;
}

static Map<?, ?> object(Object node, String what) {
    if (node instanceof Map<?, ?> m) return m;
    throw new IllegalStateException(what + " is not an object");
}

static List<?> list(Object node, String what) {
    if (node instanceof List<?> l) return l;
    throw new IllegalStateException(what + " is not an array");
}

static boolean exists(Path repo, String object) throws IOException, InterruptedException {
    Process p = new ProcessBuilder("git", "-C", repo.toString(), "cat-file", "-e", object)
            .redirectErrorStream(true)
            .start();
    p.getInputStream().readAllBytes();
    return p.waitFor() == 0;
}

static String git(Path repo, String... args) throws IOException, InterruptedException {
    List<String> command = new ArrayList<>(List.of("git", "-C", repo.toString()));
    command.addAll(List.of(args));
    Process p = new ProcessBuilder(command).redirectErrorStream(false).start();
    String out = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    String err = new String(p.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
    if (p.waitFor() != 0) throw new IllegalStateException("git " + String.join(" ", args) + ": " + err.strip());
    return out;
}

/** Just enough JSON for pauta.json: objects, arrays, strings, numbers, true, false and null. */
static final class Json {
    private final String s;
    private int i;

    Json(String s) {
        this.s = s;
    }

    Object parse() {
        Object value = value();
        space();
        if (i != s.length()) throw error("trailing text");
        return value;
    }

    private Object value() {
        space();
        if (i >= s.length()) throw error("unexpected end");
        char c = s.charAt(i);
        if (c == '{') {
            i++;
            Map<String, Object> o = new LinkedHashMap<>();
            space();
            if (peek('}')) return o;
            do {
                space();
                String key = string();
                space();
                expect(':');
                o.put(key, value());
                space();
            } while (take(','));
            expect('}');
            return o;
        }
        if (c == '[') {
            i++;
            List<Object> a = new ArrayList<>();
            space();
            if (peek(']')) return a;
            do a.add(value());
            while (spaceTake(','));
            space();
            expect(']');
            return a;
        }
        if (c == '"') return string();
        for (String word : List.of("true", "false", "null")) {
            if (s.startsWith(word, i)) {
                i += word.length();
                return word.equals("null") ? null : Boolean.valueOf(word);
            }
        }
        int start = i;
        while (i < s.length() && "+-0123456789.eE".indexOf(s.charAt(i)) >= 0) i++;
        if (start == i) throw error("unexpected '" + c + "'");
        return Double.valueOf(s.substring(start, i));
    }

    private String string() {
        expect('"');
        StringBuilder b = new StringBuilder();
        while (true) {
            if (i >= s.length()) throw error("unterminated string");
            char c = s.charAt(i++);
            if (c == '"') return b.toString();
            if (c != '\\') {
                b.append(c);
                continue;
            }
            char e = s.charAt(i++);
            switch (e) {
                case 'n' -> b.append('\n');
                case 't' -> b.append('\t');
                case 'r' -> b.append('\r');
                case 'b' -> b.append('\b');
                case 'f' -> b.append('\f');
                case 'u' -> {
                    b.append((char) Integer.parseInt(s.substring(i, i + 4), 16));
                    i += 4;
                }
                default -> b.append(e);
            }
        }
    }

    private void space() {
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) i++;
    }

    private boolean peek(char c) {
        if (i < s.length() && s.charAt(i) == c) {
            i++;
            return true;
        }
        return false;
    }

    private boolean take(char c) {
        return peek(c);
    }

    private boolean spaceTake(char c) {
        space();
        return peek(c);
    }

    private void expect(char c) {
        if (!peek(c)) throw error("expected '" + c + "'");
    }

    private IllegalStateException error(String what) {
        return new IllegalStateException("pauta.json: " + what + " at offset " + i);
    }
}
