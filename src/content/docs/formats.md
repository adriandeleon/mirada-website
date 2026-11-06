---
title: Formats
description: Every format Mirada opens, the decoder behind each, animation, orientation and colour, and what happens when a codec is missing.
category: Using Mirada
order: 8
---

| Format | Extensions | Decoded by |
| --- | --- | --- |
| JPEG | `.jpg` `.jpeg` `.jpe` `.jfif` | libjpeg-turbo; JavaFX for CMYK, YCCK and 12-bit JPEGs |
| PNG | `.png` | JavaFX |
| GIF, still and animated | `.gif` | JavaFX |
| BMP | `.bmp` | JavaFX; ImageIO for a 32-bit BMP with transparency |
| WebP, still and animated | `.webp` | libwebp |
| TIFF | `.tif` `.tiff` | TwelveMonkeys ImageIO, pure Java |
| Photoshop | `.psd` | TwelveMonkeys ImageIO, pure Java |
| HEIC, HEIF | `.heic` `.heif` `.hif` | libheif with libde265 |
| AVIF | `.avif` | libheif with dav1d |
| JPEG XL | `.jxl` | libjxl, on every core |

The installers register Mirada for these types, so they list it under "Open With".

## The native codecs

libjpeg-turbo, libwebp, libheif and libjxl are **built from source** for each platform, at pinned
versions, and bundled with Mirada: 3 to 4 MB in all. There is nothing to install, and every machine
decodes with the same versions. libheif is built with decoders only (libde265 for HEIC and dav1d
for AVIF, linked in), so no encoder, and nothing under the GPL, comes along.

They are fast. libjpeg-turbo decodes a JPEG two to three times faster than Java's own decoders, and
libwebp makes a 24-megapixel WebP about a fifth of a second instead of two.

**Before the window is up.** For every format but PNG, GIF and BMP, the first image starts decoding
while JavaFX is still starting, so a large photo is often ready before the window is.

**A missing codec is not fatal.** If one of the libraries cannot be loaded (a build from source on
a system without it, say), its files are still listed and open with a message naming the library.

## Upright

Phones and cameras store the pixels as the sensor read them and write down how the camera was
held. Mirada turns every photo upright from that EXIF orientation, the four mirrored ones included.
HEIF and JPEG XL say how to turn the picture in their own container as well, and their decoders
apply it, so Mirada does not turn them twice.

## Colour

Mirada shows pixels as they are stored, as JavaFX does, without applying an embedded colour
profile. The exception is JPEG XL, which libjxl converts to sRGB, so it shows in the colours every
other format shows in rather than in whatever space it was stored in.

## Animation

Animated GIF, WebP, AVIF, HEIC image sequences and JPEG XL play. The first frame shows at once,
and every frame decodes on a background thread after it, so stepping onto an animation costs no
more than onto a still. An animation past 64 megapixels, its frames counted together, stays a still.

## Transparency

Transparent pixels are drawn over a checkerboard.
