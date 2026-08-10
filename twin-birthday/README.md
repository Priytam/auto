# 🎂🎂 Twin Birthday ×2

A single-file interactive birthday page for twins. No build step, no dependencies,
no backend — everything runs in the browser.

## What's inside

- **Twin Telepathy** — pass-the-phone quiz: both twins secretly answer the same
  8 questions, then get a sync score and a verdict.
- **Who's Who? guest challenge** — the twins lock in the official answers
  (who's messier, who's mom's favourite…), then guests play one at a time,
  get scored against the truth, and a persistent leaderboard crowns the
  champion. 👑
- **Photo Booth Roulette** — a shuffled polaroid slideshow of your photos with
  random captions. On mobile the picker opens your gallery (Google Photos can
  be chosen as the picker source on Android), there's a take-a-photo-now
  camera button, and photos persist on-device via IndexedDB. Nothing is ever
  uploaded.
- Confetti, editable twin names (saved in `localStorage`), mobile-friendly.

## Run it

Just open `index.html` in a browser, or serve the folder:

```bash
cd twin-birthday
python3 -m http.server 8000
# → http://localhost:8000
```

## Pre-loading photos (optional)

By default photos are added on the page via tap/drag. To bake photos in instead:

1. Drop image files into `photos/`.
2. Create `photos/manifest.json` listing the filenames:

   ```json
   ["us-age-5.jpg", "graduation.jpg", "last-birthday.jpg"]
   ```

3. Serve over http (see above) — the booth picks them up automatically.

> **Privacy note:** this repo is the place photos would live if you commit them,
> so keep the repo private if you do.
