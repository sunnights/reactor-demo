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
  val scn1 = scenario(qps.toString).exec(http(qps + "_" + uri).get(uriPath))
  setUp(scn1.inject(constantUsersPerSec(qps) during (duration seconds)).protocols(httpConf))
}

// mvn gatling:test -Dgatling.simulationClass=reactor.LoadSimulation -Dbase.url=http://10.77.9.41:9001 -Duri=/netty4 -Dpath="?latency=100" -Dqps=100 -Dduration=10