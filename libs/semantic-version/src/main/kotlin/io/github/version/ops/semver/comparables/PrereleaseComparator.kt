package io.github.version.ops.semver.comparables

import io.github.version.ops.kandy.Collections.until
import io.github.version.ops.kandy.Maybe
import io.github.version.ops.kandy.Maybe.Companion.toMaybe
import io.github.version.ops.kandy.Maybe.Empty
import io.github.version.ops.kandy.Maybe.Some
import io.github.version.ops.kandy.isNumeric


internal object PrereleaseComparator : Comparator<String?> {

    val releases = listOf("GA")

    val rules: List<Rule> = listOf(
        this::compareRelease,
        ::dottedCompare
    )

    override fun compare(
        pr1: String?,
        pr2: String?,
    ): Int =
        rules.until { rule ->
            rule(pr1, pr2)
        }.toInt()

    /**
     *
     */
    internal fun compareRelease(pr1: String?, pr2: String?): Maybe<Int> {
        fun isRelease(prerelease: String?): Boolean =
            prerelease?.let { releases.contains(it) } ?: true

        return when {
            pr1 == pr2 -> Some(0)  // equal, including nulls
            else -> {
                val r1 = isRelease(pr1)
                val r2 = isRelease(pr2)
                when {
                    r1 && !r2 -> Some(1)
                    !r1 && r2 -> Some(-1)
                    else -> Empty // inconclusive - both releases or prerelease
                }
            }
        }
    }

    internal fun comparePreRelease(pr1: String?, pr2: String?): Maybe<Int> {
        val bits1 = pr1?.split(".") ?: throw IllegalArgumentException("null not expected")
        val bits2 = pr2?.split(".") ?: throw IllegalArgumentException("null not expected")

        val result = bits1.zip(bits2).until { (b1, b2) ->
            when {
                b1.isNumeric && b2.isNumeric -> b1.toInt().compareTo(b2.toInt())
                else -> b1.compareTo(b2)
            }.toCompareMaybe()
        }

        return when (result) {
            is Some -> result
            else -> bits1.size.compareTo(bits2.size).toMaybe()
        }
    }
}
