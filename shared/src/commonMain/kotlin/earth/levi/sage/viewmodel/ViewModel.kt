package earth.levi.sage.viewmodel

import kotlinx.coroutines.CoroutineScope

// This project creates ViewModels that are common and shared between the various
// targets (ios, android). This is because coroutines are common between all platforms
// and we can map the results of coroutines to data types that are more specific to each
// platform!
// Help from: https://github.com/touchlab/KaMPKit
expect abstract class ViewModel() {
    val viewModelScope: CoroutineScope
    protected open fun onCleared()
}