package griddynamicsexercise

import akka.actor.ActorRef

class TreeNode(
  id: Int,
  count: Int,
  nodeManager: ActorRef
) extends Node(id, count, nodeManager) {

  import context._

  private var m_buf = 0

  private val mParent: Option[Int] = {
    val isEven = (id + 1) % 2 == 0
    val parent = if (isEven) (id + 1) / 2 else id / 2
    if (parent == 0) None else Some(parent - 1)
  }

  private val mLeft: Option[Int] = {
    val left = (id + 1) * 2 - 1
    if (left > count - 1) None else Some(left)
  }

  private val mRight: Option[Int] = {
    val right = (id + 1) * 2
    if (right > count - 1) None else Some(right)
  }

  def receive = {
    case Start =>
      (mLeft, mRight) match {
        case (None, None) =>
          sendNumberToParent
          become(receiveSum)

        case _ =>
          become(receiveNumbersFromChildren)
      }
  }

  def startAlgorithm() = {
    (mParent, mLeft, mRight) match {
      case (Some(parent), None, None) =>
        sendTo(parent, pNumber)

      case _ =>
    }
  }

  def sendNumberToParent = {
    sendTo(mParent.get, pNumber)
  }

  def receiveNumbersFromChildren: Receive = {
    var count = (mLeft, mRight) match {
      case (Some(_), None) | (None, Some(_)) => 1
      case (Some(_), Some(_)) => 2
      case _ => throw new RuntimeException("Unexpected case")
    }
    var sum = pNumber

    {
      case value: Int =>
        count -= 1
        sum += value

        if (count == 0) {
          mParent match {
            case None =>
              sendSumToChildren(sum)
              printSum(sum)

            case Some(parent) =>
              sendTo(parent, sum)
              become(receiveSum)
          }
        }
    }
  }

  def receiveSum: Receive = {
    case value: Int =>
      sendSumToChildren(value)
      printSum(value)
  }

  def sendSumToChildren(sum: Int) = {
    mLeft foreach { sendTo(_, sum) }
    mRight foreach { sendTo(_, sum) }
  }
}
