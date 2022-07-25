package earth.levi.sage.store

import com.russhwolf.settings.Settings

/**
 * We want to have KeyValueStore to be used inside of the app so it acts as the interface
 * to future-proof the project with 1 specific API. So, if we decide to swap the implementation
 * in the future, we only need to change 1 file (the implementation of the interface) and the rest
 * of the code stays the same since the API is not changed.
 *
 * The 3rd party interface [Settings] is pretty good and one that I am OK with keeping in the longer term. So, we just create a typealias so we can keep the name "KeyValueStore" that we want to use inside of our project.
 *
 * We can always add extension functions to Settings if we want to add features to it (such as supporting Dates).
 */
typealias KeyValueStore = Settings

object KeyValueStoreKeys {
    val DROPBOX_ACCESS_TOKEN = "dropbox_access_token"
}