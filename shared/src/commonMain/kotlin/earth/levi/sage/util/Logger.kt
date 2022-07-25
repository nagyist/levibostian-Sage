package earth.levi.sage.util

interface Logger {
    fun debug(message: String)
    fun verbose(message: String)
    fun error(message: String)
    fun error(error: Throwable)
}

expect class LoggerImpl: Logger