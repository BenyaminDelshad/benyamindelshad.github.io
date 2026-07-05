package site.model

import kotlinx.serialization.Serializable

/** Top-level site metadata and social links, loaded from `content/site.yaml`. */
@Serializable
data class SiteConfig(
    val name: String,
    val role: String,
    val tagline: String = "",
    val email: String = "",
    val location: String = "",
    /** Absolute base URL of the deployed site, used for canonical/Open Graph tags. */
    val baseUrl: String = "",
    /** Short meta description for SEO and social previews. */
    val description: String = "",
    val links: List<Link> = emptyList(),
)

/** A labelled external link (GitHub, LinkedIn, email, …). */
@Serializable
data class Link(
    val label: String,
    val url: String,
)

/** A single role in the work-history section, loaded from `content/experience.yaml`. */
@Serializable
data class Experience(
    val role: String,
    val company: String,
    val period: String,
    val location: String = "",
    val highlights: List<String> = emptyList(),
    val tech: List<String> = emptyList(),
)

/** A portfolio project, loaded from `content/projects.yaml`. */
@Serializable
data class Project(
    val name: String,
    val description: String,
    val tech: List<String> = emptyList(),
    /** Live/demo URL, if any. */
    val url: String = "",
    /** Source-code URL, if any. */
    val source: String = "",
)
