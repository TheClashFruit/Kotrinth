package me.theclashfruit.kotrinth.enums

import io.ktor.http.*

enum class Method(val value: HttpMethod) {
    Get(HttpMethod.Get),
    Post(HttpMethod.Post),
    Put(HttpMethod.Put),
    Delete(HttpMethod.Delete),
    Patch(HttpMethod.Patch),
    Head(HttpMethod.Head),
    Options(HttpMethod.Options);
}