package site.render

import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import site.model.Education
import site.model.Experience
import site.model.Project
import site.model.SiteConfig
import java.time.Year

/**
 * Renders the site's pages using the kotlinx.html type-safe DSL. All markup lives here
 * as Kotlin, so the "templates" are ordinary, refactorable, compiler-checked code.
 */
class SiteRenderer(
    private val config: SiteConfig,
    private val aboutHtml: String,
    private val experience: List<Experience>,
    private val projects: List<Project>,
    private val education: List<Education>,
) {

    fun indexPage(): String = document(title = "${config.name} — ${config.role}") {
        siteHeader()
        main {
            heroSection()
            aboutSection()
            experienceSection()
            projectsSection()
            educationSection()
        }
        siteFooter()
    }

    fun notFoundPage(): String = document(title = "Page not found — ${config.name}") {
        main {
            section("section section--center") {
                h1 { +"404" }
                p { +"That page doesn't exist." }
                p { a(href = "/") { +"← Back home" } }
            }
        }
    }

    // ---- Document shell ----------------------------------------------------

    private fun document(title: String, body: BODY.() -> Unit): String {
        val builder = StringBuilder("<!DOCTYPE html>\n")
        builder.appendHTML().html {
            lang = "en"
            head { headContent(title) }
            body { body() }
        }
        return builder.toString()
    }

    private fun HEAD.headContent(pageTitle: String) {
        meta { charset = "utf-8" }
        meta { name = "viewport"; content = "width=device-width, initial-scale=1" }
        title { +pageTitle }
        if (config.description.isNotBlank()) {
            meta { name = "description"; content = config.description }
        }
        // Open Graph / social preview.
        ogMeta("og:title", pageTitle)
        ogMeta("og:type", "website")
        if (config.description.isNotBlank()) ogMeta("og:description", config.description)
        if (config.baseUrl.isNotBlank()) ogMeta("og:url", config.baseUrl)
        if (config.baseUrl.isNotBlank()) {
            link(rel = "canonical", href = config.baseUrl)
        }
        link(rel = "icon", href = "favicon.svg", type = "image/svg+xml")
        link(rel = "stylesheet", href = "styles.css")
    }

    private fun HEAD.ogMeta(property: String, value: String) {
        meta {
            attributes["property"] = property
            content = value
        }
    }

    // ---- Sections ----------------------------------------------------------

    private fun BODY.siteHeader() {
        header("site-header") {
            div("container site-header__inner") {
                a(href = "/", classes = "site-header__name") { +config.name }
                nav("site-nav") {
                    a(href = "#about") { +"About" }
                    a(href = "#experience") { +"Experience" }
                    a(href = "#projects") { +"Projects" }
                    a(href = "#education") { +"Education" }
                }
            }
        }
    }

    private fun MAIN.heroSection() {
        section("section hero") {
            div("container") {
                p("hero__eyebrow") { +config.role }
                h1("hero__name") { +config.name }
                if (config.tagline.isNotBlank()) {
                    p("hero__tagline") { +config.tagline }
                }
                if (config.location.isNotBlank()) {
                    p("hero__location") { +config.location }
                }
                if (config.links.isNotEmpty()) {
                    div("hero__links") {
                        config.links.forEach { link ->
                            a(href = link.url, classes = "button") {
                                if (link.url.startsWith("http")) {
                                    rel = "noopener noreferrer"
                                    target = "_blank"
                                }
                                +link.label
                            }
                        }
                    }
                }
            }
        }
    }

    private fun MAIN.aboutSection() {
        section("section") {
            id = "about"
            div("container") {
                h2("section__title") { +"About" }
                div("prose") {
                    unsafe { +aboutHtml }
                }
            }
        }
    }

    private fun MAIN.experienceSection() {
        if (experience.isEmpty()) return
        section("section section--alt") {
            id = "experience"
            div("container") {
                h2("section__title") { +"Experience" }
                div("timeline") {
                    experience.forEach { job ->
                        article("card") {
                            div("card__head") {
                                h3("card__title") { +"${job.role} · ${job.company}" }
                                span("card__meta") {
                                    +job.period
                                    if (job.location.isNotBlank()) +" · ${job.location}"
                                }
                            }
                            if (job.highlights.isNotEmpty()) {
                                ul("card__list") {
                                    job.highlights.forEach { li { +it } }
                                }
                            }
                            if (job.tech.isNotEmpty()) {
                                tagRow(job.tech)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun MAIN.projectsSection() {
        if (projects.isEmpty()) return
        section("section") {
            id = "projects"
            div("container") {
                h2("section__title") { +"Projects" }
                div("grid") {
                    projects.forEach { project ->
                        article("card card--project") {
                            h3("card__title") { +project.name }
                            p("card__desc") { +project.description }
                            if (project.tech.isNotEmpty()) tagRow(project.tech)
                            if (project.url.isNotBlank() || project.source.isNotBlank()) {
                                div("card__links") {
                                    if (project.url.isNotBlank()) {
                                        a(href = project.url, classes = "link") {
                                            rel = "noopener noreferrer"; target = "_blank"
                                            +"Live ↗"
                                        }
                                    }
                                    if (project.source.isNotBlank()) {
                                        a(href = project.source, classes = "link") {
                                            rel = "noopener noreferrer"; target = "_blank"
                                            +"Source ↗"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun MAIN.educationSection() {
        if (education.isEmpty()) return
        section("section section--alt") {
            id = "education"
            div("container") {
                h2("section__title") { +"Education & Achievements" }
                div("timeline") {
                    education.forEach { entry ->
                        article("card") {
                            div("card__head") {
                                h3("card__title") { +"${entry.degree} · ${entry.institution}" }
                                span("card__meta") {
                                    +entry.period
                                    if (entry.location.isNotBlank()) +" · ${entry.location}"
                                }
                            }
                            if (entry.highlights.isNotEmpty()) {
                                ul("card__list") {
                                    entry.highlights.forEach { li { +it } }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun FlowContent.tagRow(tags: List<String>) {
        ul("tags") {
            tags.forEach { li("tag") { +it } }
        }
    }

    private fun BODY.siteFooter() {
        footer("site-footer") {
            div("container site-footer__inner") {
                p {
                    +"© ${Year.now().value} ${config.name}"
                    if (config.email.isNotBlank()) {
                        +" · "
                        a(href = "mailto:${config.email}", classes = "link") { +config.email }
                    }
                }
                p("site-footer__built") {
                    +"Built with Kotlin — "
                    val repo = config.links.firstOrNull { it.label.equals("GitHub", ignoreCase = true) }
                    if (repo != null) {
                        a(href = repo.url, classes = "link") {
                            rel = "noopener noreferrer"; target = "_blank"
                            +"source on GitHub ↗"
                        }
                    } else {
                        +"source on GitHub"
                    }
                }
            }
        }
    }
}
