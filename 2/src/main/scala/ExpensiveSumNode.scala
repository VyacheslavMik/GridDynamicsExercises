package griddynamicsexercise

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success}

class ExpensiveSumNode(id: Int, count: Int) {

  private val m_number = Random nextInt ()
  private val m_isLast = id == count - 1

  println(s"Node $id. Number - $m_number")

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

  def start(): Unit = {
    if (count == 1) {
      printSum(m_number)
    } else {
      id match {
        case 0 =>
          sendTo(1, m_number)
          receiveSum()

        case _ =>
          val n = recv
          val nextId =
            if (id == count - 1) 0
            else id + 1
          val sum = n + m_number
          sendTo(nextId, sum)

          if (m_isLast) printSum(sum)
          else receiveSum()
      }
    }
  }

  def receiveSum(): Unit = Future {
    recv
  } onComplete {
    case Success(sum) =>
      printSum(sum)
      if (id != count - 2) sendTo(id + 1, sum)

    case Failure(e) => e printStackTrace ()
  }

  def printSum(sum: Int): Unit = {
    println(s"Node $id. Calculated sum - $sum")
  }
}
