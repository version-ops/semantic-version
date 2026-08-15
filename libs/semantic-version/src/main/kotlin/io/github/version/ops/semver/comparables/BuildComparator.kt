package io.github.version.ops.semver.comparables

import io.github.version.ops.kandy.Collections.until
import io.github.version.ops.kandy.Maybe
import io.github.version.ops.kandy.Maybe.Some


/**
 * according to semver.org any semver with build metadata are to be considered equal weight,
 * but then how do we build a deterministic version semantic.  Therefore, we'll oder them
 * similar to prerelease tags, so we'll order by this example.
 *
 * - 2.3.3-RC1+build.1
 * - 2.3.3-RC1+build.2
 * - 2.3.3-RC1+build.2
 * - 2.3.3-RC1
 */

internal object BuildComparator : Comparator<String> {
    override fun compare(
        b1: String?,
        b2: String?
    ): Int {
        val rules: List<Rule> = listOf(
            ::inverseNull,
            ::dottedCompare
        )

        return rules.until { rule ->
            rule(b1, b2)
        }.toInt()
    }

    fun inverseNull(b1: String?, b2: String?): Maybe<Int> {
        return nullCompareTo(b1, b2).let {
            when (it) {
                is Some -> Some(-it.value)
                else -> it
            }
        }
    }
}