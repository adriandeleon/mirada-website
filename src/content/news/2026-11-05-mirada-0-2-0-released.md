---
title: Mirada 0.2.0 released
description: "A menu bar on the Mac, Go to Image and Open Recent, Copy and drag out, Undo Move to Trash on Linux, a thumbnail strip at the side or by name, a trackpad that pans and swipes, and Finder's double-click."
date: 2026-11-05
version: 0.2.0
---

Mirada 0.2.0 makes it at home on each desktop, and adds the ways in and out that a viewer you use all
day needs.

## New

- **A menu bar on the Mac**: File, Image, View, Go and Help, with Preview's ⌘ keys and ticks on what
  is on. About and Settings… (<kbd>⌘,</kbd>) are in the Mirada menu, and Help's search finds any
  command by name.
- **Go to Image** (<kbd>Ctrl+P</kbd>, <kbd>⌘P</kbd>): type part of a file name to show it.
- **Open Recent** (<kbd>Ctrl+Shift+O</kbd>, <kbd>⇧⌘O</kbd>): the last ten folders.
- **Copy** (<kbd>Ctrl+C</kbd>): the picture as it stands on screen, turned as shown, and its file.
- **Drag a photo out** into a chat, a mail or a folder, from the image or a thumbnail.
- **Show in Finder, File Explorer or Folder**, with the image selected.
- **Undo Move to Trash** (<kbd>Ctrl+Z</kbd>) on Linux, up to twenty back.
- **The thumbnail strip can stand down the side**, show **names under the pictures**, or **names
  instead of them**.
- **A checkerboard** behind transparent images, icons in the menus, and screen-reader names for the
  icon buttons.
- **A saved edit keeps the colour profile**, in JPEG and PNG.

## Changed

- **A trackpad pans and swipes** on a Mac: pinch to zoom, two fingers to move, a swipe to step. A
  wheel still zooms.
- **The Mac's keys**: <kbd>Return</kbd> renames, <kbd>⌘I</kbd> shows the properties, <kbd>⌘R</kbd>
  and <kbd>⌘L</kbd> rotate, <kbd>⌃⌘F</kbd> is full screen, <kbd>⌘⌫</kbd> moves to the Trash.
- **The system's accent colour** on macOS and Windows.
- **Settings live where each platform keeps them**, and the window opens where it was.

## Fixed

- **macOS: an image opened from Finder shows.** A double-click, Open With or a file dropped on the
  Dock icon opened an empty window in 0.1.0. On an Intel Mac, it works once Mirada is running.
- **Escape no longer closes the window on macOS and Windows.** Linux keeps GNOME's habit.

## 0.2.1 and 0.2.2

Two fixes followed the same day, both for Linux. **0.2.1** makes the deb upgrade and uninstall: the
removal script of 0.1.0 and 0.2.0 did not parse, which failed every upgrade and removal (installing
0.2.1 over either works; dpkg warns about the old script once). **0.2.2** starts again on Debian 13
and Ubuntu 24.04 and 22.04 for x64, as 0.2.0 and 0.2.1 had been built against a newer glibc than they
have.

Downloads are on the [release page](https://github.com/adriandeleon/Mirada/releases/tag/v0.2.2).
