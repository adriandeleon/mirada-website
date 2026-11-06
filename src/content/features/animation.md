---
title: "Animations play"
group: "Every format"
order: 2
summary: "Animated GIF, WebP, AVIF, HEIC sequences and JPEG XL play, without making the step onto them any slower."
---

- **The still first.** The first frame shows at once, like any other image.
- **Then it moves.** The rest of the frames decode on a low-priority thread once the first is up, and the animation starts when they are in.
- **Whole frames.** libwebp and libjxl compose partial frames into whole pictures, so what plays is what the file means.
- **A sensible limit.** An animation past 64 megapixels, every frame counted, stays a still rather than taking the memory.

See [Formats](/docs/formats#animation).
