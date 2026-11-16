# Mirada website

The site for [Mirada](https://github.com/adriandeleon/Mirada), a fast, folder-oriented image viewer.
Served at **[mirada-project.dev](https://mirada-project.dev)**.

The domain is GitHub Pages' custom domain for this repository (`public/CNAME`, and Settings →
Pages). Its DNS is on Cloudflare, as Editora's and Termina's are: the apex and `www` are CNAMEs to
`adriandeleon.github.io`, **DNS only**. Proxied, Cloudflare would answer GitHub's certificate check
itself and Pages could not issue or renew the HTTPS certificate.

Everything routes through an Astro `base` (`src/lib/site.ts`), `"/"` at the domain's root.
Templates build every internal link and asset with `href()` from that file; Markdown is written
with plain root-relative links (`/docs/formats`) and a rehype plugin in `astro.config.mjs` puts them
under the base. Keep doing so: it is what makes moving the host or the path a one-file change.
Changing the host: `site` in `src/lib/site.ts`, `public/CNAME`, `public/robots.txt`, the social
cards (`scripts/gen-images.mjs`, then `--force`), and the custom domain under Settings → Pages.

Built with [Astro](https://astro.build) and deployed to GitHub Pages by
`.github/workflows/pages.yml` on every push to `main`. The stack, the layout and the look are
[Termina's site](https://github.com/adriandeleon/termina-website)'s, which are
[Torrenta's](https://github.com/adriandeleon/torrenta-website)'s, which are
[Editora's](https://github.com/adriandeleon/editora-website)'s, ported: the same components (the
Pagefind search modal, the page index rail, the palette demo), the same page shapes, the same
lockfile.

Mirada is built with the help of AI coding tools.

## Develop

```bash
npm install
npm run dev       # http://localhost:4321/
npm run build     # production build to dist/
npm run preview   # serve the build (site search only works here, after a build)
```

## Content

Most content is Markdown under `src/content/`:

- `docs/` — documentation pages (sidebar grouped by `category` + `order`; `introduction` is `/docs`)
- `blog/` — blog posts
- `news/` — release announcements
- `features/` — one page per feature (also drives the home-page grid; `summary` is HTML)

Add a Markdown file with the right frontmatter and it appears automatically; lists, the sitemap
and RSS regenerate on the next build.

**The site describes the latest release, not the development branch.** Check a claim against the
tag (`git -C ../Mirada show v0.3.0:README.md`, `…:src/main/java/…`), not the working tree, which
usually carries unreleased work. What is in development goes in `next` in `src/lib/whatsnew.ts`,
under the home page's "In development" heading, and nowhere else.

## Screenshots

The window screenshot is `public/screenshots/hero/window.jpg`, and the "A look at the window"
section renders only when it exists. A photo's screenshot is a JPEG: as a PNG it is five times the
size.

Feature screenshots go in `public/screenshots/features/<slug>.jpg` (or `.png`); the slug matches the
page URL, so `/features/editor` → `editor.jpg`. Without one the card and the page show a
placeholder. The window shot and the first three feature shots (`fast-start`, `properties`,
`thumbnails`) are from the same capture of a development build after 0.3.0, the folder shown before
the file name in the header; the rest are placeholders until a capture of the release replaces them.
Capture from a build of the release tag, on photos you are free to publish.

The docs pages' screenshots are `public/screenshots/docs/<name>.jpg`, each with a `<name>.dark.jpg`
of the same view in Mirada's dark theme, taken by `scripts/capture-docs-shots.sh` from the real window
with no window on the desktop (Mirada's `scripts/headless-run.sh`), over pictures
`scripts/gen-sample-photos.py` draws (Python 3 with NumPy and Pillow), so nothing in them belongs to
anyone else. A Markdown image alone in its paragraph becomes a figure
(`src/lib/rehype-doc-figures.mjs`, Editora's): alt text is required, the title is the caption, the
site's theme picks the light or dark capture, and a missing file fails the build. The current set is
from a development build after 0.3.0 (the `pauta-4.0.1` branch), so some of what they show (the
status bar, the keymap setting, the editor's commands in the palette) is not in 0.3.0; take them
again from the next release:

```bash
scripts/capture-docs-shots.sh                       # every shot, ../Mirada as it is checked out
scripts/capture-docs-shots.sh settings rename       # only these
```

Astro caches rendered Markdown in `.astro/`: after changing a rehype plugin, delete it before
building, or the pages keep the old output.

## Themes

The site wears the family palette, **Caret & Ink** (Editora Dark and Editora Light), the navy,
teal and periwinkle Mirada's icon is drawn in. Mirada itself is drawn by AtlantaFX's Cupertino
themes, and the header's picker has its three choices: **System** (the default, following
`prefers-color-scheme` as it changes), **Light** and **Dark**. The palette demo draws Mirada's window
in Cupertino's own colours (`--win-*` in `src/layouts/Layout.astro`, from AtlantaFX 3.0.0's
`cupertino-dark.css` and `cupertino-light.css`), so it switches with the theme as the app does.

## Generated data

`npm run build` first runs `scripts/gen-images.mjs`: the favicon and touch icons from
`public/mirada-icon.svg` (a copy of the app's `branding/mirada-icon.svg`), a social card per key page
in `public/og/`, and a WebP beside every PNG or JPEG screenshot. Outputs are kept unless their source
is newer; `--force` rebuilds them.

`src/lib/commands.ts` (the Keybindings page, the home page's keys, the palette demo) and
`src/lib/version.ts` are generated by `scripts/GenCommands.java` from Mirada's source **at the newest
release tag**: every constant of the `Command` enum (its label, its description and its chord), the
heading `Command.group()` gives it, the Mac's own chords from `Command.chord(boolean)`, the palette's
default keys from `Settings.defaultPaletteKeys`, and the pom's version. A chord expression the script
does not understand stops it rather than being guessed. It is a JDK 25 compact source file (JEP
512), run with `java`, no build step, from the site's root:

```bash
java scripts/GenCommands.java                       # ../Mirada, newest vX.Y.Z tag
java scripts/GenCommands.java /path/to/Mirada
java scripts/GenCommands.java --ref=main            # preview a branch; don't commit its output
```

or `MIRADA_REPO`. The output is committed, so the site build needs no access to the Mirada
repository (it isn't checked out in CI). Re-run it after each release. The second ways into a
command (Space for Next Image, Preview's keys on a Mac) are prose on the Keybindings page, from
`ShortcutsWindow.also`; check them there after a release too.

`src/lib/whatsnew.ts` (the home page's "What's new" block and the `/whats-new` page) is
generated by `scripts/GenWhatsNew.java` from Mirada's `CHANGELOG.md`, the file the GitHub release
and the app's release notes come from too (Pauta DOCS-05): every release section at the newest tag,
each entry's bold summary as its title, and "In development" from the unreleased section on `main`.
Re-run it after each release, and when `main`'s changelog moves enough to show:

```bash
java scripts/GenWhatsNew.java                       # ../Mirada, newest vX.Y.Z tag, In development from main
java scripts/GenWhatsNew.java --next-ref=pauta-4.0.1
```

`src/content/docs/pauta.md`, the *Pauta conformance* page (Pauta PAUTA-13 and PAUTA-14), is
generated by `scripts/GenPauta.java` from Mirada's `src/main/resources/com/mirada/pauta.json` and the
pom's `pauta.version`: the edition, the conformance (computed as `PautaConformance` does: Partial
while a MUST is pending, Core while a SHOULD is, Complete otherwise) and every exception under its
kind, each rule ID linked to the edition's rule. It reads the newest release tag that carries a
`pauta.json`, else the checkout's HEAD (and says so). The app's Pauta window links to
`/docs/pauta/`. Re-run it after each release and whenever `pauta.json` changes:

```bash
java scripts/GenPauta.java                          # ../Mirada
java scripts/GenPauta.java /path/to/Mirada --ref=pauta-6.1.0
```

## Not ported (yet)

Editora's site has versioned docs, a generated roadmap, per-command pages and a comparison page.
Mirada's docs are unversioned because nothing in the app links into them; when it does, port
Editora's routes and archive layout rather than inventing another scheme. Cloudflare Web Analytics
is left out until the site has its own token.
