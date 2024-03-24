rootProject.name = "before-after-slider"

if (System.getenv("JITPACK") == null) {
    include(":example")
}
include(":library")