package griddynamicsexercise

import scala.collection.mutable.{ ArrayBuffer, HashMap }

import akka.actor.ActorRef

class TreeNode(
  id: Int,
  count: Int,
  rand: RandomProvider,
  sums: ArrayBuffer[Int],
  nodes: HashMap[Int, ActorRef]
) extends Node(id, count, rand, sums, nodes) {

  private var m_buf = 0

  private val m_parent: Option[Int] = {
    val isEven = (id + 1) % 2 == 0
    val parent = if (isEven) (id + 1) / 2 else id / 2
    if (parent == 0) None else Some(parent - 1)
  }

  private val m_left: Option[Int] = {
    val left = (id + 1) * 2 - 1
    if (left > count - 1) None else Some(left)
  }

  private val m_right: Option[Int] = {
    val right = (id + 1) * 2
    if (right > count - 1) None else Some(right)
  }

  def startAlgorithm() = {
    (m_parent, m_left, m_right) match {
      case (Some(parent), None, None) =>
        sendTo(parent, p_number)

      case _ =>
    }
  }

  def onMessageRecieved(value: Int) = {
    (m_parent, m_left, m_right) match {
      case (None, Some(left), Some(right)) =>
        if (p_receivedMessages == 2) {
          val sum = m_buf + value + p_number
          sendTo(left, sum)
          sendTo(right, sum)
          printSum(sum)
        } else {
          m_buf = value
        }

      case (None, Some(left), None) =>
        val sum = value + p_number
        sendTo(left, sum)
        printSum(sum)

      case (None, None, Some(right)) =>
        val sum = value + p_number
        sendTo(right, sum)
        printSum(sum)

      case (Some(_), None, None) =>
        printSum(value)

      case (Some(parent), Some(left), None) =>
        if (p_receivedMessages == 1) sendTo(parent, value + p_number)
        else {
          sendTo(left, value)
          printSum(value)
        }

      case (Some(parent), None, Some(right)) =>
        if (p_receivedMessages == 1) sendTo(parent, value + p_number)
        else {
          sendTo(right, value)
          printSum(value)
        }

      case (Some(parent), Some(left), Some(right)) =>
        if (p_receivedMessages == 1) m_buf = value
        else if (p_receivedMessages == 2) sendTo(parent, m_buf + p_number + value)
        else {
          sendTo(left, value)
          sendTo(right, value)
          printSum(value)
        }

      case (None, None, None) => Logger.warning(s"Unexpected case")
    }
  }
}
