package earth.levi.sage.android

import android.app.Application
import earth.levi.sage.di.ContextDependents

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        ContextDependents.initialize(this)
    }
}