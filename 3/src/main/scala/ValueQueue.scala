package griddynamicsexercise

import scala.collection.mutable.{HashMap, Queue}

object ValueQueue {

  private val m_sync = new Object
  private val m_messages = new HashMap[Int, Queue[Int]]

  def sendTo(id: Int, value: Int): Unit = {
    m_sync.synchronized {
      if (!m_messages.contains(id)) m_messages += ((id, new Queue[Int]))
      m_messages(id) += value
    }
  }

  def recv(id: Int): Option[Int] = {
    m_sync.synchronized {
      m_messages get id match {
        case Some(queue) =>
          if (queue.isEmpty) None
          else Some(queue dequeue ())
        case _ =>
          None
      }
    }
  }
}
