---
title: Properties
description: The properties sidebar — the file, the image and the camera — and every metadata tag the photo carries.
category: Using Mirada
order: 5
---

**Image Properties** (<kbd>I</kbd>, <kbd>⌘I</kbd> on a Mac, or the ⓘ button in the header) opens a
sidebar beside the image. It follows you as you step, and [Settings](/docs/settings) can open it
at start.

![The properties sidebar beside a photo: its folder and position, image size, format, file size, created and modified times, and All Metadata collapsed](/screenshots/docs/properties.jpg "The properties sidebar (I).")

## What it shows

**The file**: the folder it is in (with a button that opens it in your file manager) and its
position in the folder, such as "4 of 8".

**The image**: its size in pixels, as you see it (a photo turned upright reports its upright
size), its format, and the file's size.

**The dates**: when the file was created and last modified.

**The camera**, for a photo that says: the camera and the lens, when it was taken, the exposure,
the aperture, the ISO, the focal length, whether the flash fired, and where, when the photo carries
a location.

## All Metadata

Under the camera, **All Metadata** lists every tag the file carries, grouped as the file groups
them: EXIF, the maker's notes, XMP, IPTC, the colour profile, and what the format itself records.
It is read by [metadata-extractor](https://github.com/drewnoakes/metadata-extractor), which reads
JPEG, PNG, WebP, TIFF, PSD, HEIC, AVIF and the rest.

A file whose metadata cannot be read still opens: the sidebar shows what it could find.

## Nothing is written

The sidebar only reads. Mirada never writes metadata into your files; an [edit](/docs/editing) is
saved as a new file, with the photo's metadata carried into it.
