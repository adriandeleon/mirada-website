---
title: FAQ
description: Common questions about Mirada — what it costs, what it collects, whether it changes your photos, RAW files, why it is Java, and why the installers ask before the first launch.
category: Help
order: 11
---

## Is Mirada free?

Yes. It is free and open source under the MIT License, with no paid tier, no account and no ads.

## Does it collect anything?

No. Mirada sends nothing anywhere: no telemetry, no crash reports. Once a day it asks GitHub for
the latest release, to say when a newer one is out, and **Settings › Updates** turns that off. It
reads the files you open and writes only its own settings, and what you ask it to (a rename, a move
to the Trash, an edit saved as a new file).

## Does it change my photos?

Only when you ask. Rotating the view (<kbd>R</kbd>) turns what you see, not the file. The editor
saves a **new** file unless you choose the original in its save dialog, and even then writes beside
it first and moves the result over it. Mirada never writes metadata into a file it is showing.

## Is it a photo library, like Lightroom or Photos?

No. There is no import, no catalogue and no database: Mirada shows the folder as it is on disk, and
anything else that looks at that folder sees the same files. It is for looking, culling and quick
fixes, not for managing a collection.

## Does it open RAW files?

Not as RAW. Mirada opens JPEG, PNG, GIF, BMP, WebP, TIFF, PSD, HEIC, AVIF and JPEG XL; see
[Formats](/docs/formats). A camera's RAW files (`.cr3`, `.nef`, `.arw`, `.dng`…) are not listed.

## Does it apply colour profiles?

Not on screen yet: pixels are shown as they are stored, as most viewers built on the platform's own
toolkit do, so a wide-gamut photo can look a little duller than in a colour-managed app. A saved
edit **keeps** the profile, so nothing is lost. JPEG XL is converted to sRGB.

## Why is it written in Java? Isn't Java slow to start?

It is written in Java 25 with JavaFX, as its siblings Editora, Termina and Torrenta are. On Linux,
Windows and Apple silicon it ships as a **native executable**, compiled ahead of time by GraalVM:
there is no Java start-up, and the first image is on screen in about an eighth of a second. The
decoders that do the heavy work are native C libraries in any case.

## Why does macOS (or Windows) warn on the first launch?

The macOS and Windows builds are not signed yet. On macOS open **System Settings → Privacy &
Security** and choose **Open Anyway**; on Windows choose **More info → Run anyway**. Each release's
`SHA256SUMS` is signed, so you can check what you downloaded: see
[Getting started](/docs/getting-started#checking-a-download).

## A double-click in Finder opens an empty window on my Intel Mac.

A known gap of the Intel build, which is a Java runtime rather than a native executable: macOS
delivers the file before Mirada is listening for it. Once Mirada is running, opening a file from
Finder works. Apple silicon Macs are not affected.

## Why doesn't Escape close the window?

A stray press would lose your place. Escape stops a slideshow or leaves full screen, the same on
every system; <kbd>Ctrl+W</kbd> closes the window and **Quit Mirada** (<kbd>Ctrl+Q</kbd>) ends
Mirada. Mirada 0.3.0 and earlier closed on Escape on Linux, as GNOME's Image Viewer does.

## Where are my settings?

In `settings.properties`, in `~/.mirada` on every system (an older Mirada's folder on macOS or
Windows is moved there once). See [Settings](/docs/settings#the-settings-file).

## How do I report a problem?

**Help › Report a Problem…** writes a report (the version, the runtime, the codecs, the system,
the settings and the end of the Debug Log) with home folders, names, e-mail addresses and tokens
taken out, for you to read and edit, then opens a GitHub issue with it filled in. Nothing is sent by
itself. When Mirada will not start, `mirada --report` does the same and writes
`problem-report.txt` in the folder you typed it in.

**Help › Debug Log** shows what Mirada has logged this session. <kbd>Ctrl+F</kbd> or <kbd>F3</kbd>
(<kbd>⌘F</kbd> on a Mac, <kbd>C-s</kbd> in Emacs) opens a find bar under it that searches as you
type and counts the matches; <kbd>Enter</kbd> and <kbd>Shift+Enter</kbd> step to the next and the
previous, the find key again finds the next, and <kbd>Esc</kbd> puts the keyboard back in the log.

## How far does Mirada follow Pauta?

Mirada is built to [Pauta](https://pauta-project.dev), the adl desktop apps guidelines.
**Help › Pauta Conformance** says how fully it conforms and lists each rule it does not meet yet;
[Pauta conformance](/docs/pauta) is the same list.
