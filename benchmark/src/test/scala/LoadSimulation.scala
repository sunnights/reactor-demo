package reactor

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
  val scn1 = scenario(qps.toString).exec(http(qps + "_" + uri).get(uriPath).header("X-Proto", "SSL"))

  setUp(scn1.inject(constantUsersPerSec(qps) during (duration seconds)).protocols(httpConf))
  //  setUp(scn1.inject(atOnceUsers(simUsers)).protocols(httpConf))
  // 配置并发用户的数量在30秒内均匀提高至sim_users指定的数量
  //  setUp(scn1.inject(rampUsers(qps).during(10 seconds)).protocols(httpConf))
}