name := "GraphSpectra"

version := "0.1"

scalaVersion := "2.12.4"

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  // Last stable release
  "org.scalanlp" %% "breeze" % "0.13.2",

  // Native libraries are not included by default. add this if you want them (as of 0.7)
  // Native libraries greatly improve performance, but increase jar sizes.
  // It also packages various blas implementations, which have licenses that may or may not
  // be compatible with the Apache License. No GPL code, as best I know.
  "org.scalanlp" %% "breeze-natives" % "0.13.2",

  "org.scalanlp" %% "breeze-viz" % "0.13.2",

  "org.graphstream" % "gs-core" % "1.1.1"

  //  "co.theasi" %% "plotly" % "0.2.0"
)


