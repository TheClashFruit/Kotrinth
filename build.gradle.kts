import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    `maven-publish`

    kotlin("jvm") version "2.0.0"

    id("org.jetbrains.dokka") version "1.9.20"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
}

group = "me.theclashfruit"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("io.ktor:ktor-client-core:2.3.12")
    implementation("io.ktor:ktor-client-cio:2.3.12")

    implementation("io.ktor:ktor-client-content-negotiation:2.3.12")

    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
}

tasks.test {
    useJUnitPlatform()
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

tasks.build {
    dependsOn(javadocJar)
}

tasks.withType<DokkaTask>().configureEach {
    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        moduleName = "Kotrinth"
        footerMessage = "Copyright &copy; 2024 TheClashFruit &bull; Not affiliated with Modrinth and or Rinth, Inc."
    }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")

    from(tasks.dokkaHtml)
}

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