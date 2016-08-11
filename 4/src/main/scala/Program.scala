package griddynamicsexercise

import akka.actor.{ ActorRef, ActorSystem, Props }
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import StringUtils._
import scala.io.StdIn.readLine

case object Start

object Program {

  def main(args: Array[String]) = {
    val count = getCount
    val system = ActorSystem()
    val createNode = getNodeCtor
    val numbers = for (i <- 0 until count) yield Random nextInt 10
    val sum = numbers.sum
    val rand = new RandomProvider {
      val iter = numbers.iterator
      def nextInt = iter.next
    }
    val nodeManager = system actorOf Props(new NodeManager(new ArrayBuffer))

    val actors = for (i <- 0 until count)
    yield system actorOf (Props(createNode(i, count, rand, nodeManager)), s"Node$i")

    for (actor <- actors) {
      (actor ! Start)
    }

    system.awaitTermination()
  }

  def getCount(): Int = {
    var n: Option[Int] = None

    do {
      n = readLine(s"Input node count (1 - ${Int.MaxValue}): ").toIntOpt
    }
    while(n == None || n.get <= 0 || n.get >= Int.MaxValue);

    n.get
  }

  def getNodeCtor(): (Int, Int, RandomProvider, ActorRef) => Node = {
    var n: Option[Int] = None

    do {
      n = readLine(s"""1. Simple algorithm.
                      |2. Expensive sum algorithm.
                      |Select algorithm (1 or 2): """.stripMargin).toIntOpt
    }
    while(n == None || (n.get != 1 && n.get != 2));

    n.get match {
      case 1 => (i, count, rand, nodeManager) => { new SimpleNode(i, count, rand, nodeManager) }
      case 2 => (i, count, rand, nodeManager) => { new ExpensiveSumNode(i, count, rand, nodeManager) }
    }
  }
}
