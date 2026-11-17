# Draws the six pictures the docs screenshots show (scripts/capture-docs-shots.sh): landscapes made of
# gradients and ridges, 2400x1600 JPEGs, so nothing in a screenshot belongs to anyone else. The same
# seed draws the same pictures every time.
#
#   python3 scripts/gen-sample-photos.py <folder>
#
# Needs NumPy and Pillow.
import sys, numpy as np
from PIL import Image, ImageFilter
out = sys.argv[1]
W, H = 2400, 1600
rng = np.random.default_rng(7)
def lerp(a, b, t): return a + (b - a) * t
def sky(top, mid, bottom, horizon):
    y = np.linspace(0, 1, H)[:, None, None]
    t1 = np.clip(y / horizon, 0, 1)
    c = lerp(np.array(top, float), np.array(mid, float), t1)
    t2 = np.clip((y - horizon * 0.6) / (horizon * 0.4), 0, 1)
    c = lerp(c, np.array(bottom, float), t2 * (y < horizon))
    return np.broadcast_to(c, (H, W, 3)).copy()
def ridge(base, amp, rough, seed):
    r = np.random.default_rng(seed)
    x = np.linspace(0, 1, W)
    y = np.zeros(W)
    for k in range(1, 7):
        y += amp / k**rough * np.sin(2 * np.pi * (k * 1.3 * x + r.random()))
    return (base + y) * H
def layer(img, line, colour, haze=0.0, hazec=(255, 255, 255)):
    rows = np.arange(H)[:, None]
    mask = rows >= line[None, :]
    c = lerp(np.array(colour, float), np.array(hazec, float), haze)
    shade = 1 - 0.12 * np.clip((rows - line[None, :]) / (H * 0.4), 0, 1)
    img[mask] = (c[None, None, :] * shade[..., None])[mask]
def sun(img, cx, cy, r, colour, glow):
    yy, xx = np.mgrid[0:H, 0:W]
    d = np.hypot(xx - cx * W, yy - cy * H)
    g = np.exp(-(d / (r * glow)) ** 2)[..., None]
    img[:] = lerp(img, np.array(colour, float), g * 0.55)
    img[d < r] = colour
def water(img, horizon, tint):
    h = int(horizon * H)
    above = img[:h][::-1]
    n = H - h
    refl = above[:n] if above.shape[0] >= n else np.vstack([above, np.repeat(above[-1:], n - above.shape[0], 0)])
    ripple = (np.sin(np.arange(n)[:, None] * 0.35 + rng.random((1, W)) * 0.3) * 6).astype(int)
    xs = np.clip(np.arange(W)[None, :] + ripple, 0, W - 1)
    refl = refl[np.arange(n)[:, None], xs]
    img[h:] = lerp(refl, np.array(tint, float), 0.35)
def finish(img, name):
    img += rng.normal(0, 3.0, img.shape)
    im = Image.fromarray(np.clip(img, 0, 255).astype(np.uint8)).filter(ImageFilter.GaussianBlur(0.8))
    im.save(f"{out}/{name}", quality=88)
# 1 dusk over a bay
img = sky((28, 36, 78), (212, 110, 96), (250, 190, 120), 0.62)
sun(img, 0.68, 0.58, 46, (255, 226, 170), 5)
layer(img, ridge(0.52, 0.035, 1.4, 1), (70, 52, 84), 0.25, (212, 130, 110))
layer(img, ridge(0.58, 0.025, 1.2, 2), (40, 30, 56))
water(img, 0.62, (30, 30, 60)); finish(img, "bay-at-dusk.jpg")
# 2 morning mountains
img = sky((86, 140, 205), (170, 205, 235), (225, 235, 240), 0.7)
layer(img, ridge(0.38, 0.09, 1.1, 3), (120, 140, 170), 0.45, (215, 228, 240))
layer(img, ridge(0.5, 0.07, 1.2, 4), (78, 100, 120), 0.25, (200, 215, 230))
layer(img, ridge(0.66, 0.05, 1.3, 5), (46, 78, 64))
layer(img, ridge(0.8, 0.03, 1.5, 6), (30, 56, 40)); finish(img, "morning-ridges.jpg")
# 3 noon sea
img = sky((40, 110, 190), (120, 180, 225), (200, 225, 240), 0.55)
sun(img, 0.22, 0.18, 34, (255, 252, 235), 4)
layer(img, ridge(0.5, 0.012, 1.6, 7), (90, 120, 110), 0.35, (180, 210, 230))
water(img, 0.55, (20, 90, 140)); finish(img, "open-sea.jpg")
# 4 desert evening
img = sky((60, 50, 110), (240, 140, 90), (255, 200, 140), 0.6)
sun(img, 0.35, 0.56, 60, (255, 210, 150), 4)
layer(img, ridge(0.6, 0.03, 1.0, 8), (170, 90, 60), 0.2, (240, 150, 100))
layer(img, ridge(0.72, 0.025, 1.4, 9), (130, 62, 44))
layer(img, ridge(0.86, 0.02, 1.5, 10), (90, 40, 34)); finish(img, "dunes.jpg")
# 5 night lake
img = sky((8, 12, 30), (20, 32, 66), (44, 60, 100), 0.6)
stars = rng.random((H, W)) > 0.9993
img[stars] = 235
sun(img, 0.78, 0.2, 30, (240, 240, 225), 3)
layer(img, ridge(0.55, 0.05, 1.2, 11), (14, 22, 34))
water(img, 0.62, (10, 16, 36)); finish(img, "night-lake.jpg")
# 6 green hills
img = sky((100, 165, 220), (175, 210, 235), (230, 238, 240), 0.62)
layer(img, ridge(0.55, 0.04, 1.0, 12), (110, 150, 90), 0.3, (200, 220, 230))
layer(img, ridge(0.66, 0.04, 1.2, 13), (82, 132, 60))
layer(img, ridge(0.8, 0.03, 1.3, 14), (60, 104, 44)); finish(img, "green-hills.jpg")
