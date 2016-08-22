package griddynamicsexercise

object Logger {

  private var enabled = true

  def disableLogging () = enabled = false
  def info(msg: String) = if (enabled) println(s"Info. $msg")
  def warning(msg: String) = if (enabled) println(s"Warning. $msg")
}
