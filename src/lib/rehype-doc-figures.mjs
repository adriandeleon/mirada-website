// Turns a Markdown image that stands alone in its paragraph into a captioned screenshot figure:
//
//   ![Alt text: what the picture shows](/screenshots/docs/window.jpg "Caption")
//
// becomes <figure class="doc-figure"> with the image (lazy, async decode, width and height read
// from the file so the page does not jump while it loads) linked to the full-size capture, and the
// title as <figcaption>. Alt text is required; a missing file or alt text fails the build rather
// than shipping a broken picture. Ported from Editora's site (src/lib/rehype-doc-figures.mjs).
//
// Captures come from scripts/capture-docs-shots.sh, one pixel per CSS pixel (JavaFX's headless
// screen draws at 1x), in Mirada's light theme, with the same view in the dark theme beside it as
// <name>.dark.<ext>: both are emitted and the site's theme picks one (.doc-figure in Layout.astro),
// the light capture for the light site and the dark one otherwise. A lazy image inside a
// display:none link is never fetched, so a reader downloads one of the pair.
//
// Images inline in a sentence, or several in one paragraph, are left alone.
import { existsSync } from "node:fs";
import { extname, join } from "node:path";
import sharp from "sharp";

const PUBLIC = join(process.cwd(), "public");

async function size(src, file) {
  const path = join(PUBLIC, decodeURI(src));
  if (!existsSync(path)) {
    throw new Error(`doc figure: ${src} not found under public/ (${file})`);
  }
  const { width, height } = await sharp(path).metadata();
  return { width, height };
}

function darkSibling(src) {
  const ext = extname(src);
  const dark = src.slice(0, -ext.length) + ".dark" + ext;
  return existsSync(join(PUBLIC, decodeURI(dark))) ? dark : null;
}

const isBlank = (n) => n.type === "text" && !n.value.trim();

// One link per capture, so the full-size link opens the capture in view.
function picture(src, alt, dim, cls) {
  return {
    type: "element",
    tagName: "a",
    properties: {
      href: src,
      className: ["doc-figure-link", ...(cls ? [cls] : [])],
      title: "Open the full-size screenshot",
    },
    children: [withWebp(src, {
      type: "element",
      tagName: "img",
      properties: { src, alt, width: dim.width, height: dim.height, loading: "lazy", decoding: "async" },
      children: [],
    })],
  };
}

// The WebP scripts/gen-images.mjs writes beside every capture, offered first, as the feature pages
// offer theirs; the capture itself stays the fallback and the full-size link.
function withWebp(src, img) {
  const webp = src.replace(/\.(png|jpe?g)$/i, ".webp");
  if (webp === src || !existsSync(join(PUBLIC, decodeURI(webp)))) return img;
  return {
    type: "element",
    tagName: "picture",
    properties: {},
    children: [{ type: "element", tagName: "source", properties: { srcSet: webp, type: "image/webp" }, children: [] }, img],
  };
}

export default function rehypeDocFigures() {
  return async (tree, file) => {
    const where = file?.path ?? file?.history?.[0] ?? "markdown";
    const jobs = [];
    const visit = (node) => {
      if (!node.children) return;
      node.children.forEach((child, i) => {
        if (child.type !== "element") return;
        if (child.tagName === "p") {
          const kids = child.children.filter((k) => !isBlank(k));
          if (kids.length === 1 && kids[0].type === "element" && kids[0].tagName === "img") {
            jobs.push({ parent: node, index: i, image: kids[0] });
            return;
          }
        }
        visit(child);
      });
    };
    visit(tree);

    for (const { parent, index, image } of jobs) {
      const { src, alt, title } = image.properties;
      if (typeof src !== "string" || !src.startsWith("/")) continue;
      if (!alt || !String(alt).trim()) {
        throw new Error(`doc figure: ${src} has no alt text (${where})`);
      }
      const dim = await size(src, where);
      const dark = darkSibling(src);
      const children = dark
        ? [picture(src, alt, dim, "doc-figure-light"), picture(dark, alt, await size(dark, where), "doc-figure-dark")]
        : [picture(src, alt, dim)];
      if (title) {
        children.push({
          type: "element",
          tagName: "figcaption",
          properties: {},
          children: [{ type: "text", value: String(title) }],
        });
      }
      parent.children[index] = {
        type: "element",
        tagName: "figure",
        properties: { className: ["doc-figure"] },
        children,
      };
    }
  };
}
