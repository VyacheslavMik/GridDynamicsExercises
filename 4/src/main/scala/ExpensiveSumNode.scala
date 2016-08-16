package griddynamicsexercise

import scala.collection.mutable.{ArrayBuffer, HashMap}

import akka.actor.ActorRef

class ExpensiveSumNode(
  id: Int,
  count: Int,
  rand: RandomProvider,
  sums: ArrayBuffer[Int],
  nodes: HashMap[Int, ActorRef]
) extends Node(id, count, rand, sums, nodes) {

  def startAlgorithm(): Unit = {
    if (id == 0) sendTo(1, p_number)
  }

  def onMessageRecieved(value: Int) = {
    (id, p_receivedMessages) match {
      case (0, _) =>
        if (count > 2) sendTo(1, value)
        printSum(value)

      case (id, 1) =>
        val nextId = if (id == count - 1) 0 else id + 1
        val sum = p_number + value
        sendTo(nextId, sum)
        if (id == count - 1) printSum(sum)

      case (id, 2) =>
        printSum(value)
        if (id < count - 2) sendTo(id + 1, value)
    }
  }
}
