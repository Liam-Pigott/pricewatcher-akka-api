name := "pricewatcher-akka-api"

version := "0.1"

scalaVersion := "2.13.5"

val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.4"
val slickJodaMapperVersion = "2.4.2"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
  "com.github.tototoshi" %% "slick-joda-mapper" % slickJodaMapperVersion,
  "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "joda-time" % "joda-time" % "2.10.10",
  "org.joda" % "joda-convert" % "1.7",
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "mysql" % "mysql-connector-java" % "6.0.6",
  "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion,
  "org.scalactic" %% "scalactic" % "3.2.5",
  "org.scalatest" %% "scalatest" % "3.2.5" % "test",
  "org.scalatestplus" %% "mockito-3-4" % "3.2.5.0" % "test"
)