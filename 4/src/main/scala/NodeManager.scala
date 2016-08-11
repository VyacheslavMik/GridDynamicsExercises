package griddynamicsexercise

import akka.actor.{Actor, ActorLogging, ActorRef}
import scala.collection.mutable.{ArrayBuffer, HashMap, HashSet, Queue}

case class SendMessage(id: Int, value: Int)
case class RegisterNode(id: Int)
case class Unregister(id: Int, sum: Int)
case class GetMessage(id: Int)

class NodeManager(sums: ArrayBuffer[Int]) extends Actor with ActorLogging {

  private val m_registeredNodes = new HashSet[Int]
  private val m_messages = new HashMap[Int, Queue[Int]]
  private val m_waitingMessages = new HashMap[Int, ActorRef]

  def receive = {
    case RegisterNode(id) =>
      m_registeredNodes += id

    case SendMessage(id, value) =>
      if (m_waitingMessages contains id) {
        m_waitingMessages(id) ! value
        m_waitingMessages -= id
      } else {
        if (!m_messages.contains(id)) m_messages += ((id, new Queue[Int]))
        m_messages(id) += value
      }

    case GetMessage(id) =>
      if (m_messages.contains(id) && !m_messages(id).isEmpty)
        sender ! m_messages(id).dequeue()
      else
        m_waitingMessages += ((id, sender()))

    case Unregister(id, sum) =>
      m_registeredNodes -= id
      sums += sum
      if (m_registeredNodes.isEmpty) context.system.shutdown()

    case value => log.warning(s"Unexpected: $value")
  }
}
