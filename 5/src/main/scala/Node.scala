package griddynamicsexercise

import Logger._
import akka.actor.{ Actor, ActorLogging, ActorRef }
import akka.contrib.pattern.ReceivePipeline
import akka.contrib.pattern.ReceivePipeline.{ Inner, HandledCompletely }
import scala.util.Random

abstract class Node(
  id: Int,
  count: Int,
  nodeManager: ActorRef
) extends Actor with ActorLogging with ReceivePipeline {

  protected val pNumber = Random nextInt ()

  info(s"Node $id. Number - $pNumber")

  nodeManager ! Register(id)

  pipelineInner {
    case Start =>
      if (count == 1) {
        printSum(pNumber)
        HandledCompletely
      } else Inner(Start)

    case value: Int =>
      info(s"Node $id. Recieved $value")
      Inner(value)

    case value =>
      warning(s"Unexpected: $value")
      HandledCompletely
  }

  def sendTo(id: Int, value: Int): Unit = {
    info(s"Node ${this.id}. Sending $value to Node $id")
    nodeManager ! Message(id, value)
  }

  protected def printSum(sum: Int): Unit = {
    info(s"Node $id. Calculated sum - $sum")
    nodeManager ! Unregister(id)
    context.system.terminate()
  }
}
