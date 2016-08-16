package griddynamicsexercise

import akka.actor.{ ActorRef, ActorSystem, Props }
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import StringUtils._
import scala.io.StdIn.readLine
import scala.collection.mutable.HashMap

case object Start

object Program {

  def main(args: Array[String]) = {
    val count = getCount
    val system = ActorSystem()
    val createNode = getNodeCtor
    val numbers = for (i <- 0 until count) yield Random nextInt ()
    val sum = numbers.sum
    val rand = new RandomProvider {
      val iter = numbers.iterator
      def nextInt = iter.next
    }
    val sums = ArrayBuffer[Int]()
    val nodes = HashMap[Int, ActorRef]()
    for (i <- 0 until count) {
      val actor = system actorOf (Props(createNode(i, count, rand, sums, nodes)), s"Node$i")
      nodes += ((i, actor))
    }

    for ((_, node) <- nodes) {
      (node ! Start)
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

  def getNodeCtor(): (Int, Int, RandomProvider, ArrayBuffer[Int], HashMap[Int, ActorRef]) => Node = {
    var n: Option[Int] = None

    do {
      n = readLine(s"""1. Simple algorithm.
                      |2. Expensive sum algorithm.
                      |3. Tree-based algorithm.
                      |Select algorithm (1 or 2 or 3): """.stripMargin).toIntOpt
    }
    while(n == None || (n.get != 1 && n.get != 2 && n.get != 3));

    n.get match {
      case 1 => (i, count, rand, sums, nodes) => new SimpleNode(i, count, rand, sums, nodes)
      case 2 => (i, count, rand, sums, nodes) => new ExpensiveSumNode(i, count, rand, sums, nodes)
      case 3 => (i, count, rand, sums, nodes) => new TreeNode(i, count, rand, sums, nodes) 
    }
  }
}
