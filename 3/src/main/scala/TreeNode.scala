package griddynamicsexercise

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Random
import scala.concurrent.blocking

class TreeNode(id: Int, count: Int) {

  private val m_number = Random nextInt 10
  private val m_isLast = id == count - 1

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

  println(s"Node $id. Parent - $m_parent. Left - $m_left. Right - $m_right. Number - $m_number")

  def sendTo(id: Int, value: Int): Unit = {
    println(s"Node ${this.id}. Sending $value to Node $id")
    ValueQueue sendTo (id, value)
  }

  def recv: Int = {
    var n: Option[Int] = None

    do {
      n = ValueQueue recv id
    } while (n == None);

    println(s"Node $id. Recieved ${n.get}")
    n.get
  }

  def start(implicit ec: ExecutionContext): Future[Unit] = {
    Future {
      println(s"Node $id. Starting")

      (m_parent, m_left, m_right) match {
        case (Some(parent), None, None) =>
          sendTo(parent, m_number)
          println(s"Node $id. Receiving sum.")
          blocking {
            printSum(recv)
          }

        case (Some(parent), None, Some(right)) =>
          println(s"Node $id. Receiving rightNumber.")
          val rightNumber = blocking { recv }
          sendTo(parent, m_number + rightNumber)
          println(s"Node $id. Receiving sum.")
          val sum = blocking { recv }
          sendTo(right, sum)
          printSum(sum)

        case (Some(parent), Some(left), None) =>
          println(s"Node $id. Receiving leftNumber.")
          val leftNumber = blocking { recv }
          sendTo(parent, m_number + leftNumber)
          println(s"Node $id. Receiving sum.")
          val sum = blocking { recv }
          sendTo(left, sum)
          printSum(sum)

        case (Some(parent), Some(left), Some(right)) =>
          println(s"Node $id. Receiving leftNumber.")
          val leftNumber = blocking { recv }
          println(s"Node $id. Receiving rightNumber.")
          val rightNumber = blocking { recv }
          sendTo(parent, leftNumber + m_number + rightNumber)
          println(s"Node $id. Receiving sum.")
          val sum = blocking { recv }
          sendTo(left, sum)
          sendTo(right, sum)
          printSum(sum)

        case (None, Some(left), None) =>
          println(s"Node $id. Receiving leftNumber.")
          val leftNumber = blocking { recv }
          val sum = m_number + leftNumber
          sendTo(left, sum)
          printSum(sum)

        case (None, None, Some(right)) =>
          println(s"Node $id. Receiving rightNumber.")
          val rightNumber = blocking { recv }
          val sum = m_number + rightNumber
          sendTo(right, sum)
          printSum(sum)

        case (None, Some(left), Some(right)) =>
          println(s"Node $id. Receiving rightNumber.")
          val rightNumber = blocking { recv }
          println(s"Node $id. Receiving leftNumber.")
          val leftNumber = blocking { recv }
          val sum = leftNumber + m_number + rightNumber
          sendTo(left, sum)
          sendTo(right, sum)
          printSum(sum)

        case (None, None, None) =>
          printSum(m_number)
      }
    }
  }

  def printSum(sum: Int): Unit = {
    println(s"Node $id. Calculated sum - $sum")
  }
}
