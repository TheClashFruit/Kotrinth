# Kotrinth

![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmvn.theclashfruit.me%2Fsnapshots%2Fme%2Ftheclashfruit%2Fkotrinth%2Fmaven-metadata.xml&label=Snapshots%20Version)
![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmvn.theclashfruit.me%2Freleases%2Fme%2Ftheclashfruit%2Fkotrinth%2Fmaven-metadata.xml&label=Releases%20Version)

A Modrinth API wrapper for Kotlin.

## Getting Started

### Usage

#### Import Kotrinth

Add the repository and dependency to your `build.gradle.kts`:

<details>
  <summary>Beta, Alpha (Snapshots Repo)</summary>

  ```kotlin
  maven {
      name = "TheClashFruit's Maven Snapshots"
      url = uri("https://mvn.theclashfruit.me/snapshots")
  }
  ```
</details>

<details>
  <summary>Stable (Releases Repo)</summary>

  ```kotlin
  maven {
      name = "TheClashFruit's Maven Releases"
      url = uri("https://mvn.theclashfruit.me/releases")
  }
  ```
</details>

```kotlin
implementation("me.theclashfruit:kotrinth:$version")
```

#### Example

```kotlin
package com.example

import me.theclashfruit.kotrinth.Kotrinth

suspend fun main() {
  // Create a new Kotrinth instance.
  val kotrinth = Kotrinth(
    appName = "Example",
    appVersion = "1.0.0",
    appContact = "admin@example.com"
  )
  
  // Get a user by their username.
  val user = kotrinth.v2.user("TheClashFruit")
  
  // Print the user's id.
  println(user.id)
}
```

---

### Cloning

This repository doesn't use any special stuff, so we can clone it in to old-fashioned way of `git clone https://github.com/TheClashFruit/Kotrinth.git`.

### Building

Building is also straight forward, open the project in your desired code editor, IDE or just a terminal then just run `./gradlew build` in a terminal.

## Contributing

You can read the [CONTRIBUTING.md](https://github.com/TheClashFruit/Kotrinth/blob/main/CONTRIBUTING.md) file for details on the project's code of conduct, and the process for submitting pull requests to this project, have fun!

## License

This project is licensed under MIT, if you want to learn more check the [LICENSE](https://github.com/TheClashFruit/Kotrinth/blob/main/LICENSE) file.