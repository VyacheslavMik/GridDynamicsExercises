package griddynamicsexercise

import java.net.NetworkInterface

import scala.collection.JavaConversions._
import scala.concurrent.Await
import scala.concurrent.duration._

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory, ConfigResolveOptions, ConfigValueFactory}

case object Start extends Serializable

object Program {

  def main(args: Array[String]): Unit = {
    args match {
      case Array("manager") =>
        println("Starting node manager")
        load() match {
          case Some(ip) =>
            val system = ActorSystem("system", loadConfig(ip, 5150))
            val manager = system.actorOf(Props[NodeManager], "manager")
            Await.ready(system.whenTerminated, Duration.Inf)

          case None =>
            println("IP not found")
        }

      case Array("node", managerIp, managerPort, id, count, algorithm) =>
        println(s"Starting node $id. Total nodes $count. Algorithm $algorithm. Manager: $managerIp:$managerPort")
        load() match {
          case Some(ip) =>
            val system = ActorSystem("system", loadConfig(ip, 0))
            val nodeCtor = getNodeCtor(algorithm.toInt)
            implicit val resolveTimeout = Timeout(5.seconds)
            val manager = Await.result(
              system.actorSelection(s"akka.tcp://system@$managerIp:$managerPort/user/manager")
                .resolveOne(),
              resolveTimeout.duration)
            val node = system.actorOf(Props(nodeCtor(id.toInt, count.toInt, manager)))
            node ! Start
            Await.ready(system.whenTerminated, Duration.Inf)

          case None =>
            println("IP not found")
        }

      case _ =>
        println("Wrong arguments")
    }
  }

    def loadConfig(ip: String, port: Int): Config = {
    val ipValue = ConfigValueFactory fromAnyRef ip
    val portValue = ConfigValueFactory fromAnyRef port
    ConfigFactory.load(
      getClass.getClassLoader,
      ConfigResolveOptions.defaults.setAllowUnresolved(true)
    ).withValue("ip", ipValue)
      .withValue("port", portValue)
      .resolve
  }

  def load(): Option[String] = {
    val interfaces = NetworkInterface.getNetworkInterfaces()
    val interface = interfaces find (_.getName equals "eth0")

    interface flatMap { inet =>
      // the docker adress should be siteLocal
      inet.getInetAddresses find (_.isSiteLocalAddress) map (_.getHostAddress)
    }
  }

  def getNodeCtor(algorithm: Int): (Int, Int, ActorRef) => Node = {
    algorithm match {
      case 1 => (i, count, nodeManager) => new SimpleNode(i, count, nodeManager)
      case 2 => (i, count, nodeManager) => new ExpensiveSumNode(i, count, nodeManager)
      case 3 => (i, count, nodeManager) => new TreeNode(i, count, nodeManager)
    }
  }
}
