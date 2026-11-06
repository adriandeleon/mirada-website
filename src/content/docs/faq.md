---
title: FAQ
description: Common questions about Mirada — what it costs, what it collects, whether it changes your photos, RAW files, why it is Java, and why the installers ask before the first launch.
category: Help
order: 11
---

## Is Mirada free?

Yes. It is free and open source under the MIT License, with no paid tier, no account and no ads.

## Does it collect anything?

No. Mirada sends nothing anywhere: no telemetry, no crash reports, no update check. It reads the
files you open and writes only its own settings file, and what you ask it to (a rename, a move to
the Trash, an edit saved as a new file).

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

## Why does Escape close the window on Linux but not elsewhere?

GNOME's Image Viewer closes on Escape, and Linux keeps that habit: look, then go. On macOS and
Windows no viewer does, and a stray press would lose your place, so there Escape only stops a
slideshow or leaves full screen.

## Where are my settings?

In `settings.properties`: `~/.mirada` on Linux, `~/Library/Application Support/Mirada` on macOS,
`%APPDATA%\Mirada` on Windows. See [Settings](/docs/settings).

## How do I report a problem?

Open an [issue on GitHub](https://github.com/adriandeleon/Mirada/issues), with your platform, the
version (About, <kbd>F12</kbd>, shows it with the codec versions) and, if a file will not open, what
kind of file it is.
