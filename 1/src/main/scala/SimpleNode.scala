package griddynamicsexercise

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

class SimpleNode(id: Int, count: Int) {

  private val m_number = Random nextInt ()
  var sum = 0

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
    if (id != 0) {
      sendTo(0, m_number)
      Future {
        sum = recv
        printSum()
      }
    } else {
      val sumF = Future {
        (for (_ <- 1 until count) yield recv).sum + m_number
      }
      sumF onSuccess {
        case calculatedSum => {
          for (i <- 1 until count) sendTo(i, calculatedSum)
          sum = calculatedSum
          printSum()
        }
      }
    }
  }

  def printSum(): Unit = {
    println(s"Node $id. Calculated sum - $sum")
  }
}
