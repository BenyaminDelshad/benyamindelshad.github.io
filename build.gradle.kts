plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    // Type-safe HTML DSL — the site's markup is pure Kotlin, no template files.
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.11.0")
    // YAML content (experience, projects, site config) via kotlinx.serialization.
    implementation("com.charleskorn.kaml:kaml:0.66.0")
    // Markdown → HTML for prose content (about, etc.).
    implementation("com.vladsch.flexmark:flexmark-all:0.64.8")
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

application {
    // The static-site generator entry point.
    mainClass.set("site.MainKt")
}
