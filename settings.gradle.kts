rootProject.name = "update-versions-plugin"
include(
    "libs:semantic-version",
)

fun ProjectDescriptor.applyGradleKtsBuildFileNames() {
    children.forEach { subproject ->
        subproject.buildFileName = "${subproject.name}.gradle.kts"
        subproject.applyGradleKtsBuildFileNames()
    }
}

rootProject.applyGradleKtsBuildFileNames()

