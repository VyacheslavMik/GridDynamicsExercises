package griddynamicsexercise

import scala.collection.mutable.{ ArrayBuffer, HashMap }

import akka.actor.ActorRef

class ExpensiveSumNode(
  id: Int,
  count: Int,
  rand: RandomProvider,
  sums: ArrayBuffer[Int],
  nodes: HashMap[Int, ActorRef]
) extends Node(id, count, rand, sums, nodes) {

  import context._

  def receive = {
    case Start =>
      if (id == 0) {
        sendTo(1, pNumber)
        become(receiveSum)
      } else become(increaseByPNumberAndPropogateValue)
  }

  def increaseByPNumberAndPropogateValue: Receive = {
    case Message(value) =>
      val nextId = if (id == count - 1) 0 else id + 1
      val sum = pNumber + value

      sendTo(nextId, sum)

      if (id == count - 1) printSum(sum) // last node already have sum, just print it
      else become(receiveSum)
  }

  def receiveSum: Receive = {
    case Message(value) =>
      printSum(value)
      // last node already have sum, that's why we haven't to send sum to it
      if (id < count - 2) sendTo(id + 1, value)
  }
}
