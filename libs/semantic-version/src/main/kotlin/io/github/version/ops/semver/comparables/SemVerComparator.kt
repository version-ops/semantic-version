package io.github.version.ops.semver.comparables

import io.github.version.ops.kandy.Collections.until
import io.github.version.ops.kandy.Maybe
import io.github.version.ops.semver.SemanticVersion


object SemVerComparator : Comparator<SemanticVersion> {

    override fun compare(
        v1: SemanticVersion,
        v2: SemanticVersion
    ): Int =
        rules.until { rule ->
            val result = rule(v1, v2)
            result
        }.toInt()

    val rules: List<(SemanticVersion, SemanticVersion) -> Maybe<Int>> = listOf(
        this::coreComparator,
        this::preReleaseComparator,
        this::buildComparator
    )

    fun coreComparator(v1: SemanticVersion, v2: SemanticVersion): Maybe<Int> {
        val coreComparator = compareBy(
            SemanticVersion::major,
            SemanticVersion::minor,
            SemanticVersion::patch,
        )
        return coreComparator.compare(v1, v2).toCompareMaybe()
    }

    fun preReleaseComparator(v1: SemanticVersion, v2: SemanticVersion): Maybe<Int> {
        return PrereleaseComparator.compare(v1.prerelease, v2.prerelease)
            .toCompareMaybe()
    }

    fun buildComparator(v1: SemanticVersion, v2: SemanticVersion): Maybe<Int> {
        return BuildComparator.compare(v1.buildMetaData, v2.buildMetaData)
            .toCompareMaybe()
    }

}