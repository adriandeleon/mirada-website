---
title: "On screen in an eighth of a second"
group: "Looking"
order: 1
summary: "A native executable, not a Java start-up: about 125 ms from launch to a small JPEG on Linux, with the first image decoding while the window is still being built."
---

- **Compiled ahead of time.** On Linux, Windows and Apple silicon Mirada ships as a native executable built by GraalVM, with JavaFX linked in. It reaches its own code in about 9 ms, where a Java runtime takes 160.
- **The first image does not wait for the window.** For every format but PNG, GIF and BMP it starts decoding on another thread at the first instruction, while GTK, Cocoa or Win32 comes up, so a large photo is often ready before the window is.
- **Measured, not guessed.** On Linux, median of warm runs: 123 ms to a small JPEG, 160–177 ms to a 24-megapixel one. The figures are re-measured after anything that touches start-up.
- **Less memory too.** The native build uses about a third less than the Java one.
- **Intel Macs** get a Java runtime whose start-up is trained ahead of time across every format, about a quarter of a second.

See [the blog post](/blog/an-eighth-of-a-second) for how it got there.
