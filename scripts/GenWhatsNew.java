// Generates src/lib/whatsnew.ts (the home page's "What's new" block and the /whats-new page) from
// Mirada's CHANGELOG.md, the one file the GitHub release and the app's release notes come from
// too (Pauta DOCS-05).
//
// - the releases: every version section of CHANGELOG.md at the newest vX.Y.Z tag, so the site
//   describes what was released and not what is on a branch;
// - `next`, "In development": the unreleased section on main (--next-ref), which is what has landed
//   but is not in a release yet.
//
// An entry is a "- " item under a "### Added/Changed/Fixed/…" heading. Its title is the bold
// one-line summary every entry opens with; its detail is the rest. Both headings are read: Keep a
// Changelog's ("## [0.3.0] - 2026-11-05", "## [Unreleased]") and the older ("## 0.3.0 — 2026-11-05",
// "## 0.4.0 — unreleased"). An entry with no bold summary stops it rather than being guessed.
//
//   java scripts/GenWhatsNew.java [/path/to/Mirada] [--ref=v0.3.0] [--next-ref=main]
//
// The checkout is the argument, else $MIRADA_REPO, else ../Mirada. Run it from the site's root. The
// output is committed, so the site builds without Mirada checked out. Re-run it after each release.
// A JDK 25 compact source file (JEP 512): no build step, nothing outside java.base.

static final Path OUT = Path.of("src/lib/whatsnew.ts");

/** How many highlights the home page shows, newest release first, and how many in development. */
static final int HIGHLIGHTS = 6;
static final int NEXT = 6;

/** An entry: its bold summary, the rest, and the whole of it as written. */
record Entry(String title, String detail, String markdown) {}

record Section(String heading, List<Entry> entries) {}

record Release(String version, String date, List<Section> sections) {}

static final Pattern VERSION = Pattern.compile("^## \\[?(\\d+\\.\\d+\\.\\d+)\\]?\\s*(?:[-—–]\\s*(.*))?$");
static final Pattern UNRELEASED = Pattern.compile("^## (?:\\[Unreleased\\]|\\d+\\.\\d+\\.\\d+\\s*[-—–]\\s*unreleased)\\s*$",
        Pattern.CASE_INSENSITIVE);
static final Pattern BOLD = Pattern.compile("^\\*\\*(.+?)\\*\\*\\s*(.*)$", Pattern.DOTALL);

void main(String[] args) throws IOException, InterruptedException {
    String repoArg = null;
    String ref = null;
    String nextRef = "main";
    for (String a : args) {
        if (a.startsWith("--ref=")) ref = a.substring("--ref=".length());
        else if (a.startsWith("--next-ref=")) nextRef = a.substring("--next-ref=".length());
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

    List<Release> releases = new ArrayList<>();
    List<Section> unused = new ArrayList<>();
    parse(git(repo, "show", ref + ":CHANGELOG.md"), releases, unused);
    if (releases.isEmpty()) throw new IllegalStateException("no version sections in CHANGELOG.md at " + ref);

    List<Release> ignored = new ArrayList<>();
    List<Section> unreleased = new ArrayList<>();
    parse(git(repo, "show", nextRef + ":CHANGELOG.md"), ignored, unreleased);

    String ts = render(releases, unreleased, ref, nextRef);
    Files.createDirectories(OUT.getParent());
    Files.writeString(OUT, ts);
    int entries = releases.stream().mapToInt(r -> count(r.sections())).sum();
    System.out.printf(
            "Wrote %s: %d releases (%d entries) from %s, %d in development from %s%n",
            OUT, releases.size(), entries, ref, count(unreleased), nextRef);
}

/** Fills {@code releases} with the version sections and {@code unreleased} with the unreleased one. */
static void parse(String changelog, List<Release> releases, List<Section> unreleased) {
    List<Section> into = null;
    List<Entry> entries = null;
    StringBuilder item = null;
    for (String line : (changelog + "\n## end").split("\n", -1)) {
        boolean heading = line.startsWith("## ") || line.startsWith("### ");
        boolean bullet = line.startsWith("- ");
        if ((heading || bullet || line.isBlank()) && item != null && entries != null) {
            entries.add(entry(item.toString()));
            item = null;
        }
        if (line.startsWith("## ")) {
            entries = null;
            Matcher v = VERSION.matcher(line.strip());
            if (UNRELEASED.matcher(line.strip()).matches()) {
                into = unreleased;
            } else if (v.matches()) {
                String date = v.group(2) == null ? "" : v.group(2).strip();
                Release release = new Release(v.group(1), date, new ArrayList<>());
                releases.add(release);
                into = release.sections();
            } else {
                into = null;
            }
        } else if (line.startsWith("### ") && into != null) {
            entries = new ArrayList<>();
            into.add(new Section(line.substring(4).strip(), entries));
        } else if (bullet && entries != null) {
            item = new StringBuilder(line.substring(2).strip());
        } else if (item != null && !line.isBlank()) {
            item.append(' ').append(line.strip());
        }
    }
}

static Entry entry(String markdown) {
    Matcher m = BOLD.matcher(markdown);
    if (!m.matches()) throw new IllegalStateException("an entry with no bold summary: " + markdown);
    String title = m.group(1).strip();
    if (title.endsWith(".")) title = title.substring(0, title.length() - 1);
    // "**Copy works on a greyscale PNG**: it did nothing" reads as title and detail once the colon or
    // comma that ran on from the summary is gone; the whole entry keeps it.
    String detail = m.group(2).strip().replaceFirst("^[:,;]\\s*", "");
    return new Entry(title, detail, markdown);
}

static int count(List<Section> sections) {
    return sections.stream().mapToInt(s -> s.entries().size()).sum();
}

static String render(List<Release> releases, List<Section> unreleased, String ref, String nextRef) {
    StringBuilder out = new StringBuilder();
    out.append("// Generated by scripts/GenWhatsNew.java from Mirada's CHANGELOG.md (the releases at ")
            .append(ref)
            .append(", In development at ")
            .append(nextRef)
            .append(").\n// Do not edit: run `java scripts/GenWhatsNew.java` after a release.\n\n");
    out.append("export type NewsItem = { title: string; detail: string };\n");
    out.append("export type ReleaseSection = { heading: string; items: string[] };\n");
    out.append("export type Release = { version: string; date: string; sections: ReleaseSection[] };\n\n");

    out.append("/** Every release, newest first, each entry in full as HTML, its summary in <strong>. */\n");
    out.append("export const releases: Release[] = [\n");
    for (Release r : releases) {
        out.append("  { version: ").append(js(r.version())).append(", date: ").append(js(r.date()));
        out.append(", sections: [\n");
        for (Section s : r.sections()) {
            out.append("    { heading: ").append(js(s.heading())).append(", items: [\n");
            for (Entry e : s.entries())
                out.append("      ").append(js(html(e.markdown(), true))).append(",\n");
            out.append("    ] },\n");
        }
        out.append("  ] },\n");
    }
    out.append("];\n\n");

    out.append("/** The home page's highlights: the newest release's entries, then those before it. */\n");
    out.append("export const whatsNew: NewsItem[] = [\n");
    int shown = 0;
    for (int i = 0; i < releases.size() && shown < HIGHLIGHTS; i++) {
        Release r = releases.get(i);
        for (Section s : r.sections()) {
            for (Entry e : s.entries()) {
                if (shown == HIGHLIGHTS) break;
                String since = i == 0 ? "" : "Since " + r.version() + ": ";
                out.append("  ").append(item(html(e.title()), since + html(brief(e.detail())))).append(",\n");
                shown++;
            }
        }
    }
    out.append("];\n\n");

    out.append("/** On main and headed for the next release, but in no release yet. */\n");
    out.append("export const next: NewsItem[] = [\n");
    shown = 0;
    for (Section s : unreleased) {
        for (Entry e : s.entries()) {
            if (shown == NEXT) break;
            out.append("  ").append(item(html(e.title()), html(brief(e.detail())))).append(",\n");
            shown++;
        }
    }
    out.append("];\n\n");
    out.append("/** Every entry in development, in full as HTML, for the What's new page. */\n");
    out.append("export const inDevelopment: ReleaseSection[] = [\n");
    for (Section s : unreleased) {
        out.append("  { heading: ").append(js(s.heading())).append(", items: [\n");
        for (Entry e : s.entries()) out.append("    ").append(js(html(e.markdown(), true))).append(",\n");
        out.append("  ] },\n");
    }
    out.append("];\n\n");
    out.append("/** How many entries are in development in all, of which `next` shows the first few. */\n");
    out.append("export const nextCount = ").append(count(unreleased)).append(";\n");
    return out.toString();
}

static String item(String title, String detail) {
    return "{ title: " + js(title) + ", detail: " + js(detail) + " }";
}

/** The detail cut for a one-line list: its first sentence or two, under 220 characters. */
static String brief(String detail) {
    if (detail.length() <= 220) return detail;
    int cut = -1;
    for (int i = detail.indexOf(". "); i > 0 && i < 220; i = detail.indexOf(". ", i + 1)) cut = i + 1;
    String s;
    if (cut > 40) {
        s = detail.substring(0, cut);
    } else {
        int space = detail.lastIndexOf(' ', 210);
        s = detail.substring(0, space < 0 ? 210 : space) + "…";
    }
    // Never leave a code span open.
    if (s.chars().filter(c -> c == '`').count() % 2 == 1) s = s.substring(0, s.lastIndexOf('`')).strip() + "…";
    return s;
}

static String html(String s) {
    return html(s, false);
}

/** The changelog's inline Markdown as safe HTML: code kept, links as their text, bold kept or flattened. */
static String html(String s, boolean strong) {
    s = s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    s = s.replaceAll("`([^`]+)`", "<code>$1</code>");
    s = s.replaceAll("\\*\\*([^*]+)\\*\\*", strong ? "<strong>$1</strong>" : "$1");
    s = s.replaceAll("(?<![\\w*])\\*([^*\\s][^*]*)\\*(?![\\w*])", "<em>$1</em>");
    s = s.replaceAll("\\[([^\\]]+)\\]\\([^)]+\\)", "$1");
    return s;
}

static String js(String s) {
    StringBuilder b = new StringBuilder("\"");
    for (char c : s.toCharArray()) {
        switch (c) {
            case '"' -> b.append("\\\"");
            case '\\' -> b.append("\\\\");
            case '\n' -> b.append("\\n");
            default -> b.append(c);
        }
    }
    return b.append('"').toString();
}

static String newestTag(Path repo) throws IOException, InterruptedException {
    for (String tag : git(repo, "tag", "--list", "v*", "--sort=-v:refname").split("\n")) {
        if (tag.strip().matches("v\\d+\\.\\d+\\.\\d+")) return tag.strip();
    }
    throw new IllegalStateException("no vX.Y.Z tag in " + repo);
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
