import akka.actor.typed.ActorSystem

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
object Main extends App{
  val state = State(0, _active = false)
  val system = ActorSystem(Counter.online(state), "wow")
  system ! Counter.Incr
  system ! Counter.Incr
  system ! Counter.Active
  system ! Counter.Incr
  system ! Counter.Show
  system ! Counter.Incr
  system ! Counter.Show
}
