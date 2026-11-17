---
title: Files
description: Rename an image, move it to the Trash and back, copy it, drag it out, and show it in the file manager.
category: Using Mirada
order: 7
---

What Mirada does to a file it does on disk, where every other app sees it. Each of these is in the
right-click menu, the palette and, on a Mac, the menu bar.

## Rename

**Rename…** (<kbd>F2</kbd>; <kbd>Return</kbd> on a Mac, as in Finder) asks for a new name with the
part before the extension selected, so typing replaces just that. A name typed without an extension
keeps the old one, and a name another file already has is refused rather than overwritten. The
image keeps its place in the folder's order, by its new name.

![The Rename dialog over a night photo, the name night-lake.jpg in the field](/screenshots/docs/rename.jpg "Rename (F2): type the new name; the extension is kept.")

## Move to Trash

**Move to Trash** (<kbd>Delete</kbd>; <kbd>⌘⌫</kbd> on a Mac, whose Delete key is Backspace) moves
the image to the desktop's Trash, the Recycle Bin on Windows, and shows the next one. Nothing is
deleted outright: the file can be put back from there.

On Linux Mirada can put it back itself: **Undo Move to Trash** (<kbd>Ctrl+Z</kbd>) returns the last
image moved to the Trash to where it was, and again for the one before, up to twenty. It never
overwrites a file that has taken the name since. Windows' Recycle Bin and the Mac's Trash do not say
where a file went, so there you put it back from the desktop's own.

## Copy

**Copy** (<kbd>Ctrl+C</kbd>, <kbd>⌘C</kbd>) puts two things on the clipboard: **the picture** as it
stands on screen, turned as shown, and **the file**. Paste into an app that takes images (a chat,
a document, an image editor) and you get the picture; paste into a folder and you get the file.

## Drag out

Drag a fitted image, or a thumbnail from the strip, into a chat, a mail or a folder to drop its
file there. With the image zoomed in, dragging pans instead.

## Show in Folder

**Show in Folder** opens the image's folder in your file manager with the image selected. It is
called **Show in Finder** on a Mac and **Show in File Explorer** on Windows.

## Print

**Print…** (File menu, or the command palette) prints the image as it stands on screen, turned as
you turned it, through your desktop's own print dialog: GTK's on Linux, the system's on macOS and
Windows. Choose the printer, the paper and the copies there, or print to a PDF file. The image
fills the page, centred and never cropped, and a wide image starts on a landscape page.

Print has no key of its own, since <kbd>Ctrl+P</kbd> (<kbd>⌘P</kbd>) goes to an image by name. To
give it one, add a line such as `keys.file.print=p` to Mirada's settings file.
