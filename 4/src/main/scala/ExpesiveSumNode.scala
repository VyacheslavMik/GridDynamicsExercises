package griddynamicsexercise

import akka.actor.ActorRef
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class ExpensiveSumNode(id: Int, count: Int, rand: RandomProvider, nodeManager: ActorRef)
    extends Node(id, count, rand, nodeManager) {

  private val m_isLast = id == count - 1

  def startAlgorithm(): Unit = {
    if (count == 1) {
      printSum(p_number)
    } else {
      id match {
        case 0 =>
          sendTo(1, p_number)
          receiveSum()

        case _ =>
          val n = recv
          val nextId =
            if (id == count - 1) 0
            else id + 1

          val sum = n + p_number
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
}
