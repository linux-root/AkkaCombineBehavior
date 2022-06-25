import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object Counter {
  sealed trait Command

  case object Incr extends Command
  case object  Show extends Command
  case object Active extends Command
  case object DeActive extends Command

  def online(state: State): Behavior[Command] = Behaviors.receiveMessagePartial(
     countHandler(state, state.active,  online)
       .orElse(activeHandler(state, online))
  )


  def countHandler(state: State,
                   isActive: Boolean,
                   nextState: State => Behavior[Command]): PartialFunction[Command, Behavior[Command]] = {
    case Incr =>
      if (isActive){
        nextState(state.++)
      } else {
        println("state is NOT active")
        Behaviors.same
      }

    case Show =>
      println(s"count is ${state.count}")
      Behaviors.same
  }

  def activeHandler(state: State, nextState: State => Behavior[Command]): PartialFunction[Command, Behavior[Command]] = {
    case Active =>
      if (state.active){
        println("state already active")
        Behaviors.same
      } else {
        nextState(state.beActive)
      }
    case DeActive =>
    if (!state.active){
        println("state already NOT active")
        Behaviors.same
      } else {
        nextState(state.notActive)
      }
  }

}
case class State(count: Int, private val _active: Boolean){
  def ++ : State = this.copy(count = this.count + 1)
  def active: Boolean = {
    println("get Active")
    this._active
  }
  def beActive:  State = this.copy(_active = true)
  def notActive: State = this.copy(_active = false)
}
