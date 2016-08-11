package griddynamicsexercise

object Program {

  def main(args: Array[String]) = {
    val count = 5
    val nodes = for (i <- 0 until count) yield new ExpensiveSumNode(i, count)

    for (node <- nodes) {
      node start ()
    }
  }
}
