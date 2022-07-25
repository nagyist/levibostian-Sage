package earth.levi.sage.util

import android.util.Log

actual class LoggerImpl: Logger {
    private val tag = "[Sage]"

    override fun debug(message: String) {
        Log.d(tag, message)
    }

    override fun verbose(message: String) {
        Log.v(tag, message)
    }

    override fun error(message: String) {
        Log.e(tag, message)
    }

    override fun error(error: Throwable) {
        error.message?.let { Log.e(tag, it) }
    }
}