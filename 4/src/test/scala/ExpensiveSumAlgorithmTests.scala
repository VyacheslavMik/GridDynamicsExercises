package griddynamicsexercise.tests

import akka.actor.{ ActorSystem, Props, ActorRef }
import griddynamicsexercise._
import org.scalatest._
import scala.collection.mutable.{ ArrayBuffer, HashMap }

class ExpensiveSumAlgorithmSpec extends FlatSpec with Matchers {

  "An expensive sum algorithm" should "give correct sum on one node" in {
    Logger.disableLogging()

    val system = ActorSystem()
    val sum = 5
    val rand = new RandomProvider {
      def nextInt = sum
    }
    val sums = new ArrayBuffer[Int]
    val nodes = HashMap[Int, ActorRef]()
    val actor = system actorOf (Props(new ExpensiveSumNode(0, 1, rand, sums, nodes)))

    nodes += ((0, actor))

    actor ! Start

    system.awaitTermination()

    sums.length should be (1)
    sums(0) should be (sum)
  }

  it should "give correct sum on 2 nodes" in {
    Logger.disableLogging()

    val system = ActorSystem()
    val sum = 3
    val count = 2
    val rand = new RandomProvider {
      val numbers = Array(1, 2).iterator
      def nextInt = {
        numbers.synchronized {
          numbers.next()
        }
      }
    }
    val sums = new ArrayBuffer[Int]
    val nodes = HashMap[Int, ActorRef]()
    for(i <- 0 until count) {
      val actor = system actorOf (Props(new ExpensiveSumNode(i, count, rand, sums, nodes)))
      nodes += ((i, actor))
    }

    for ((_, node) <- nodes) node ! Start

    system.awaitTermination()

    sums.length should be (count)
    sums.forall { _ == sum } should be (true)
  }

  it should "give correct sum on n nodes" in {
    Logger.disableLogging()

    val system = ActorSystem()
    val sum = 6
    val count = 3
    val rand = new RandomProvider {
      val numbers = Array(1, 2, 3).iterator
      def nextInt = {
        numbers.synchronized {
          numbers.next()
        }
      }
    }
    val sums = new ArrayBuffer[Int]
    val nodes = HashMap[Int, ActorRef]()
    for(i <- 0 until count) {
      val actor = system actorOf (Props(new ExpensiveSumNode(i, count, rand, sums, nodes)))
      nodes += ((i, actor))
    }

    for ((_, node) <- nodes) node ! Start

    system.awaitTermination()

    sums.length should be (count)
    sums.forall { _ == sum } should be (true)
  }
}
