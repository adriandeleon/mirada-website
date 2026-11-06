---
title: "A saved edit keeps its metadata"
group: "Changing a photo"
order: 2
summary: "The camera, lens, exposure, date and location, and the colour profile of a wide-gamut photo, carried into the edited copy, with maker notes intact."
---

- **EXIF, patched rather than rebuilt.** The orientation becomes upright and the size the edit's; the old embedded thumbnail is left out; everything else, maker notes included, keeps its bytes and its offsets.
- **From any format into JPEG or PNG**: the EXIF of a JPEG, HEIC, AVIF, WebP, PNG or JPEG XL.
- **The colour profile**, so an iPhone's Display P3 or a camera's Adobe RGB photo does not save duller than it was, in apps that manage colour. Kept only while it still describes the pixels.
- **Nothing written into the original.** Mirada never touches a file's metadata in place.

See [Editing](/docs/editing#what-a-saved-edit-keeps).
