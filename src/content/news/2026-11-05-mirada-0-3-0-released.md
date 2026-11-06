---
title: Mirada 0.3.0 released
description: "A thumbnail strip four to fifty times faster, the command palette on M-x and on keys of your choosing, and Windows without the Visual C++ runtime."
date: 2026-11-05
version: 0.3.0
---

Mirada 0.3.0 makes the thumbnail strip fast and the palette's keys yours, and starts on every
Windows PC.

## Download

From the [release page](https://github.com/adriandeleon/Mirada/releases/tag/v0.3.0):

- **macOS**: the `macos-arm64` DMG for Apple silicon, `macos-x64` for Intel. Open it and drag
  Mirada to Applications.
- **Windows (x64)**: the MSI.
- **Linux (x64 and ARM64)**: the deb (`sudo apt install ./Mirada-*.deb`), the rpm
  (`sudo dnf install ./Mirada-*.rpm`) or the tar.gz.

`SHA256SUMS` is signed, with the key beside it, and so are the Linux packages. The macOS and Windows
builds are not signed yet: see [Getting started](/docs/getting-started#the-first-launch).

## New

- **The command palette opens with <kbd>M-x</kbd> too** (<kbd>Alt+X</kbd>; <kbd>Option+X</kbd> on
  a Mac), besides <kbd>Ctrl+Shift+P</kbd> and <kbd>F1</kbd>.
- **Its keys are yours to choose**: Settings → Keys → Command palette adds a key by pressing it, or
  takes one off by pressing it again; or edit `palette.keys` in `settings.properties`. A key chosen
  there wins over what it did before. See [Keyboard and the palette](/docs/keyboard#changing-the-palettes-keys).

## Changed

- **The thumbnail strip fills four to fifty times faster with photos.** A camera's or phone's JPEG
  carries a small copy of itself, and the strip now shows that, when it is large enough and the
  photo's shape, instead of decoding the photo. Up to four thumbnails are made at once, the ones on
  screen first, and those scrolled past before they were made are no longer made at all.

## Fixed

- **Windows: Mirada starts on a PC without the Visual C++ runtime.** 0.2.x needed it installed and
  otherwise failed at once ("vcruntime140.dll was not found"); the three DLLs now come with Mirada.
- **The thumbnail strip no longer disappears** when it is moved from the side back to the bottom.
- **Copy works on a greyscale PNG.**
