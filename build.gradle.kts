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