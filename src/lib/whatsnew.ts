// Hand-written highlights for the home page's "What's new" block. Update them with each release,
// from that release's news post. `next` is what is in development and not in a release yet: keep
// it to what has landed in the repository, and empty it as each item ships.

export type NewsItem = { title: string; detail: string };

export const whatsNew: NewsItem[] = [
  { title: "Mirada 0.3.0", detail: "Out for macOS (Apple silicon and Intel), Windows (x64) and Linux (x64 and ARM64). On Windows it now starts on a PC without the Visual C++ runtime installed." },
  { title: "Thumbnails four to fifty times faster", detail: "A camera's JPEG carries a small copy of itself, and the strip now shows that instead of decoding the photo; the thumbnails on screen are made first." },
  { title: "The palette on <kbd>M-x</kbd> too", detail: "<kbd>Alt+X</kbd> (<kbd>Option+X</kbd> on a Mac) opens the command palette besides <kbd>Ctrl+Shift+P</kbd> and <kbd>F1</kbd>, and its keys are yours to choose in Settings → Keys." },
  { title: "A menu bar on the Mac", detail: "Since 0.2.0: File, Image, View, Go and Help, with Preview's ⌘ keys, and Finder's ⌘⌫ to move to the Trash." },
  { title: "Go to Image and Open Recent", detail: "Since 0.2.0: the palette over the folder's file names (<kbd>Ctrl+P</kbd>), or over the last ten folders (<kbd>Ctrl+Shift+O</kbd>)." },
  { title: "Copy, drag out, Show in Folder", detail: "Since 0.2.0: copy the picture as it stands on screen with its file, drag a photo into a chat or a folder, and undo Move to Trash on Linux." },
];

export const next: NewsItem[] = [
  { title: "Bookmarks and notes", detail: "<kbd>B</kbd> bookmarks an image and <kbd>N</kbd> writes a note on it, kept by Mirada and never in the file; <kbd>]</kbd> and <kbd>[</kbd> step between bookmarks, and Go to Bookmark finds any of them by name or note." },
  { title: "A menu for the thumbnail strip", detail: "Right-click it to stand it at the side or along the bottom, or show names instead of pictures. At the side, Up and Down step through the folder." },
  { title: "Open Folder", detail: "<kbd>Ctrl+Shift+F</kbd>, and Open Image and Open Folder buttons in an empty window." },
  { title: "An AppImage for Linux", detail: "One file that runs on any distribution with GTK 3, without installing anything." },
  { title: "More metadata kept in a saved edit", detail: "The photo's XMP (title, keywords, rating, rights), and in a saved TIFF the EXIF and colour profile too." },
];
