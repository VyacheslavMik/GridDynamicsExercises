package griddynamicsexercise

object Logger {

  var enabled = true

  def info(msg: String) = if (enabled) println(s"Info. $msg")
  def warning(msg: String) = if (enabled) println(s"Warning. $msg")
}
