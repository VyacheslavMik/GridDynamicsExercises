package griddynamicsexercise

import akka.actor.{ Actor, ActorLogging, ActorRef }
import scala.collection.mutable.{ HashMap, Queue }

case class Register(id: Int) extends Serializable
case class Message(id: Int, value: Int) extends Serializable
case class Unregister(id: Int) extends Serializable

class NodeManager extends Actor with ActorLogging {

  private val mNodes = HashMap[Int, ActorRef]()
  private val mMessages = HashMap[Int, Queue[Int]]()

  def receive = {
    case Register(id) =>
      val node = sender()
      mNodes += ((id, node))
      mMessages.get(id) foreach { messages =>
        while(messages.length > 0) {
          node ! messages.dequeue()
        }
      }

    case Message(id, value) =>
      mNodes get id match {
        case Some(node) =>
          node ! value
        case None =>
          if (!mMessages.contains(id)) {
            mMessages += ((id, Queue[Int]()))
          }
          mMessages(id) += value
      }

    case Unregister(id) =>
      mNodes -= id
      if (mNodes.isEmpty) context.system.terminate()
  }
}
