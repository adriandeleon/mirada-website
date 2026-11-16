---
title: Viewing
description: Zoom and pan, fit and actual size, rotating the view, full screen and the slideshow, and how a mouse wheel and a trackpad differ.
category: Using Mirada
order: 3
---

An image opens **fitted to the window**: the whole picture, as large as the window allows, never
larger than its own size unless you zoom.

![A mountain photo zoomed in so it fills the window, with the full-screen button at the top left and the previous, next and zoom controls at the bottom](/screenshots/docs/viewing-zoom.jpg "Zoomed in: drag to pan, and the zoom control at the bottom right goes back to fitted or actual size.")

## Zoom

| Key | Does |
| --- | --- |
| <kbd>+</kbd> | Zoom in, to the next step, about the centre of the window |
| <kbd>-</kbd> | Zoom out, to the previous step |
| <kbd>0</kbd> | Zoom to fit the window |
| <kbd>1</kbd> | Actual size: one image pixel per screen pixel |
| Double-click | Toggle between fit and actual size |

The **mouse wheel** zooms about the pointer, so the spot under it stays put. Above 200 % the pixels
are drawn as crisp squares rather than smoothed, so you can see what the file really holds.

The floating controls that appear when the pointer moves over the image have zoom buttons too,
and a menu of zoom steps. <kbd>Ctrl+=</kbd>, <kbd>Ctrl+-</kbd> and <kbd>Ctrl+0</kbd> zoom the image
as well, in every keymap.

To make Mirada itself larger or smaller, its menus, header, palette and windows, rather than the
image, use the **interface zoom**: <kbd>Ctrl+Alt+=</kbd>, <kbd>Ctrl+Alt+-</kbd> and
<kbd>Ctrl+Alt+0</kbd> (<kbd>⌘⌥</kbd> on a Mac), or [Settings](/docs/settings#appearance).

## Pan

A zoomed image can be moved about:

- **Drag** it with the mouse.
- **<kbd>Shift+←</kbd> and <kbd>Shift+→</kbd>** move left and right, and **<kbd>↑</kbd> and
  <kbd>↓</kbd>** up and down. (Plain <kbd>←</kbd> and <kbd>→</kbd> step through the folder.)
- On a Mac **trackpad**, two fingers pan.

## Mouse wheel or trackpad

On macOS Mirada can tell a trackpad from a wheel:

- **Pinch** to zoom.
- **Two fingers** move about a zoomed-in image.
- **A sideways swipe** shows the next or previous image when there is nothing to pan.

A mouse wheel always zooms, and <kbd>Ctrl</kbd> or <kbd>⌘</kbd> with any scroll zooms. Windows and
Linux touchpads do not say how many fingers are on them, so there a two-finger scroll zooms, as a
wheel does; a Windows touchpad's pinch arrives as <kbd>Ctrl</kbd>+scroll and zooms too.

## Keep the zoom when stepping

Normally each image opens fitted. **Keep Zoom When Stepping** (<kbd>Z</kbd>) keeps the zoom and the
position instead, so you can compare the same detail across a burst of shots: zoom into the eyes in
the first frame, then arrow through the rest. Press <kbd>Z</kbd> again to go back to fitting. The
choice is remembered.

## Rotate

<kbd>R</kbd> turns the view a quarter clockwise and <kbd>Shift+R</kbd> a quarter counterclockwise
(<kbd>Ctrl+R</kbd> and <kbd>Ctrl+Shift+R</kbd> work too, and <kbd>⌘R</kbd> and <kbd>⌘L</kbd> on a
Mac). **The file is not changed**: this is how you look at it. To save a turned copy, use the
[editor](/docs/editing).

Photos are already turned upright from the orientation the camera wrote into them, mirrored ones
included, so a phone's portrait shot does not lie on its side.

## Full screen

<kbd>F</kbd> or <kbd>F11</kbd> fills the screen with the image, on black; on a Mac, the green window
button and <kbd>⌃⌘F</kbd> do too. <kbd>Esc</kbd> leaves. The pointer hides after the same rest as the
floating controls.

## Slideshow

<kbd>F5</kbd> shows the folder's images one after another, full screen, at the speed set in
[Settings](/docs/settings) (5 seconds by default, from 1 to 60). <kbd>Esc</kbd> stops it.

## Escape

<kbd>Esc</kbd> stops the slideshow or leaves full screen. On Linux, with nothing to leave, it
closes the window, as GNOME's viewer does: look, then go. On macOS and Windows it never closes the
window.

## Transparency and animation

Transparent parts of an image are drawn over a **checkerboard**, so they show as transparent rather
than as the window's colour. Animated GIF, WebP, AVIF, HEIC sequences and JPEG XL **play**: the first
frame shows at once, and the animation starts when the rest of the frames have decoded.
