package griddynamicsexercise.tests

import akka.actor.{ ActorSystem, Props, ActorRef }
import griddynamicsexercise._
import org.scalatest._
import scala.collection.mutable.{ ArrayBuffer, HashMap }
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class TreeBasedAlgorithmSpec extends FlatSpec with Matchers {

  "An tree-based algorithm" should "give correct sum on one node" in {
    Logger.disableLogging()

    val system = ActorSystem()
    val sum = 5
    val rand = new RandomProvider {
      def nextInt = sum
    }
    val sums = new ArrayBuffer[Int]
    val nodes = HashMap[Int, ActorRef]()
    val actor = system actorOf (Props(new TreeNode(0, 1, rand, sums, nodes)))

    nodes += ((0, actor))

    actor ! Start

    Await.ready(system.whenTerminated, Duration.Inf)

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
      val actor = system actorOf (Props(new TreeNode(i, count, rand, sums, nodes)))
      nodes += ((i, actor))
    }

    for ((_, node) <- nodes) node ! Start

    Await.ready(system.whenTerminated, Duration.Inf)

    sums.length should be (count)
    sums.forall { _ == sum } should be (true)
  }

  it should "give correct sum on n nodes" in {
    Logger.disableLogging()

    val system = ActorSystem()
    val sum = 28
    val count = 7
    val rand = new RandomProvider {
      val numbers = Array(1, 2, 3, 4, 5, 6, 7).iterator
      def nextInt = {
        numbers.synchronized {
          numbers.next()
        }
      }
    }
    val sums = new ArrayBuffer[Int]
    val nodes = HashMap[Int, ActorRef]()
    for(i <- 0 until count) {
      val actor = system actorOf (Props(new TreeNode(i, count, rand, sums, nodes)))
      nodes += ((i, actor))
    }

    for ((_, node) <- nodes) node ! Start

    Await.ready(system.whenTerminated, Duration.Inf)

    sums.length should be (count)
    sums.forall { _ == sum } should be (true)
  }
}
