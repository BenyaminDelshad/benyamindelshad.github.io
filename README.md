# Personal Website

My personal site — a small **static-site generator written in Kotlin**. Content lives
in plain Markdown/YAML files; a Kotlin program renders them into a fast, dependency-free
static site that ships to GitHub Pages via GitHub Actions.

**Live:** https://delshad.dev

## How it works

```
content/            # what the site says (edit these)
  site.yaml         #   name, role, tagline, social links
  about.md          #   bio prose (Markdown)
  experience.yaml   #   work history
  projects.yaml     #   portfolio projects
static/             # assets copied verbatim (CSS, favicon, resume.pdf)
src/main/kotlin/site/
  model/            # typed content models (@Serializable data classes)
  content/          # loaders: kaml for YAML, flexmark for Markdown
  render/           # kotlinx.html type-safe HTML templates
  Main.kt           # pipeline: load content -> render -> write build/site
```

The generator reads `content/`, renders each page with the
[kotlinx.html](https://github.com/Kotlin/kotlinx.html) type-safe DSL (so the markup is
compiler-checked Kotlin, not string templates), writes HTML to `build/site/`, and copies
`static/` assets alongside it.

## Develop locally

```bash
./gradlew run                                   # generate the site into build/site/
python3 -m http.server 4173 --directory build/site   # then open http://localhost:4173
```

## Deploy

Every push to `main` triggers [`.github/workflows/deploy.yml`](.github/workflows/deploy.yml),
which builds the site and publishes `build/site/` to GitHub Pages. Routine updates are
just edits to the `content/` files.

## Tech

Kotlin · Gradle (Kotlin DSL) · kotlinx.html · flexmark-java · kaml · GitHub Actions · GitHub Pages
