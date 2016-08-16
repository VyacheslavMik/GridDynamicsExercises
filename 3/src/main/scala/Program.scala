package griddynamicsexercise

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object Program {

  def main(args: Array[String]): Unit = {
    val count = 7
    val nodes = for (i <- 0 until count) yield new TreeNode(i, count)
    val futures = for (node <- nodes) yield node.start
    val f = Future.sequence(futures)
    Await.ready(f, Duration.Inf)
    println("Program end")
  }
}
