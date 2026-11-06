---
title: "The modern formats, built in"
group: "Every format"
order: 1
summary: "HEIC from an iPhone, AVIF and WebP from the web, JPEG XL, TIFF and PSD, beside JPEG, PNG, GIF and BMP. Nothing to install: the codecs come with Mirada."
---

- **Ten formats**: JPEG, PNG, GIF, BMP, WebP, TIFF, PSD, HEIC, AVIF and JPEG XL, and the installers register Mirada for each.
- **The reference decoders**: libjpeg-turbo, libwebp, libheif (with libde265 and dav1d) and libjxl, called directly through Java's foreign function interface.
- **Built from source, bundled.** Each library is compiled from pinned sources for each platform, 3 to 4 MB in all, so every machine decodes with the same versions and nothing comes from a package manager.
- **Decoders only.** libheif is built without encoders or plugin loading, so nothing under the GPL comes along.
- **Fast.** JPEG two to three times faster than Java's own decoder; a 24-megapixel WebP in a fifth of a second instead of two; JPEG XL on every core.
- **A missing library is not a crash.** Its files still list, and open with a message naming the library.

See [Formats](/docs/formats).
