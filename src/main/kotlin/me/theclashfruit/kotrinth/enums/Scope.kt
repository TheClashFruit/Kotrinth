package me.theclashfruit.kotrinth.enums

enum class Scope(val value: Int) {
    USER_READ_EMAIL(1 shl 0),
    USER_READ(1 shl 1),
    USER_WRITE(1 shl 2),

    NOTIFICATION_READ(1 shl 5),
    NOTIFICATION_WRITE(1 shl 6),

    PAYOUTS_READ(1 shl 7),
    PAYOUTS_WRITE(1 shl 8),
    ANALYTICS(1 shl 9),

    PROJECT_CREATE(1 shl 10),
    PROJECT_READ(1 shl 11),
    PROJECT_WRITE(1 shl 12),
    PROJECT_DELETE(1 shl 13),

    VERSION_CREATE(1 shl 14),
    VERSION_READ(1 shl 15),
    VERSION_WRITE(1 shl 16),
    VERSION_DELETE(1 shl 17),

    REPORT_CREATE(1 shl 18),
    REPORT_READ(1 shl 19),
    REPORT_WRITE(1 shl 20),
    REPORT_DELETE(1 shl 21),

    THREAD_READ(1 shl 22),
    THREAD_WRITE(1 shl 23),

    COLLECTION_CREATE(1 shl 31),
    COLLECTION_READ(1 shl 32),
    COLLECTION_WRITE(1 shl 33),
    COLLECTION_DELETE(1 shl 34),

    ORGANIZATION_CREATE(1 shl 35),
    ORGANIZATION_READ(1 shl 36),
    ORGANIZATION_WRITE(1 shl 37),
    ORGANIZATION_DELETE(1 shl 38),

}