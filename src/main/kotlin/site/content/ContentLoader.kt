package site.content

import com.charleskorn.kaml.Yaml
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import kotlinx.serialization.builtins.ListSerializer
import site.model.Experience
import site.model.Project
import site.model.SiteConfig
import java.nio.file.Path
import kotlin.io.path.readText

/**
 * Reads the site's content from a directory of Markdown and YAML files and turns it
 * into typed model objects. Keeping content out of the code means routine updates are
 * just edits to the `content` directory — no code changes, only a rebuild.
 */
class ContentLoader(private val root: Path) {

    private val yaml = Yaml.default
    private val markdownParser = Parser.builder().build()
    private val markdownRenderer = HtmlRenderer.builder().build()

    fun siteConfig(): SiteConfig =
        yaml.decodeFromString(SiteConfig.serializer(), read("site.yaml"))

    fun experience(): List<Experience> =
        yaml.decodeFromString(ListSerializer(Experience.serializer()), read("experience.yaml"))

    fun projects(): List<Project> =
        yaml.decodeFromString(ListSerializer(Project.serializer()), read("projects.yaml"))

    /** Renders `about.md` to an HTML fragment. */
    fun aboutHtml(): String =
        markdownRenderer.render(markdownParser.parse(read("about.md")))

    private fun read(name: String): String = root.resolve(name).readText()
}
