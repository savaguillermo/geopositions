import sbt._

object Resolvers {
  val sonatypeRepo = "Bintray Repository" at "http://dl.bintray.com/typesafe/maven-releases/"
  val clouderaRepo = "Cloudera Repository" at "https://repository.cloudera.com/artifactory/cloudera-repos/"

  val repos = Seq(sonatypeRepo, clouderaRepo)
}