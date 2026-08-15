package io.github.version.ops.semver

typealias SemVer = SemanticVersion

/**
 * use the toSemanticVersion() extension function to instantiate.
 *
 * Note: from a design perspective, it proved challenging to implement
 * a parser and all the features for the class.  Attempted:
 * - 1-class which rapidly turned into a mess that required tradeoffs.  Mainly,
 *   a single parameter string constructor made forced doing parsing to do a copy.
 * - interface class with backing implementation - this also ran into problems and tradeoffs
 * - a class that calls on various other elements which is this implementation.  The
 *   author is open to ideas.
 */
class SemanticVersion internal constructor(
    val version: String,
    val major: Int,
    val minor: Int,
    val patch: Int,
    val prefix: String? = null,
    val prerelease: String? = null,
    val buildMetaData: String? = null,
    val valid: Boolean = true,
) {
    fun copy(
        version: String = this.version,
        major: Int = this.major,
        minor: Int = this.minor,
        patch: Int = this.patch,
        prefix: String? = this.prefix,
        prerelease: String? = this.prerelease,
        buildMetaData: String? = this.buildMetaData,
    ): SemanticVersion {
        if (!this.valid) {
            throw IllegalArgumentException("Version $version is not valid and copy semantics not available.")
        }

        val newVersion = StringBuilder("${prefix ?: ""}$major.$minor.$patch")
        prerelease?.let { newVersion.append("-$it") }
        buildMetaData?.let { newVersion.append("+$it") }

        return SemanticVersion(
            version = newVersion.toString(),
            major = major,
            minor = minor,
            patch = patch,
            prefix = prefix,
            prerelease = prerelease,
            buildMetaData = buildMetaData,
            valid = true
        )
    }

    override fun toString() = version


    override fun hashCode(): Int = version.removePrefix("v").hashCode()

    override fun equals(other: Any?): Boolean =
        when (other) {
            is SemanticVersion -> comparator.compare(this, other) == 0
            is String -> version.removePrefix("v").equals(other.removePrefix("v"), ignoreCase = true)
            else -> false
        }

    operator fun compareTo(other: SemanticVersion): Int = comparator.compare(this, other)

    companion object {
        fun String.toSemanticVersion() = SemanticVersionImpl.create(this)
        fun String.toSemVer() = toSemanticVersion()
        val String.semver get() = toSemanticVersion()
        val comparator: Comparator<SemanticVersion> =
            compareBy(
                SemanticVersion::major,
                SemanticVersion::minor,
                SemanticVersion::patch,
                SemanticVersion::prerelease,
                SemanticVersion::buildMetaData
            )

        fun generate(): SemanticVersion =
            List(3) { (0..9999).random() }
                .joinToString(".")
                .toSemanticVersion()
    }

}

