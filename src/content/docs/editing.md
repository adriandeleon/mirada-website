---
title: Editing
description: Crop, turn and flip a photo, undo any step, and save the result as a new JPEG, PNG, TIFF or BMP that keeps the photo's EXIF and colour profile.
category: Using Mirada
order: 6
---

**Edit…** (<kbd>E</kbd>) opens the editor over the image: crop, turn and flip, then save the result
as a **new file**. The original is untouched unless you choose it yourself in the save dialog.

![The editor over a photo of dunes: Cancel and Save at the top, the crop box with its handles and thirds, and the crop shape, turn, flip, undo, redo and reset tools along the bottom](/screenshots/docs/editor.jpg "The editor (E): crop, turn and flip, then save as a new file.")

## Crop

Drag the crop box's corners and edges, or drag inside it to move it. Below the image, **Crop**
holds the box to a shape, or lets it go free:

Free · Original (the photo's own shape) · Square · 5:4 · 4:3 · 3:2 · 16:9

<kbd>A</kbd> cycles through them. The arrow keys move the box; with <kbd>Ctrl</kbd>
(<kbd>⌘</kbd>) they move its right or bottom edge instead, to resize it.

## Turn and flip

| Key | Does |
| --- | --- |
| <kbd>R</kbd> | Rotate right |
| <kbd>Shift+R</kbd> | Rotate left |
| <kbd>H</kbd> | Flip horizontally |
| <kbd>V</kbd> | Flip vertically |

Each has a button too. The editor starts from the photo as you see it, already upright.

## Undo

Every step can be undone and redone, Reset included:

| Key | Does |
| --- | --- |
| <kbd>Ctrl+Z</kbd> | Undo |
| <kbd>Ctrl+Shift+Z</kbd> or <kbd>Ctrl+Y</kbd> | Redo |
| <kbd>Backspace</kbd> | Reset to the photo as it was |

A drag is one step, recorded when you let go, and a run of arrow presses is one step too.

## Save

**Save…** (<kbd>Enter</kbd> or <kbd>Ctrl+S</kbd>) asks where. It suggests the name with
"(edited)" added, beside the original, in a format Mirada can write:

- **JPEG, PNG, TIFF or BMP**. A photo in HEIC, AVIF, JPEG XL or WebP is offered as JPEG or PNG.
- The file is written beside its target first and then moved over it, so a failed save never leaves
  half a file.

<kbd>Esc</kbd> or **Cancel** closes the editor. With an edit not yet saved, leaving it (Cancel,
<kbd>Esc</kbd>, closing the window, Quit) asks first: **Save** (the default) saves and leaves,
**Discard** drops the edit, and **Cancel** stays in the editor. Save keeps the editor open when the
save does not go through. In every question before something is lost, <kbd>Enter</kbd> never
removes or discards.

While the editor is open, nothing changes the image under it: stepping, trashing and renaming are
turned away until it closes.

## What a saved edit keeps

- **The EXIF**, into a JPEG or PNG: the camera, lens, exposure, date and location, from a JPEG,
  HEIC, AVIF, WebP, PNG or JPEG XL. It is patched rather than rebuilt: the orientation is set to
  upright and the size to the edit's, the old embedded thumbnail is left out, and everything else,
  maker notes included, survives byte for byte.
- **The colour profile**, into a JPEG or PNG, so a wide-gamut photo (an iPhone's Display P3, a
  camera's Adobe RGB) does not save duller than it was in apps that manage colour. It is kept
  only while it still describes the pixels.

A BMP gets neither, as the format has nowhere to put them.

The editor works on a preview, so every step shows at once; the save applies the same edit to the
full image.
