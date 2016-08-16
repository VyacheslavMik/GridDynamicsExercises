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

  private var m_buf = 0

  def startAlgorithm(): Unit = {
    if (id == 0) m_buf = p_number
    else sendTo(0, p_number)
  }

  def onMessageRecieved(value: Int) = {
    if (id == 0) {
      m_buf += value

      if (p_receivedMessages == count - 1) {
        for (i <- 1 until count) sendTo(i, m_buf)
        printSum(m_buf)
      }
    }
    else printSum(value)
  }
}
