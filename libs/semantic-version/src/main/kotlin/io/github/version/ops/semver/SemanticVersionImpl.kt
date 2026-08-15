package io.github.version.ops.semver

import io.github.oshai.kotlinlogging.KotlinLogging

val logger = KotlinLogging.logger {}


@Suppress("RegExpRepeatedSpace", "RegExpUnexpectedAnchor")
private val regex = """
    ^
    (?<v>v)?          # optional v (not legal)
    (?<major>0|[1-9]\d*)\.(?<minor>0|[1-9]\d*)\.(?<patch>0|[1-9]\d*)
    (?:-(?<prerelease>(?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\.(?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?
    (?:\+(?<buildMetaData>[0-9a-zA-Z-]+(?:\.[0-9a-zA-Z-]+)*))?
    $
""".toRegex(RegexOption.COMMENTS)


internal object SemanticVersionImpl {

    fun create(version: String): SemanticVersion {
        return when (val r = regex.find(version)) {
            null -> {
                logger.warn { "invalid semantic version [$this], returning origin version" }
                SemanticVersion(
                    version = version,
                    major = 0, minor = 0, patch = 0,
                    prefix = null, prerelease = null, buildMetaData = null,
                    valid = false,
                )
            }

            else -> SemanticVersion(
                version,
                major = r.groups["major"]?.value?.toInt() ?: 0,
                minor = r.groups["minor"]?.value?.toInt() ?: 0,
                patch = r.groups["patch"]?.value?.toInt() ?: 0,
                prefix = r.groups["v"]?.value,
                prerelease = r.groups["prerelease"]?.value,
                buildMetaData = r.groups["buildMetaData"]?.value,
                valid = true,
            )
        }
    }
}

