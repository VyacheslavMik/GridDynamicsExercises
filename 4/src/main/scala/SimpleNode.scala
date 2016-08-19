package griddynamicsexercise

import scala.collection.mutable.{HashMap, ArrayBuffer}

import akka.actor.ActorRef

class SimpleNode(
  id: Int,
  count: Int,
  rand: RandomProvider,
  sums: ArrayBuffer[Int],
  nodes: HashMap[Int, ActorRef]
) extends Node(id, count, rand, sums, nodes) {

  import context._

  def receive = {
    case Start =>
      if (id == 0) become(receiveAllOtherNumbers)
      else {
        sendTo(0, pNumber)
        become(receiveSum)
      }
  }

  def receiveAllOtherNumbers: Receive = {
    var receivedMessages = 0
    var sum = pNumber

    {
      case Message(value) =>
        receivedMessages += 1
        sum += value
        sendSumToOtherNodes(receivedMessages, sum)
    }
  }

  def sendSumToOtherNodes(receivedMessages: Int, sum: Int) = {
    if (receivedMessages == count - 1) {
      for (i <- 1 until count) sendTo(i, sum)
      printSum(sum)
    }
  }

  def receiveSum: Receive = {
    case Message(value) =>
      printSum(value)
  }
}
