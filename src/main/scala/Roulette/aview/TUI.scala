package Roulette.aview

import Roulette.model.Player
import Roulette.util.Observer
import Roulette.model.Bet
import Roulette.controller.Controller
import Roulette.controller.State

import scala.io.StdIn.readLine
import scala.util.control.Breaks._
import scala.collection.immutable.VectorBuilder

case class TUI(controller: Controller) extends Observer: //player: Player

  val vc = VectorBuilder[Bet]
  controller.add(this)
  controller.setupPlayers()
  inputLoop()

  def inputLoop(): Unit = {
    val randomNumber = controller.generateRandomNumber()
    controller.changeState(State.BET)
    vc.clear()
    println("\n" + controller.printState())

    while(controller.state == State.BET) {
      for (player_index <- 0 until controller.getPlayerCount()) {
        print("\nTurn of player " + (player_index + 1) + "\n" + "Available money: $" + controller.players(player_index).getAvailableMoney() + "\n")

        val bet = new Bet
        val bet_type = readLine("\nDo you want to place a bet on a number (n), on odd or even (o) or on a color (c)? If one of the players types (d), the betting phase will stop. (u)ndo (r)edo >>>")

        bet_type match
          case "n" =>
            val bet_number = readLine("On which number do you want to place your bet? (0-36) >>>").toInt
            val bet_amount = readLine("How much money do you want to bet? >>>$").toInt
            bet.withBetType(bet_type).withRandomNumber(randomNumber).withPlayerIndex(player_index).withBetNumber(bet_number).withBetAmount(bet_amount)
            vc.addOne(bet)
            print("<<<Your bet was placed!>>>\n")
          case "o" =>
            val bet_odd_or_even = readLine("Do you want to bet on odd (o) or even (e)? >>>")
            val bet_amount = readLine("How much money do you want to bet? >>>$").toInt
            bet.withBetType(bet_type).withRandomNumber(randomNumber).withPlayerIndex(player_index).withOddOrEven(bet_odd_or_even).withBetAmount(bet_amount)
            vc.addOne(bet)
            print("<<<Your bet was placed!>>>\n")
          case "c" =>
            val bet_color = readLine("Do you want to bet on red (r) or black (b)? >>>")
            val bet_amount = readLine("How much money do you want to bet? >>>$").toInt
            bet.withBetType(bet_type).withRandomNumber(randomNumber).withPlayerIndex(player_index).withColor(bet_color).withBetAmount(bet_amount)
            vc.addOne(bet)
            print("<<<Your bet was placed!>>>\n")
          case "d" =>
            controller.changeState(State.RESULT)
          case "r" => controller.redo()
          case "u" => controller.undo()
          
          controller.changeMoney(player_index, bet.bet_amount, false)
      }
    }
    println(controller.printState())
    print("The roulette number is: " + randomNumber)
    val bets = controller.calculateBets(vc.result())
    for (s <- bets) {
      println(s)
    }
    inputLoop()
  }

  override def update: Unit = println("")
