package io.github.version.ops.semver.comparables

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PrereleaseComparatorTest : FunSpec({

    val c = PrereleaseComparator

    test("nulls work fine") {
        c.compare(null, null) shouldBe EQUAL
        c.compare(null, "RC.1") shouldBe GREATER
        c.compare("RC.2", null) shouldBe LESS
    }

    test("aphanumberic works") {
        c.compare("alpha", "beta") shouldBe LESS
        c.compare("RC2", "RC1") shouldBe GREATER
        c.compare("RC1", "RC1") shouldBe EQUAL
    }

    test("general available is an exception") {
        c.compare("GA", "alpha") shouldBe GREATER
    }

    test("dotted build numbers work") {
        c.compare("alpha.10", "alpha.2") shouldBe GREATER
    }
})
