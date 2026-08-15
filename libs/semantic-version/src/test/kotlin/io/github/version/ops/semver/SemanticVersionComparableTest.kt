//package io.github.versionops.semver
//
//import io.kotest.core.spec.style.FunSpec
//import io.kotest.matchers.shouldBe
//
//class SemanticVersionComparableTest : FunSpec({
//
//    val c = SemanticVersionComparable
//
//    test("test nulls") {
//        c.compare(null, null) shouldBe 0
//        c.compare(null, SemVer("1.2.3"))
//        c.compare(SemVer("1.2.3"), null)
//    }
//
//    test("core versions") {
//        c.compare(SemVer("1.2.3"), SemVer("1.2.3")) shouldBe 0
//        c.compare(SemVer("1.2.3"), SemVer("1.2.4")) shouldBe -1
//        c.compare(SemVer("1.2.4"), SemVer("1.2.3")) shouldBe 1
//
//        c.compare(SemVer("1.2.3"), SemVer("2.2.3")) shouldBe -1
//        c.compare(SemVer("1.2.3"), SemVer("1.3.3")) shouldBe -1
//    }
//
//    test("prerelease versions") {
//        c.compare(SemVer("1.2.3"), SemVer("1.2.3-RC1")) shouldBe 1
//        c.compare(SemVer("1.2.3-RC1"), SemVer("1.2.3-GA")) shouldBe -1
//
//        c.compare(SemVer("1.2.3-RC1"), SemVer("1.2.3-RC1")) shouldBe 0
//        c.compare(SemVer("1.2.3-RC1"), SemVer("1.2.3-RC2")) shouldBe -1
//        c.compare(SemVer("1.2.3-alpha"), SemVer("1.2.3-alpha.1")) shouldBe -1
//
//        c.compare(SemVer("1.2.3"), SemVer("1.2.3-RC1")) shouldBe 1
//    }
//})