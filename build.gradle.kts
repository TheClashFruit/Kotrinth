import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import java.io.ByteArrayOutputStream

plugins {
    `maven-publish`

    kotlin("jvm") version "2.0.0"

    id("org.jetbrains.dokka") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

val gitHash: String by lazy {
    val stdout = ByteArrayOutputStream()

    rootProject.exec {
        commandLine("git", "rev-parse", "--short", "HEAD")

        standardOutput = stdout
    }

    stdout.toString().trim()
}

group = "me.theclashfruit"
version = "1.0.0+$gitHash"

repositories {
    mavenCentral()
}
dependencies {
    testImplementation(kotlin("test"))

    implementation("io.ktor:ktor-client-core:3.0.2")
    implementation("io.ktor:ktor-client-cio:3.0.2")
    implementation("io.ktor:ktor-client-android:3.0.2")

    implementation("io.ktor:ktor-client-content-negotiation:3.0.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.2")
}

kotlin {
    jvmToolchain(17)
}
java {
    withSourcesJar()
}
buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.9.20")
    }
}

tasks.test {
    useJUnitPlatform()
}
tasks.build {
    dependsOn(javadocJar)
}
tasks.compileKotlin {
    dependsOn(generateBuildConfig)
}
tasks.withType<DokkaTask>().configureEach {
    mustRunAfter(generateBuildConfig)

    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        moduleName = "Kotrinth"
        footerMessage = "Copyright &copy; 2024 TheClashFruit &bull; Not affiliated with Modrinth and or Rinth, Inc."
    }
}
tasks.named<Jar>("sourcesJar") {
    mustRunAfter(generateBuildConfig)
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")

    from(tasks.dokkaHtml)
}

fun buildConfigClass(configurations: Map<String, String>): String {
    val classBuilder = StringBuilder()

    classBuilder.appendLine("// Generated file. Do not edit!")
    classBuilder.appendLine("")
    classBuilder.appendLine("package me.theclashfruit.kotrinth")
    classBuilder.appendLine("")
    classBuilder.appendLine("/**")
    classBuilder.appendLine(" * Metadata for Kotrinth.")
    classBuilder.appendLine(" */")

    classBuilder.appendLine("object KotrinthMeta {")

    configurations.forEach { (name, value) ->
        classBuilder.appendLine("    /**")
        classBuilder.appendLine("     * $value")
        classBuilder.appendLine("     */")
        classBuilder.appendLine("    const val $name = $value")
        classBuilder.appendLine("")
    }

    classBuilder.appendLine("}")

    return classBuilder.toString()
}

val generateBuildConfig by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/source/buildConfig/me/theclashfruit/kotrinth")

    outputs.dir(outputDir)

    doLast {
        val outputDirPath = outputDir.get().asFile

        val config = mutableMapOf<String, String>()

        config["GIT_HASH"] = "\"$gitHash\""
        config["VERSION"] = "\"$version\""

        val buildConfigFile = outputDirPath.resolve("KotrinthMeta.kt")

        buildConfigFile.parentFile.mkdirs()
        buildConfigFile.writeText(buildConfigClass(config))
    }
}

sourceSets["main"].kotlin.srcDirs(layout.buildDirectory.dir("generated/source/buildConfig"))

publishing {
    repositories {
        maven {
            name = "theClashFruitSnapshots"
            url = uri("https://mvn.theclashfruit.me/snapshots")

            credentials(PasswordCredentials::class)

            authentication {
                create<BasicAuthentication>("basic")
            }
        }

        maven {
            name = "theClashFruitReleases"
            url = uri("https://mvn.theclashfruit.me/releases")

            credentials(PasswordCredentials::class)

            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = "me.theclashfruit"
            artifactId = "kotrinth"

            version = project.version.toString()

            from(components["kotlin"])

            artifact(javadocJar)
            artifact(tasks.getByName("sourcesJar"))

            pom {
                name.set("Kotrinth")
                description.set("A Kotlin wrapper for the Modrinth API.")

                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
            }
        }
    }
}