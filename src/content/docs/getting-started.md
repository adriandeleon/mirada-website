---
title: Getting started
description: Install Mirada on macOS, Windows or Linux, open a photo or a folder, and learn the handful of keys that do most of the work.
category: Getting started
order: 2
---

## Install

Every release has installers for each platform on the
[releases page](https://github.com/adriandeleon/Mirada/releases/latest). Each registers Mirada for
the image types it opens, so it appears under "Open With" for a photo.

| Platform | File | How |
| --- | --- | --- |
| macOS, Apple silicon | `Mirada-<version>-macos-arm64.dmg` | Open it and drag Mirada to Applications |
| macOS, Intel | `Mirada-<version>-macos-x64.dmg` | The same |
| Windows | `Mirada-<version>-windows-x64.msi` | Run it |
| Debian, Ubuntu | `Mirada-<version>-linux-x64.deb` (or `arm64`) | `sudo apt install ./Mirada-*.deb` |
| Fedora, openSUSE | `Mirada-<version>-linux-x64.rpm` (or `arm64`) | `sudo dnf install ./Mirada-*.rpm` |
| Any Linux | `Mirada-<version>-linux-x64.tar.gz` (or `arm64`) | Unpack it anywhere and run `bin/Mirada` |

The Linux packages install on Debian 12 and Ubuntu 22.04 and anything newer.

### The first launch

The macOS and Windows installers are not signed yet, so each asks once:

- **macOS** refuses the first launch of an app that is not notarized. Open **System Settings →
  Privacy & Security**, find Mirada near the bottom, and choose **Open Anyway**.
- **Windows** SmartScreen warns about an unknown publisher: choose **More info → Run anyway**.

### Checking a download

Each release carries `SHA256SUMS`, signed as `SHA256SUMS.asc` with the key published beside it,
`mirada-signing-key.asc`. To check every file you downloaded:

```bash
gpg --import mirada-signing-key.asc
gpg --verify SHA256SUMS.asc
sha256sum -c --ignore-missing SHA256SUMS
```

## Open something

- **Double-click a photo** in your file manager, once Mirada is its viewer, or choose it under
  Open With.
- **Drop a file or a folder** on Mirada's window.
- **Open…** (<kbd>Ctrl+O</kbd>, <kbd>⌘O</kbd> on a Mac) picks an image, or a folder.
- **Open Recent…** (<kbd>Ctrl+Shift+O</kbd>) lists the last ten folders you opened.
- **From a shell**, give it the file or folder. The deb puts `mirada` on your path
  (`mirada ~/Pictures/photo.jpg`); from the rpm the launcher is `/opt/mirada/bin/Mirada`.

Opening a file opens its whole folder, starting at that file. Opening a folder starts at its first
image.

## The keys that do most of the work

| Key | Does |
| --- | --- |
| <kbd>→</kbd> <kbd>Space</kbd> | Next image |
| <kbd>←</kbd> <kbd>Backspace</kbd> | Previous image |
| <kbd>+</kbd> <kbd>-</kbd> | Zoom in and out |
| <kbd>0</kbd> <kbd>1</kbd> | Fit to the window, actual size |
| <kbd>R</kbd> | Rotate right (<kbd>Shift+R</kbd> left) |
| <kbd>I</kbd> | The properties sidebar |
| <kbd>T</kbd> | The thumbnail strip |
| <kbd>F</kbd> | Full screen |
| <kbd>Ctrl+Shift+P</kbd> | The command palette, for everything else |

On a Mac, <kbd>⌘</kbd> takes <kbd>Ctrl</kbd>'s place. The [Keybindings](/keybindings) page has
them all.

## The window

The header bar names the folder and the file. Its buttons go full screen, rotate, open the
properties sidebar and, off macOS, the main menu; on a Mac the menus are in the menu bar. Move the
pointer over the image for the floating previous, next and zoom controls; they fade when the pointer
rests. Right-click the image for a menu of what you can do to it.

Next: [Viewing](/docs/viewing).
