---
title: Bookmarks and notes
description: Bookmark an image or a folder, write a note on it, step through the bookmarks, find any of them, and the Bookmarks and Notes window, where a removed bookmark can be put back.
category: Using Mirada
order: 6.5
---

Mirada keeps bookmarks and notes of its own on images and folders, in `bookmarks.properties` beside
the settings, never in the file.

| Key | Does |
| --- | --- |
| <kbd>B</kbd> | Bookmark the image, or take its bookmark off (<kbd>Shift+B</kbd> its folder) |
| <kbd>N</kbd> | Write a note on the image (<kbd>Shift+N</kbd> on its folder) |
| <kbd>]</kbd> <kbd>[</kbd> | The next and the previous bookmarked image in the folder |
| <kbd>Ctrl+B</kbd> | **Go to Bookmark…**: find any bookmark or note, in any folder, by its name or its note's words |

A bookmarked image wears a bookmark in its corner and in the thumbnail strip, and the
[properties sidebar](/docs/properties) shows its bookmark and note, and its folder's. A rename in
Mirada keeps them.

## The Bookmarks and Notes window

**Bookmarks and Notes** (<kbd>Ctrl+Shift+B</kbd>, <kbd>⇧⌘B</kbd> on a Mac) lists every bookmark and
note, grouped by folder, each image with its thumbnail and its note. Type to filter by name, folder
or note; <kbd>Ctrl+1</kbd>, <kbd>Ctrl+2</kbd> and <kbd>Ctrl+3</kbd> show all, only bookmarks or only
notes. <kbd>Enter</kbd> or a double-click opens the image, <kbd>F2</kbd> edits the note, and
<kbd>Ctrl+B</kbd> takes the bookmark off or puts it on. A mark whose file has gone is listed dimmed,
so it can be removed. The window has a command palette of its own.

**Remove** (<kbd>Ctrl+Delete</kbd>) takes a bookmark off without asking: <kbd>Ctrl+Z</kbd>
(<kbd>⌘Z</kbd> on a Mac, <kbd>C-/</kbd> in Emacs), or **Bookmarks: Undo Remove** in the palette,
puts it back with its note. Removing a note on its own asks first, with a **Remove** button, since a note
cannot be brought back.
