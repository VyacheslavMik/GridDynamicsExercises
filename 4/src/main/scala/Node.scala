package griddynamicsexercise

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import Logger._
import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.pattern.ask
import akka.util.Timeout

abstract class Node(id: Int, count: Int, rand: RandomProvider, nodeManager: ActorRef)
  extends Actor with ActorLogging {

  protected val p_number = rand nextInt ()

  info(s"Node $id. Number - $p_number")
  nodeManager ! RegisterNode(id)

  def sendTo(id: Int, value: Int): Unit = {
    info(s"Node ${this.id}. Sending $value to Node $id")
    nodeManager ! SendMessage(id, value)
  }

  def recv: Int = {
    implicit val timeout = Timeout(5.seconds)
    val valueF = nodeManager ? GetMessage(id)
    val value = Await.result(valueF, timeout.duration).asInstanceOf[Int]
    info(s"Node $id. Recieved $value")
    value
  }

  def receive = {
    case Start => Future { startAlgorithm() }
    case value => warning(s"Unexpected: $value")
  }

  def startAlgorithm(): Unit

  def endAlgorithm(sum: Int): Unit = {
    nodeManager ! Unregister(id, sum)
  }

  protected def printSum(sum: Int): Unit = {
    info(s"Node $id. Calculated sum - $sum")
    endAlgorithm(sum)
  }
}
