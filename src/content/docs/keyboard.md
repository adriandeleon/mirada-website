---
title: Keyboard and the palette
description: How Mirada's keys are chosen, the command palette and its own keys, Go to Image and Open Recent, the Mac's keys, and changing the keys that open the palette.
category: Make it yours
order: 9
---

Everything Mirada does can be done without the mouse. Every command has one name and at most one
key, declared once in the app, so the palette, the right-click menu, the Mac's menu bar and the
Keyboard Shortcuts window (<kbd>Ctrl+?</kbd>) always agree. The [Keybindings](/keybindings) page
lists them all.

## How the keys are chosen

- **Bare keys for looking.** An image viewer has no text to type into, so stepping, zooming,
  turning and the panels are single keys: arrows, <kbd>+</kbd>, <kbd>R</kbd>, <kbd>I</kbd>,
  <kbd>T</kbd>. Several are GNOME Image Viewer's.
- **<kbd>Ctrl</kbd> for files and windows**: Open, Copy, Close, Settings. <kbd>⌘</kbd> on a Mac.
- **A Mac has its own for a few**, because its keyboard has no forward Delete and its function keys
  are brightness and volume: <kbd>⌘⌫</kbd> moves to the Trash, <kbd>Return</kbd> renames,
  <kbd>⌘I</kbd> shows the properties, <kbd>⌘R</kbd> and <kbd>⌘L</kbd> rotate and <kbd>⌃⌘F</kbd> is
  full screen, as in Preview and Finder. Mirada's own keys work there too, and <kbd>Backspace</kbd>
  alone always steps back: it never deletes anything.

## The command palette

**<kbd>Ctrl+Shift+P</kbd>**, **<kbd>Alt+X</kbd>** (Emacs's <kbd>M-x</kbd>) or **<kbd>F1</kbd>**
opens it; <kbd>⇧⌘P</kbd>, <kbd>⌥X</kbd> or <kbd>F1</kbd> on a Mac. It lists every command by name,
with its key, and the highlighted one's description underneath. Type to narrow the list: letters
match in order, not necessarily together, so `kz` finds Keep Zoom When Stepping. A command that
cannot run right now, such as Next Image on the last image, is greyed, with the reason.

![The command palette over the window with "rot" typed: Rotate Left highlighted with its key Shift+R, then Rotate Right, the editor's rotate commands greyed, and the highlighted command's description underneath](/screenshots/docs/palette.jpg "The command palette, narrowed to "rot": each command with its key, the greyed ones with a reason.")

Inside the palette, its pickers, the Message Log and the Bookmarks and Notes list, the keys that
move follow your keymap (**Settings › Keys › Keymap**):

| Key | Mirada's keymap | Emacs's keymap |
| --- | --- | --- |
| Move the selection a row | <kbd>↑</kbd> <kbd>↓</kbd> | <kbd>C-p</kbd> <kbd>C-n</kbd>, and the arrows |
| Move by a page | <kbd>Page Up</kbd> <kbd>Page Down</kbd> | <kbd>M-v</kbd> <kbd>C-v</kbd>, and Page Up and Down |
| The first and the last row | <kbd>Ctrl+Home</kbd> <kbd>Ctrl+End</kbd> (<kbd>⌘</kbd> on a Mac) | <kbd>M-&lt;</kbd> <kbd>M-&gt;</kbd> |
| Run the selected command | <kbd>Enter</kbd> | <kbd>Enter</kbd> |
| Close | <kbd>Esc</kbd> | <kbd>Esc</kbd> or <kbd>C-g</kbd> |

Closing the palette or a picker puts the focus back where it was.

The text field takes Emacs's editing keys as well: <kbd>Ctrl+A</kbd> and <kbd>Ctrl+E</kbd> to the
ends, <kbd>Ctrl+F</kbd> and <kbd>Ctrl+B</kbd> a character, <kbd>Alt+F</kbd> and <kbd>Alt+B</kbd> a
word, <kbd>Ctrl+K</kbd> to cut to the end, and <kbd>Ctrl+Y</kbd> to paste. It is the same palette as [Editora](https://editora-project.dev)'s
and [Termina](https://termina-project.dev)'s, with the same matching.

### Go to Image and Open Recent

The palette also lists **images and folders**. **Go to Image…** (<kbd>Ctrl+P</kbd>) opens it over the
folder's file names, and **Open Recent…** (<kbd>Ctrl+Shift+O</kbd>) over the last ten folders. See
[The folder](/docs/the-folder).

## Changing the palette's keys

The keys that open the palette are yours to choose: **Settings → Keys → Command palette**. **Add a
key…**, then press it; pressing one that is already there takes it off; **Default** puts the three
back. A key you choose there wins over whatever it did before, so you can give the palette
<kbd>Space</kbd> or <kbd>P</kbd> if you would rather.

They are kept in `settings.properties` as `palette.keys`, in Emacs-style tokens, which you can edit
by hand:

```properties
palette.keys=C-S-p M-x f1
```

`C-` is <kbd>Ctrl</kbd>, `S-` <kbd>Shift</kbd>, `M-` <kbd>Alt</kbd> (<kbd>Option</kbd> on a Mac) and
`Cmd-` <kbd>⌘</kbd>. A key the palette uses to move its selection, such as <kbd>Ctrl+P</kbd>, stays
the palette's while it is open. A setting with no key Mirada can read is the default again, so a
bad edit never leaves the palette without a way in.

## Your own keys

Any command can be given keys of your own in `settings.properties`, as `keys.<command id>`, and they
win over the keymap's; a blank value takes its keys away:

```properties
keys.go.next=n space
keys.tool.properties=C-i
```

Each command's id, such as `go.next` or `editor.save`, is in Mirada's
[keymaps](https://github.com/adriandeleon/Mirada/tree/main/src/main/resources/com/mirada/keymaps).
The properties sidebar and the
thumbnail strip are `tool.properties` and `tool.thumbnails` (<kbd>I</kbd> and <kbd>T</kbd>, and
<kbd>M-1</kbd> and <kbd>M-2</kbd> in the Emacs keymap); keys you gave them under their old names,
`view.properties` and `view.thumbnails`, move to the new ones at the next start.
