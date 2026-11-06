import type { Chord, Cmd } from "./commands";

// How a chord reads. Mirada's chords differ by platform in more than the modifier's name (Delete
// off macOS is ⌘⌫ on it, F2 is Return), so each command carries both forms. The pages render the
// Linux and Windows form and a small script in Layout swaps in the Mac form on a Mac (data-mac), so
// the static HTML is right for most visitors and for search engines.

const MAC_GLYPH: Record<string, string> = {
  Ctrl: "⌃",
  Option: "⌥",
  Shift: "⇧",
  Cmd: "⌘",
  Left: "←",
  Right: "→",
  Up: "↑",
  Down: "↓",
  Enter: "↩",
  Backspace: "⌫",
  Delete: "⌦",
  Esc: "Esc",
};

export function chordText(chord: Chord, mac = false): string {
  return mac ? chord.map((k) => MAC_GLYPH[k] ?? k).join("") : chord.join("+");
}

/**
 * A command's keys as the page shows them: `pc` is the static text, `mac` what a Mac sees. An
 * empty `mac` with a `pc` means the chord does not exist on macOS, which the swap
 * script shows as such rather than leaving the Ctrl form up.
 */
export function keysOf(c: Pick<Cmd, "pc" | "mac">): { pc: string; mac: string } | null {
  if (!c.pc && !c.mac) return null;
  return { pc: c.pc ? chordText(c.pc) : "—", mac: c.mac ? chordText(c.mac, true) : "" };
}
