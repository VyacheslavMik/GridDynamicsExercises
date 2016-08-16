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

  private var m_sum = 0

  def startAlgorithm(): Unit = {
    if (id == 0) m_sum = p_number
    else sendTo(0, p_number)
  }

  def onMessageRecieved(value: Int) = {
    if (id == 0) {
      m_sum += value

      if (p_receivedMessages == count - 1) {
        for (i <- 1 until count) sendTo(i, m_sum)
        printSum(m_sum)
      }
    }
    else printSum(value)
  }
}
