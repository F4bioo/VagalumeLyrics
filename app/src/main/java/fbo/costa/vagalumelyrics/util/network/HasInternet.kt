package fbo.costa.vagalumelyrics.util.network


object HasInternet {

    private const val command = "/system/bin/ping -c 1 8.8.8.8"

    // Android won't let you run it on the main thread
    // Use a coroutine scope
    fun execute(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(command)
            return process.waitFor() == 0
        } catch (e: Exception) {
            false
        }
    }
}
