package earth.levi.sage.type.error

// When an error happens, we want to have an error with a message that is human understandable.
// the raw error, we log internally.
class HostingServiceGenericFetchError: Throwable("Hmm...Looks like a unique error occurred while getting new data. Team has been notified. We recommend you try again later.")