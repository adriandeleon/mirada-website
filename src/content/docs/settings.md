---
title: Settings
description: Every setting in Mirada's Settings window, and the plain settings.properties file behind it, with where it lives on each platform.
category: Make it yours
order: 10
---

**Settings** (<kbd>Ctrl+,</kbd>, <kbd>⌘,</kbd> in the Mirada menu on a Mac) applies each change the
moment you make it; there is no Save button.

![The Settings window: a search field on top, a sidebar of pages (General, Viewer, Thumbnails, Folder, Keys, Backup), and the General page with the theme, what opens at start and the update check](/screenshots/docs/settings.jpg "Settings (Ctrl+,): every change applies at once.")

## Appearance

- **Theme**: follow the system, light, or dark. Following the system tracks it as it changes. On
  macOS and Windows the current thumbnail, the palette's highlighted row and every selection take
  the system's accent colour, adjusted when it is too pale or too deep to read text on; Linux keeps
  the blue.

## Viewer

- **Thumbnails at start**: open the [thumbnail strip](/docs/the-folder#the-thumbnail-strip) when
  Mirada starts. <kbd>T</kbd> toggles it any time.
- **Thumbnails at the side**, **File names under thumbnails**, **File names instead of pictures**:
  the strip's layout.
- **Properties at start**: open the [properties sidebar](/docs/properties) when Mirada starts.
  <kbd>I</kbd> toggles it.
- **Hide controls after**: how long the pointer rests before the floating controls fade, 1 to 10
  seconds (2 by default).
- **Keep zoom when stepping**: keep the zoom and position when moving to another image, to compare
  the same detail. <kbd>Z</kbd> toggles it.

## Folder

- **Sort by**: name, date modified or size.
- **Reverse order**: last first, so newest, largest, or Z to A.
- **Slideshow speed**: seconds each image shows, 1 to 60 (5 by default). <kbd>F5</kbd> starts it.

## Keys

- **Command palette**: the keys that open it. See [Changing the palette's keys](/docs/keyboard#changing-the-palettes-keys).

## The settings file

Settings are a plain `settings.properties` file, meant to be readable and editable by hand:

| Platform | Folder |
| --- | --- |
| Linux | `~/.mirada` |
| macOS | `~/Library/Application Support/Mirada` |
| Windows | `%APPDATA%\Mirada` |

On macOS and Windows a `~/.mirada` from 0.1.0 is moved there on the first start. Every value is
checked as it is read, and one out of range is brought back into it, so a bad edit cannot break the
next start; a file that will not parse at all is simply the defaults.

```properties
theme=system
start.thumbnails=false
start.info=false
controls.hideSeconds=2
sort.by=name
sort.reversed=false
view.keepZoom=false
thumbnails.side=false
thumbnails.names=false
thumbnails.captions=false
slideshow.seconds=5
palette.keys=C-S-p M-x f1
```

Mirada also keeps the window's place and size there, and the last ten folders for Open Recent.
