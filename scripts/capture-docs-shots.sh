#!/usr/bin/env bash
#
# Captures the docs pages' screenshots from the real window (Pauta DOCS-01), in Mirada's light and
# dark themes, with no window on the desktop: each one is a run of Mirada's own
# scripts/headless-run.sh, which drives the app on JavaFX's Headless platform with a throwaway
# settings folder. Writes public/screenshots/docs/<name>.jpg and <name>.dark.jpg, which the docs
# Markdown shows as figures (src/lib/rehype-doc-figures.mjs picks the one for the site's theme).
#
#   scripts/capture-docs-shots.sh [/path/to/Mirada] [name…]
#
# The checkout is the argument, else $MIRADA_REPO, else ../Mirada; capture from the release tag's
# build when there is one with the headless driver. The pictures are drawn by
# scripts/gen-sample-photos.py (Python 3 with NumPy and Pillow), so nothing in the shots belongs to
# anyone else. Names after the checkout capture only those shots.
set -euo pipefail
site="$(cd "$(dirname "$0")/.." && pwd)"
repo="${MIRADA_REPO:-$site/../Mirada}"
if [ $# -gt 0 ] && [ -e "$1/.git" ]; then repo="$1"; shift; fi
repo="$(cd "$repo" && pwd)"
only=("$@")

work="$(mktemp -d)"
trap 'rm -rf "$work"' EXIT
photos="$work/Coast trip"
mkdir -p "$photos"
python3 -I "$site/scripts/gen-sample-photos.py" "$photos"
out="$site/public/screenshots/docs"
mkdir -p "$out"

# name | file to open | steps (each one a HeadlessDriver step, separated by ;) | crop x,y,w,h, if any
shots=(
  "window|bay-at-dusk.jpg|key t;wait 1200;move 500 300;wait 300"
  "viewing-zoom|morning-ridges.jpg|key plus;key plus;key plus;wait 600;move 500 400;wait 300"
  "palette|bay-at-dusk.jpg|key t;wait 1200;key C-S-p;type rot"
  "go-to-image|bay-at-dusk.jpg|key t;wait 1200;key C-p"
  "properties|bay-at-dusk.jpg|key i;wait 800"
  "editor|dunes.jpg|key e;wait 1000"
  "settings|green-hills.jpg|size 1000 1000;key C-,;wait 1200|110,60,780,560"
  "rename|night-lake.jpg|key f2;wait 800"
)

built=""
for theme in light dark; do
  for entry in "${shots[@]}"; do
    IFS='|' read -r name file steps crop <<<"$entry"
    if [ ${#only[@]} -gt 0 ] && [[ ! " ${only[*]} " == *" $name "* ]]; then continue; fi
    config="$work/config-$theme-$name"
    mkdir -p "$config"
    printf 'theme=%s\n' "$theme" >"$config/settings.properties"
    IFS=';' read -r -a list <<<"wait 2000;$steps"
    png="$work/$name-$theme.png"
    (cd "$repo" && MIRADA_NO_BUILD="$built" MIRADA_CONFIG_DIR="$config" \
      scripts/headless-run.sh "$photos/$file" -- "${list[@]}" "wait 300" "shot $png" >/dev/null 2>"$work/err" ) \
      || { cat "$work/err" >&2; exit 1; }
    built=1
    suffix=$([ "$theme" = dark ] && echo ".dark" || echo "")
    python3 -I - "$png" "$out/$name$suffix.jpg" "${crop:-}" <<'PY'
import sys
from PIL import Image
image = Image.open(sys.argv[1]).convert("RGB")
if sys.argv[3]:
    x, y, w, h = map(int, sys.argv[3].split(","))
    image = image.crop((x, y, x + w, y + h))
image.save(sys.argv[2], quality=86, optimize=True)
PY
    echo "captured $name ($theme)"
  done
done
