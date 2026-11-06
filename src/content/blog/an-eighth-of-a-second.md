---
title: An eighth of a second
description: "How Mirada went from a quarter of a second to an eighth between launch and the first image: a native executable, a decoder that starts before the window, and the five things that failed only when a file was opened."
date: 2026-11-02
tags: [performance, graalvm, native-image]
---

An image viewer is opened a hundred times a day and kept open for three seconds each time. Whatever
it costs to start is paid every time, and is most of what it costs to use. So the number Mirada is
built around is the time from launch to the first image on screen.

Before the first release that number was **about 280 ms** on Linux for a small JPEG: a jlink
runtime, a launcher script in place of jpackage's, and an ahead-of-time cache trained by stepping
through every format. Good for Java. The 0.1.0 that shipped takes **123 ms**. This is what changed.

## Where the time went

Timestamps at each stage of start-up said that the window and the image were not the problem. Of
the 280 ms, the JVM took about 160 before Mirada's `main` ran at all, and the window was up 60 ms
after that, with the image decoded and painted in the next 30. The work Mirada does was the smaller
part. The runtime starting was the larger.

The AOT cache had already halved that part once. Halving it again meant not having it at all.

## A native executable

GraalVM's native image compiles the application, the JDK and its libraries ahead of time into one
executable: no bytecode to load or verify, no interpreter, no JIT warming up. JavaFX was the
obstacle, since it loads native libraries and reflects into its own classes, and
[StaticFX](https://github.com/HebiRobotics/jfx-static-feature) is what removes it, linking JavaFX's
native code statically into the image.

Measured against the jlink image from the same commit:

| | jlink + AOT cache | Native image |
|---|---|---|
| Launch to first image, small JPEG | 271–275 ms | **126–130 ms** |
| Reaching `main` | about 168 ms | about 13 ms |
| Resident memory, small JPEG shown | 236 MB | **147 MB** |
| On disk, without the codecs | 118 MB | **74 MB** |

Half the time, a third less memory, a third less disk. Most of what is left is JavaFX bringing up
GTK, about 90 ms of it.

## The image starts before the window

That left the toolkit as the floor, and there was no making GTK faster. But nothing says the image
has to wait for it. Mirada's `main` now starts decoding the file it was given on another thread as
its first act, **before** JavaFX is launched, and the window picks the pixels up when it is ready.
For a large photo the decode is often finished before the window is.

There is a catch: JavaFX's own decoder cannot run that early, since it asks for the screens' scale.
So the head start uses Mirada's own decoders (libjpeg-turbo, libwebp, libheif, libjxl and ImageIO),
which is every format but PNG, GIF and BMP, and it produces plain ARGB pixels that become a JavaFX
image later without a copy. If the window asks for the file before the head start has claimed it,
the head start gives up rather than decoding twice.

## Five things that built and then failed

A native image is only as complete as its metadata: the list of what is reached by reflection, JNI,
resources and native calls. Nothing at build time says what is missing; a gap shows when the code
that needs it runs. Each of these built cleanly and failed only when a particular file was opened:

1. **A charset.** metadata-extractor asks for Windows-1252 when it reads a PNG or a HEIF. Without
   every charset in the image, that threw from a static initializer and took the whole image with
   it. It also exposed a real bug on every runtime: the EXIF reader promised never to fail and did
   not catch `LinkageError`.
2. **AWT's libraries.** ImageIO is not linked statically; the image leaves `libawt` and five others
   beside the executable, and ImageIO fails without them. One file is really one file and six
   libraries.
3. **A 16-bit TIFF.** The first metadata run used 8-bit test files, and a 16-bit TIFF failed on a
   JNI field of AWT's raster classes that had never been touched.
4. **A 32-bit BMP.** JavaFX's own BMP decoder throws on one with transparency, on every runtime.
   Mirada now hands ImageIO whatever JavaFX gives up on, and on the native image that path needed
   metadata of its own.
5. **A JPEG-compressed TIFF**, which calls back into the JDK's JPEG reader through JNI.

The answer was to stop trusting the fixtures. A script writes **41 variants** of the formats into
the test resources: JPEG in RGB, grey, CMYK, progressive and 4:4:4; PNG at 8 and 16 bits, grey,
palette and alpha; animated GIF and WebP; TIFF compressed every way, in grey, CMYK and RGBA, with
several pages; PSD at 8 and 16 bits; HEIC and AVIF with alpha and at 10 bits; JPEG XL; an EXIF
orientation and a heavy EXIF block. CI builds the native image on each platform and opens every one
of them in it, and the metadata is recorded by touring the same matrix. A new kind of file that
turns up a problem becomes a new variant.

## macOS and GraalVM 25.4

On a Mac, the first native image (GraalVM 25.0) opened JPEG, PNG, HEIC and JPEG XL and failed WebP,
TIFF and PSD with `Could not initialize class java.awt.Toolkit`: 25.0 emits no AWT libraries on
macOS at all. **25.4** leaves nine of them beside the executable, as Linux leaves its `.so` files, and
the metadata recorded on Linux was enough. Mirada builds with 25.4 on every platform. It has no
Intel macOS build, so Intel Macs keep the jlink image with its trained cache.

## What it cost

A native build needs 8.6 GB of memory at its peak and a runner per platform, and its metadata has to
be kept: re-recorded after anything that reflects, loads a resource or adds a native call. Long
pure-Java decodes are a little slower without the JIT (a 24-megapixel WebP through ImageIO was 28 %
slower), which mattered less once libwebp took WebP over.

In exchange, Mirada's window opens on the photo in about the time it takes to notice that it has.
