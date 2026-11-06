---
title: "Always upright"
group: "Every format"
order: 3
summary: "Photos are turned from the orientation the camera wrote, the four mirrored ones included, and a HEIC is never turned twice."
---

- **All eight orientations.** Phones store pixels as the sensor read them and record how they were held; Mirada applies that, mirrored orientations included, and a test checks every one against the tag's definition.
- **Never twice.** HEIF and JPEG XL carry their own rotation in the container, which their decoders apply; an iPhone writes an EXIF orientation as well, and Mirada knows not to apply both.
- **Sizes as you see them.** The properties sidebar and the fit to the window use the upright size.
- **Transparent parts on a checkerboard**, so they show as transparent rather than as the window's colour.

See [Formats](/docs/formats#upright).
