---
title: Mirada 0.1.0, the first release
description: "A fast, folder-oriented image viewer for macOS, Windows and Linux: ten formats with their codecs built in, on screen in an eighth of a second, with an editor, a properties sidebar and a command palette."
date: 2026-11-01
version: 0.1.0
---

Mirada 0.1.0 is the first version: open a photo, look, arrow through its folder, close.

## Download

From the [release page](https://github.com/adriandeleon/Mirada/releases/tag/v0.1.0): a DMG for
macOS (Apple silicon and Intel), an MSI for Windows (x64), and a deb, rpm and tar.gz for Linux (x64
and ARM64). Each registers Mirada for the image types it opens. The builds are not signed yet: see
[Getting started](/docs/getting-started#the-first-launch) for the one question each platform asks.

## What it does

- **Ten formats**: JPEG, PNG, GIF, BMP, WebP, TIFF, PSD, HEIC, AVIF and JPEG XL, through
  libjpeg-turbo, libwebp, libheif and libjxl, built from source for each platform and bundled
  (3–4 MB): nothing to install, and the same versions everywhere.
- **On screen in about an eighth of a second** on Linux. Mirada is compiled ahead of time into a
  native executable on Linux, Windows and Apple silicon, and the first image decodes while the
  window is still being built. A 24-megapixel JPEG is up 170 ms after launch.
- **The folder is the document.** Opening a file opens its folder, in natural name order
  (`IMG_9` before `IMG_10`), or by date or size. The images either side are ready before you step.
- **Upright**, from the EXIF orientation, mirrored ones included; **animated** WebP, AVIF and JPEG XL
  play, as GIFs do.
- **An editor** (<kbd>E</kbd>): crop freely or to a shape, turn and flip, undo any step, and save a
  new JPEG, PNG, TIFF or BMP; a JPEG or PNG **keeps the photo's EXIF**.
- **A properties sidebar** (<kbd>I</kbd>) with the file, the image and the camera, and every
  metadata tag.
- **A thumbnail strip** (<kbd>T</kbd>), a **slideshow** (<kbd>F5</kbd>), **full screen**,
  **Rename** (<kbd>F2</kbd>) and **Move to Trash** (<kbd>Delete</kbd>).
- **Zoom and pan**: the wheel zooms about the pointer, <kbd>0</kbd> fits and <kbd>1</kbd> is actual
  size, and <kbd>Z</kbd> keeps the zoom while you step through a burst.
- **A command palette** (<kbd>Ctrl+Shift+P</kbd> or <kbd>F1</kbd>), the same as Editora's and
  Termina's, and a Keyboard Shortcuts window (<kbd>Ctrl+?</kbd>).
- **A window laid out as GNOME Image Viewer's**: a header bar in the title bar, floating controls
  that fade, light and dark themes that follow the system.

The full list is in the [changelog](https://github.com/adriandeleon/Mirada/blob/main/CHANGELOG.md).
