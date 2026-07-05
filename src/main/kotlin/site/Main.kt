package site

import site.content.ContentLoader
import site.render.SiteRenderer
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.writeText

/**
 * Static-site generator entry point.
 *
 * Pipeline: load `content/` → render pages with kotlinx.html → write `build/site/` →
 * copy `static/` assets. Run with `./gradlew run`; CI runs the same build and publishes
 * `build/site/` to GitHub Pages.
 */
fun main() {
    val projectRoot = Path(System.getProperty("user.dir"))
    val contentDir = projectRoot.resolve("content")
    val staticDir = projectRoot.resolve("static")
    val outputDir = projectRoot.resolve("build/site")

    val loader = ContentLoader(contentDir)
    val renderer = SiteRenderer(
        config = loader.siteConfig(),
        aboutHtml = loader.aboutHtml(),
        experience = loader.experience(),
        projects = loader.projects(),
    )

    prepareOutput(outputDir)
    outputDir.resolve("index.html").writeText(renderer.indexPage())
    outputDir.resolve("404.html").writeText(renderer.notFoundPage())
    copyStaticAssets(staticDir, outputDir)

    println("Generated site → $outputDir")
}

/** Clears any previous output and recreates an empty output directory. */
private fun prepareOutput(outputDir: Path) {
    if (outputDir.exists()) outputDir.toFile().deleteRecursively()
    outputDir.createDirectories()
}

/** Copies everything under `static/` verbatim into the output (CSS, favicon, resume, …). */
private fun copyStaticAssets(staticDir: Path, outputDir: Path) {
    if (!staticDir.exists()) return
    staticDir.toFile().copyRecursively(outputDir.toFile(), overwrite = true)
}
