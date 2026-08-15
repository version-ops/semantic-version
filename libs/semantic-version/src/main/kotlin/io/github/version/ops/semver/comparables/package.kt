package io.github.version.ops.semver.comparables

import io.github.version.ops.kandy.Collections.until
import io.github.version.ops.kandy.Maybe
import io.github.version.ops.kandy.Maybe.Empty
import io.github.version.ops.kandy.Maybe.Some
import io.github.version.ops.kandy.isNumeric
import io.github.version.ops.semver.comparables.PrereleaseComparator.releases


fun <T> nullCompareTo(t1: T?, t2: T?): Maybe<Int> =
    when {
        t1 == null && t2 == null -> Some(0)
        t1 == null -> Some(-1)
        t2 == null -> Some(1)
        else -> Empty
    }

/**
 * were searching for less or greater than, so
 * we continue searching when they are equal.
 */
fun Maybe<Int>.toInt() =
    when (this) {
        is Some -> value
        Empty -> 0
    }

fun Int.toCompareMaybe() = when {
    this < 0 -> Some(-1)
    this > 0 -> Some(1)
    else -> Empty
}

typealias Rule = (String?, String?) -> Maybe<Int>

internal fun compareRelease(pr1: String?, pr2: String?): Maybe<Int> {
    fun isRelease(prerelease: String?): Boolean =
        prerelease?.let { releases.contains(it) } ?: true

    return when {
        pr1 == pr2 -> Some(0)  // equal, including nulls
        else -> {
            val r1 = isRelease(pr1)
            val r2 = isRelease(pr2)
            when {
                r1 && !r2 -> Some(-1)
                !r1 && r2 -> Some(1)
                else -> Empty // inconclusive - both releases or prerelease
            }
        }
    }
}

/**
 * despite acceptiing nullables, nulls are undefined as it depends on context
 */
internal fun dottedCompare(v1: String?, v2: String?): Maybe<Int> {
    val bits1 = v1?.split(".") ?: throw IllegalArgumentException("null not expected")
    val bits2 = v2?.split(".") ?: throw IllegalArgumentException("null not expected")

    val result = bits1.zip(bits2).until { (b1, b2) ->
        when {
            b1.isNumeric && b2.isNumeric -> b1.toInt().compareTo(b2.toInt())
            else -> b1.compareTo(b2)
        }.toCompareMaybe()
    }

    return when (result) {
        is Some -> result
        else -> bits1.size.compareTo(bits2.size).toCompareMaybe()
    }
}
