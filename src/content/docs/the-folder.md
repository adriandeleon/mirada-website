---
title: The folder
description: Stepping through a folder, the order it is in, going to an image by name, reopening a recent folder, and the thumbnail strip.
category: Using Mirada
order: 4
---

Mirada's document is the folder. Opening one photo lists every image beside it, and the header
says which folder you are in; the properties sidebar says where in it you are ("4 of 8").

## Stepping

| Key | Does |
| --- | --- |
| <kbd>→</kbd> <kbd>Space</kbd> <kbd>Page Down</kbd> | Next image |
| <kbd>←</kbd> <kbd>Backspace</kbd> <kbd>Shift+Space</kbd> <kbd>Page Up</kbd> | Previous image |
| <kbd>Home</kbd> <kbd>End</kbd> | First and last image |

The floating arrows over the image and a sideways swipe on a Mac trackpad step too. Stepping is
instant for the image either side: they are decoded as soon as the current one is up, and the
window keeps the current image on screen until the next is ready, so it never flashes empty. Going
back further is quick as well: the last two dozen images leave a preview behind, shown at once while
the full resolution decodes again.

Hidden files are left out. Everything Mirada can open is listed: see [Formats](/docs/formats).

## Order

By default the folder is in **natural name order**, the way a person counts, so `IMG_9` comes
before `IMG_10`. It can also be sorted by **date** (when each file was last changed) or **size**,
and any of them **reversed**: from the palette (Sort by Name, Sort by Date, Sort by Size, Reverse
Order), the right-click menu, the menus, or [Settings](/docs/settings). The order is remembered.

## Go to Image

**Go to Image…** (<kbd>Ctrl+P</kbd>, <kbd>⌘P</kbd>) opens the palette over the folder's file names,
with the current one under the cursor. Type part of a name, as loosely as you like (`0412` finds
`IMG_20260412_0930.jpg`), and press <kbd>Enter</kbd> to show it. Names that match equally well stay
in the folder's order.

## Open Recent

**Open Recent…** (<kbd>Ctrl+Shift+O</kbd>, <kbd>⇧⌘O</kbd>) is the palette over the last ten folders
you opened, by name, with the path of the highlighted one under the list.

## The thumbnail strip

<kbd>T</kbd> opens the strip: the folder's images along the bottom of the window, the current one
outlined. Click one to show it; drag one out to drop its file somewhere else. It is closed by
default, and [Settings](/docs/settings) can open it at start.

Thumbnails are made in the background, the ones in view first, up to four at a time, and one
scrolled past before it was made is never made. For a photo from a camera or a phone, the strip
shows the small copy of itself the JPEG already carries, when it is large enough and the photo's
shape, rather than decoding the photo, which is four to fifty times faster.

The strip has three variations, each in the palette, the menus and Settings:

- **Thumbnails at the Side**: a column down the left instead of a row along the bottom.
- **Names Under Thumbnails**: each thumbnail with its file name under it.
- **Names Instead of Thumbnails**: the folder as a list of file names, with no pictures at all,
  which suits a folder of thousands.
