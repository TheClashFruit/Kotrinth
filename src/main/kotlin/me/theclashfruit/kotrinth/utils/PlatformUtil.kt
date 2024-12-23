package me.theclashfruit.kotrinth.utils

object PlatformUtil {
    /**
     * Check if the device the code being run on is android.
     *
     * @return `Boolean`
     */
    fun isRunningOnAndroid(): Boolean {
        return try {
            Class.forName("android.os.Build")

            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }
}