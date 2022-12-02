package Roulette.controller

object State extends Enumeration {
    type State = Value
    val BET, RESULT, IDLE = Value

    val message = Map[State, String](
        BET -> "Betting phase starts now!",
        RESULT -> "Betting results are here!",
        IDLE -> ""
    )

    def printState(s: State): String = {
       message(s)
    }
}
