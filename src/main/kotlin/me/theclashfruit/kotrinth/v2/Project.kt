package me.theclashfruit.kotrinth.v2

import io.ktor.client.*
import me.theclashfruit.kotrinth.Kotrinth
import me.theclashfruit.kotrinth.v2.serializables.ProjectData

class Project(
    private val data: ProjectData,
    private val kotrinth: Kotrinth
) {

}