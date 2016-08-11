package griddynamicsexercise

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import akka.actor.ActorRef

class SimpleNode(id: Int, count: Int, rand: RandomProvider, nodeManager: ActorRef)
  extends Node(id, count, rand, nodeManager) {

  def startAlgorithm(): Unit = {
    if (id != 0) {
      sendTo(0, p_number)
      Future {
        printSum(recv)
      }
    } else {
      val sumF = Future {
        (for (_ <- 1 until count) yield recv).sum + p_number
      }
      sumF onSuccess {
        case sum => {
          for (i <- 1 until count) sendTo(i, sum)
          printSum(sum)
        }
      }
    }
  }
}
