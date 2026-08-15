package io.github.version.ops.semver.comparables

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class BuildComparatorTest : FunSpec({
    val c = BuildComparator

    test("nulls work fine") {
        c.compare(null, null) shouldBe EQUAL
        c.compare(null, "build.1") shouldBe GREATER
        c.compare("build.2", null) shouldBe LESS
    }

    test("dotted build numbers work") {
        c.compare("1", "10") shouldBe LESS
        c.compare("alpha.10", "alpha.1") shouldBe GREATER
    }

})
