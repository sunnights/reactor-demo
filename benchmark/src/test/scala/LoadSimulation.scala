package reactor

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class LoadSimulation extends Simulation {

  // 从系统变量读取 baseUrl、path和模拟的用户数
  val baseUrl = System.getProperty("base.url")
  val uri = System.getProperty("uri")
  val path = System.getProperty("path")
  val qps = System.getProperty("qps").toInt
  val duration = System.getProperty("duration").toInt
  val uriPath = uri + path

  val httpConf = http.warmUp(baseUrl).baseUrl(baseUrl).shareConnections.maxConnectionsPerHost(5000)

  // 定义模拟的场景
  val scn0 = scenario("warmup").exec(http(qps + "_" + "/warmup").get("/netty4"+path))
  val scn1 = scenario("netty4").exec(http(qps + "_" + "/netty4").get("/netty4"+path))
  val scn2 = scenario("reactor").exec(http(qps + "_" + "/reactor").get("/reactor"+path))

  setUp(
    scn0.inject(constantUsersPerSec(qps) during (5 seconds)),
    scn1.inject(nothingFor(10 seconds), constantUsersPerSec(qps) during (duration seconds)),
    scn2.inject(nothingFor(25 seconds), constantUsersPerSec(qps) during (duration seconds))
  ).protocols(httpConf)
}

// mvn gatling:test -Dgatling.simulationClass=reactor.LoadSimulation -Dbase.url=http://10.77.9.41:9001 -Duri=/netty4 -Dpath="?latency=100" -Dqps=100 -Dduration=10