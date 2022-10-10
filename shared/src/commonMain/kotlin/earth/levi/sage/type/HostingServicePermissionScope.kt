package earth.levi.sage.type

// Each hosting service will convert these generic scopes into scopes specific for that service.
enum class HostingServicePermissionScope {
    READ_FILES,
    WRITE_FILES,
    SHARE_FILES;
}