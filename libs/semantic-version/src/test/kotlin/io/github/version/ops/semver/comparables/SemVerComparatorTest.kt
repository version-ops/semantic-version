package io.github.version.ops.semver.comparables

import io.github.version.ops.semver.SemanticVersion.Companion.semver
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

val LESS = -1
val EQUAL = 0
val GREATER = 1

class SemVerComparatorTest : FunSpec({

    val c = SemVerComparator

    test("core semvers work") {
        c.compare("1.2.3".semver, "1.2.3".semver) shouldBe EQUAL
        c.compare("1.2.4".semver, "1.2.3".semver) shouldBe GREATER
        c.compare("1.3.4".semver, "1.4.2".semver) shouldBe LESS
        c.compare("2.0.1".semver, "3.2.3".semver) shouldBe LESS
    }

    test("GA versus RC1") {
        c.compare("1.2.3-RC1".semver, "1.2.3-RC2".semver) shouldBe LESS
        c.compare("1.2.3-GA".semver, "1.2.3-RC2".semver) shouldBe GREATER
    }

    test("builds work") {
        c.compare("1.2.3-RC1+build.223".semver, "1.2.3-RC1+build.33".semver) shouldBe GREATER

    }
})
