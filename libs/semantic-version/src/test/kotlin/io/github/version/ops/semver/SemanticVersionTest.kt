package io.github.version.ops.semver

import io.github.version.ops.semver.SemanticVersion.Companion.toSemVer
import io.github.version.ops.semver.SemanticVersion.Companion.toSemanticVersion
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class SemanticVersionTest : FunSpec({

    test("simple semantic version") {
        val v = "v1.2.3".toSemVer()
        v.major shouldBe 1
        v.minor shouldBe 2
        v.patch shouldBe 3
        v.prefix shouldBe "v"
        v.valid shouldBe true
    }

    test("prerelease works") {
        "1.2.3-RC1".toSemVer().prerelease shouldBe "RC1"
        "1.2.3-rc.1".toSemVer().prerelease shouldBe "rc.1"
    }

    test("build meta data") {
        "1.2.3-RC1+build.12323".toSemVer().apply {
            prerelease shouldBe "RC1"
            buildMetaData shouldBe "build.12323"
        }
    }

    test("hashcode works") {
        val v = "v1.2.3".toSemanticVersion()
        v.hashCode() shouldBe "1.2.3".toSemanticVersion().hashCode()
    }

    test("equals works") {
        val v = "v1.2.3".toSemanticVersion()
        v shouldBe "v1.2.3".toSemanticVersion()
        v shouldBe "1.2.3".toSemanticVersion()

        v shouldBe "v1.2.3"
        v shouldBe "1.2.3"
    }

    test("comareTo operator works") {
        ("v1.2.3".toSemanticVersion() < "v1.2.4".toSemanticVersion()) shouldBe true
    }

    test("simple comparison works") {
        val l = listOf(
            "v1.2.7".toSemanticVersion(),
            "1.2.5".toSemanticVersion()
        )
        (l[0] > l[1]) shouldBe true

        val ordered = l.sortedWith(SemanticVersion.comparator)

        val z = ordered.zipWithNext()
        z.forEach { (a, b) ->
            (a <= b) shouldBe true
        }
    }

    test("generated comparison works") {
        val l = List(1000) { SemanticVersion.generate() }

        val ordered = l.sortedWith(SemanticVersion.comparator)

        val z = ordered.zipWithNext()
        z.forEach { (a, b) ->
            (a <= b) shouldBe true
        }
    }

    // TODO: enable this
//    test("prerelease progression works") {
//        val progression = listOf("1.3.3", "1.3.4-RC1", "1.3.4-RC2", "1.3.4-RC2", "1.3.4").map { it.toSemanticVersion() }
//        val test = progression.shuffled()
//
//        test.sortedWith(SemanticVersionImpl.) shouldBe progression
//    }

    test("progression - test") {
        val progression = listOf(
            "1.0.0-alpha",
            "1.0.0-alpha.1",
            "1.0.0-alpha.beta",
            "1.0.0-beta",
            "1.0.0-beta.2",
            "1.0.0-beta.11",
            "1.0.0-rc.1",
            "1.0.0",
        )

        // TODO: work on the test
    }

    test("valid semantic version") {
        val lines = this::class.java.getResourceAsStream("semantic-version-valid.txt")
            ?.reader()?.readLines() ?: emptyList()

        lines.forEach {
            it.toSemanticVersion().valid shouldBe true
        }
    }

    test("invalid semantic version") {
        val lines = this::class.java.getResourceAsStream("semantic-version-valid.txt")
            ?.reader()?.readLines() ?: emptyList()

        lines.forEach { line ->
            line.toSemanticVersion().let {
                it.valid shouldBe false
                it shouldBe "0.0.0".toSemVer()
            }
        }
    }
})
