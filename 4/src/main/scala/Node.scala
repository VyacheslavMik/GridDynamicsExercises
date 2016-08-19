package griddynamicsexercise

import scala.collection.mutable.{ArrayBuffer, HashMap}

import Logger._
import akka.actor.{ Actor, ActorLogging, ActorRef }
import akka.contrib.pattern.ReceivePipeline
import akka.contrib.pattern.ReceivePipeline.{ Inner, HandledCompletely }

case class Message(value: Int)

abstract class Node(
  id: Int,
  count: Int,
  rand: RandomProvider,
  sums: ArrayBuffer[Int],
  nodes: HashMap[Int, ActorRef]
) extends Actor with ActorLogging with ReceivePipeline {

  protected val pNumber = rand nextInt ()

  info(s"Node $id. Number - $pNumber")

  pipelineInner {
    case Start =>
      if (count == 1) {
        printSum(pNumber)
        HandledCompletely
      } else Inner(Start)

    case msg @ Message(value) =>
      info(s"Node $id. Recieved $value")
      Inner(msg)

    case value =>
      warning(s"Unexpected: $value")
      HandledCompletely
  }

  def sendTo(id: Int, value: Int): Unit = {
    info(s"Node ${this.id}. Sending $value to Node $id")
    nodes(id) ! Message(value)
  }

  protected def printSum(sum: Int): Unit = {
    info(s"Node $id. Calculated sum - $sum")
    sums += sum
    if (sums.length == count) context.system.terminate()
  }
}
