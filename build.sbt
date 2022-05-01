lazy val baseName       = "Lucre2D"
lazy val baseNameL      = baseName.toLowerCase
lazy val gitProject     = baseName
lazy val gitRepoHost    = "codeberg.org"
lazy val gitRepoUser    = "sciss"

lazy val projectVersion = "0.1.0-SNAPSHOT"
lazy val mimaVersion    = "0.1.0"

// ---- dependencies ----

lazy val deps = new {
  val main = new {
    val lucre     = "4.6.0"
  }
  val test = new {
    val scalaTest = "3.2.10"
    val scallop   = "4.0.2"
  }
}

lazy val commonJvmSettings = Seq(
  crossScalaVersions  := Seq("3.1.0", "2.13.7", "2.12.15"),
  run / fork          := true
)

// sonatype plugin requires that these are in global
ThisBuild / version       := projectVersion
ThisBuild / organization  := "de.sciss"
ThisBuild / versionScheme := Some("pvp")

lazy val root = crossProject(JVMPlatform, JSPlatform).in(file("."))
  .jvmSettings(commonJvmSettings)
  .settings(
    name                 := baseName,
    scalaVersion         := "2.13.7",
    description          := "Expression based 2D graphics canvas for Lucre",
    homepage             := Some(url(s"https://$gitRepoHost/$gitRepoUser/$gitProject")),
    licenses             := Seq("AGPL v3+" -> url("http://www.gnu.org/licenses/agpl-3.0.txt")),
    libraryDependencies ++= Seq(
      "de.sciss"      %%% "lucre-expr"   % deps.main.lucre,
      "org.scalatest" %%% "scalatest"    % deps.test.scalaTest % Test,
    ),
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-encoding", "utf8", "-Xlint"),
    Compile / compile / scalacOptions ++= (if (/* !isDotty.value && */ scala.util.Properties.isJavaAtLeast("9")) Seq("-release", "8") else Nil), // JDK >8 breaks API; skip scala-doc
    // ---- compatibility ----
    mimaPreviousArtifacts := Set("de.sciss" %% baseNameL % mimaVersion),
    updateOptions := updateOptions.value.withLatestSnapshots(false)
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "org.rogach" %% "scallop" % deps.test.scallop % Test,    // command line option parsing
    ),
    assembly / assemblyJarName := s"$baseName.jar",
  )
  .jsSettings(
    libraryDependencies ++= Seq(
    )
  )
  .settings(publishSettings)

lazy val examples = project.in(file("examples"))
  .settings(commonJvmSettings)
  .settings(assemblySettings)
  .dependsOn(root.jvm)
  .settings(
    name                 := "examples",
    scalaVersion         := "2.13.7",
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-encoding", "utf8", "-Xlint"),
    Compile / compile / scalacOptions ++= (if (/* !isDotty.value && */ scala.util.Properties.isJavaAtLeast("9")) Seq("-release", "8") else Nil), // JDK >8 breaks API; skip scala-doc
    assembly / assemblyJarName := s"$baseName.jar",
    libraryDependencies ++= Seq(
      "org.rogach" %%% "scallop" % deps.test.scallop,    // command line option parsing
    ),
  )

lazy val assemblySettings = Seq(
  // ---- assembly ----
  assembly / test            := {},
  assembly / target          := baseDirectory.value,
  ThisBuild / assemblyMergeStrategy := {
    case "logback.xml" => MergeStrategy.last
    case PathList("org", "xmlpull", _ @ _*)              => MergeStrategy.first
    case PathList("org", "w3c", "dom", "events", _ @ _*) => MergeStrategy.first // Apache Batik
    case p @ PathList(ps @ _*) if ps.last endsWith "module-info.class" =>
      println(s"DISCARD: $p")
      MergeStrategy.discard // Jackson, Pi4J
    case x =>
      val old = (ThisBuild / assemblyMergeStrategy).value
      old(x)
  },
  //  assembly / fullClasspath := (Test / fullClasspath).value // https://github.com/sbt/sbt-assembly/issues/27
)

// ---- publishing ----
lazy val publishSettings = Seq(
  publishMavenStyle := true,
  Test / publishArtifact := false,
  pomIncludeRepository := { _ => false },
  developers := List(
    Developer(
      id    = "sciss",
      name  = "Hanns Holger Rutz",
      email = "contact@sciss.de",
      url   = url("https://www.sciss.de")
    )
  ),
  scmInfo := {
    val h = gitRepoHost
    val a = s"$gitRepoUser/$gitProject"
    Some(ScmInfo(url(s"https://$h/$a"), s"scm:git@$h:$a.git"))
  },
)

