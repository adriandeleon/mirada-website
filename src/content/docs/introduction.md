---
title: Introduction
description: What Mirada is, what version 0.3.0 can do, what it is built from, and how the documentation is organized.
category: Getting started
order: 1
---

**Mirada** is a fast, folder-oriented image viewer. Double-click a photo, look, arrow through the
folder, close. It is free and open source under the MIT License.

It runs on macOS (Apple silicon and Intel), Windows (x64) and Linux (x64 and ARM64). The installers
carry everything it needs, the image codecs included, so there is nothing else to install.

Version 0.3.0, released on 2026-11-05, is the latest release. Downloads are on the
[releases page](https://github.com/adriandeleon/Mirada/releases).

## The idea

An image viewer is the app you open a hundred times a day for three seconds each. Mirada treats the
**folder as the document**: open one photo and the rest of the folder is already there, in the
order you'd number it, one arrow key away. The image fills the window; the properties sidebar says
which file, where it sits in the folder, how large it is, and what took it.

There is no library to import into and no catalogue to keep. Mirada looks at files where they
are, and what it changes (a rename, a move to the Trash, an edit saved as a new file) it changes on
disk, where every other app sees it.

## What it can do

- **Open** JPEG, PNG, GIF, BMP, WebP, TIFF, PSD, HEIC, AVIF and JPEG XL, turned upright from
  their EXIF orientation, and animated where the file is. See [Formats](/docs/formats).
- **Step** through the folder by name, date or size, jump to any image by typing part of its name,
  and reopen any of the last ten folders. See [The folder](/docs/the-folder).
- **Zoom, pan, rotate**, go full screen or start a slideshow. See [Viewing](/docs/viewing).
- **Show the properties**: file, image and camera, and every metadata tag. See
  [Properties](/docs/properties).
- **Crop, turn and flip**, and save the result as a new file that keeps the photo's EXIF and
  colour profile. See [Editing](/docs/editing).
- **Rename, move to the Trash, copy, drag out** and show in the file manager. See
  [Files](/docs/files).
- **Find any command by name** in the command palette, or press its key. See
  [Keyboard](/docs/keyboard) and the [Keybindings](/keybindings) reference.

## How it is built

Mirada is written in Java 25 with JavaFX 27, and on Linux, Windows and Apple silicon it ships as a
**native executable** compiled ahead of time by GraalVM, with JavaFX linked in: no Java start-up,
and the first image on screen in about an eighth of a second. Intel Macs get a Java runtime with a
cache trained ahead of time instead.

The decoders are the reference libraries, built from source for each platform and bundled:
**libjpeg-turbo** for JPEG, **libwebp** for WebP, **libheif** (with libde265 and dav1d) for HEIC
and AVIF, and **libjxl** for JPEG XL. TIFF and PSD are read by the TwelveMonkeys ImageIO plugins,
in pure Java, and PNG, GIF and BMP by JavaFX itself.

## How these docs are organized

- **Getting started**: this page, and [installing and first steps](/docs/getting-started).
- **Using Mirada**: viewing, the folder, properties, editing, files and formats.
- **Make it yours**: the [keyboard and the palette](/docs/keyboard), and [Settings](/docs/settings).
- **Help**: the [FAQ](/docs/faq).
