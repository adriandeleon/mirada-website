---
title: "Instant stepping"
group: "Looking"
order: 2
summary: "The images either side are decoded before you press the arrow, the window never flashes empty, and going back further shows a preview at once."
---

- **Neighbours first, never before.** Once the current image is on screen, the next and the previous decode on background threads, so stepping onto them takes a few milliseconds for every format.
- **No flash.** The window keeps the current image up until the next one is decoded *and* its orientation is known, so a photo never shows sideways for a frame, nor an empty canvas.
- **Back is quick too.** The last two dozen images leave a small preview behind, shown at once at the full image's size while the full resolution decodes again.
- **Many keys for it.** <kbd>→</kbd>, <kbd>Space</kbd> and <kbd>Page Down</kbd> step forward; <kbd>←</kbd>, <kbd>Backspace</kbd>, <kbd>Shift+Space</kbd> and <kbd>Page Up</kbd> back; <kbd>Home</kbd> and <kbd>End</kbd> jump to the ends.

See [The folder](/docs/the-folder).
