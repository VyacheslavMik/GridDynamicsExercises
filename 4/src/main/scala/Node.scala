package griddynamicsexercise

import scala.collection.mutable.{ArrayBuffer, HashMap}

import Logger._
import akka.actor.{Actor, ActorLogging, ActorRef}

case class Message(value: Int)

abstract class Node(
  id: Int,
  count: Int,
  rand: RandomProvider,
  sums: ArrayBuffer[Int],
  nodes: HashMap[Int, ActorRef]
) extends Actor with ActorLogging {

  protected val p_number = rand nextInt ()
  protected var p_receivedMessages = 0

  info(s"Node $id. Number - $p_number")

  def sendTo(id: Int, value: Int): Unit = {
    info(s"Node ${this.id}. Sending $value to Node $id")
    nodes(id) ! Message(value)
  }

  def receive = {
    case Start =>
      if (count == 1) printSum(p_number)
      else startAlgorithm()

    case Message(value) =>
      info(s"Node $id. Recieved $value")
      p_receivedMessages += 1
      onMessageRecieved(value)

    case value => warning(s"Unexpected: $value")
  }

  def startAlgorithm(): Unit

  def onMessageRecieved(value: Int): Unit

  protected def printSum(sum: Int): Unit = {
    info(s"Node $id. Calculated sum - $sum")
    sums += sum
    if (sums.length == count) context.system.shutdown()
  }
}
