import sbt._

object Dependencies {
  val scalatest = "org.scalatest" %% "scalatest" % "2.1.5" % "test"
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.2"
  val wsClient = "io.backchat.hookup" %% "hookup" % "0.2.3"
  val snakeyaml = "org.yaml" % "snakeyaml" % "1.14"
  val commonsIO= "commons-io" % "commons-io" % "2.4"

  object Akka {
    private val akkaBase = "com.typesafe.akka"
    private val version = "2.3.6"

    val agent = akkaBase %% "akka-agent" % version
    val testKit = akkaBase %% "akka-testkit" % version
    val actor = akkaBase %% "akka-actor" % version
    val cluster = akkaBase %% "akka-cluster" % version
    val contrib = akkaBase %% "akka-contrib" % version
    val slf4j = akkaBase %%  "akka-slf4j"	%	version

    val persistance = akkaBase %% "akka-persistence-experimental" % version
    val cassandraPersistence = "com.github.krasserm" %% "akka-persistence-cassandra" % "0.3.3"
    val multiNode = akkaBase %% "akka-multi-node-testkit" % version
//    val kafkaPersistence = "com.github.krasserm" %% "akka-persistence-kafka" % "0.3.2"
  }

  object Spray {
    private val version = "1.3.2-20140909" // Same version as used in wandoulabs websocket
    private val sprayBase = "io.spray"
    private val wandoulabsWebsocketVersion = "0.1.3"

    val can = sprayBase %% "spray-can" % version
    val routing = sprayBase %% "spray-routing" % version
    val testkit = sprayBase %% "spray-testkit" % version

    val websocket = "com.wandoulabs.akka" %% "spray-websocket" % wandoulabsWebsocketVersion
  }

  object Playframework {
    private val version = "2.3.4"
    private val playBase = "com.typesafe.play"

    val json = playBase %% "play-json" % version
    val functional = playBase %% "play-functional" % version

  }

}
