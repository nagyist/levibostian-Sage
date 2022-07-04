package earth.levi.sage

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}