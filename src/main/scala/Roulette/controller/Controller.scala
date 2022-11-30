package Roulette.controller

import scala.collection.immutable.VectorBuilder
import scala.io.StdIn.readLine
import scala.util.Random

import Roulette.controller
import Roulette.model.{Bet, Player, PlayerUpdate}
import Roulette.util.{Observable, UndoManager}
import Roulette.controller.State._

class Controller(playerCount: Int, startingMoney: Int) extends Observable {

  val r = new Random()

  var state: State = IDLE
  var players = Vector[Player]()
  val undoManager = new UndoManager[Vector[Player]]

  def setupPlayers(): Unit = {
    val vc = VectorBuilder[Player]
    for (player_index <- 0 until playerCount) {
      vc.addOne(Player(startingMoney))
    }
    players = vc.result()
  }

  def updatePlayer(player_index: Int, updated_money: Int): Vector[Player] = {
    players.updated(player_index, Player(updated_money))
  }

  def changeMoney(player_index: Int, money: Int, add: Boolean): Unit = {
    var updated_money: Int = 0
    if (add == true)
      updated_money = players(player_index).getAvailableMoney() + money
    else
      updated_money = players(player_index).getAvailableMoney() - money
    players = doStep(new PlayerUpdate(player_index, money, add, this))
  }

  def calculateBets(bets: Vector[Bet]): Vector[String] = {
    val vc = VectorBuilder[String]
    for (bet <- bets) {
      val total_win: Int = 0
      bet.bet_type match
        case "n" =>
          vc.addOne(num(bet))
        case "o" =>
          vc.addOne(evenOdd(bet))
        case "c" =>
          vc.addOne(color(bet))
    }
    vc.result()
  }

  def generateRandomNumber(): Int = {
    r.nextInt(37)
  }

  def win(playerIndex: Int, bet: Int, winRate: Int): String = {
    val won_money: Int = bet * winRate
    val new_money: Int = players(playerIndex).getAvailableMoney() + won_money
    changeMoney(playerIndex, new_money, true)
    val retvalue = "Player " + (playerIndex + 1) + " won their bet of $" + won_money + ". They now have $" + players(playerIndex).getAvailableMoney() + " available."
    notifyObservers
    retvalue
  }

  def lose(playerIndex: Int, bet: Int): String = {
    val lost_money: Int = bet
    val retval = "Player " + (playerIndex + 1) + " lost their bet of $" + lost_money + ". They now have $" + players(playerIndex).getAvailableMoney() + " available."
    notifyObservers
    retval
  }

  def doStep(player_update: PlayerUpdate): Vector[Player] = {
    undoManager.doStep(players, PutCommand(player_update))
  }
  
  def undo(): Unit = {
    players = undoManager.undoStep(players)
  }
  
  def redo(): Unit = {
    players = undoManager.redoStep(players)
  }

  def getPlayerCount(): Int = {
    playerCount
  }

  def changeState(state: Value): Unit = {
    this.state = state
  }

  def getState(): State = {
    this.state
  }

  def printState(): String = {
    State.printState(state)
  }

  def num(bet: Bet): String = {
    NumExpression(bet).interpret()
  }

  def evenOdd(bet: Bet): String = {
    EOExpression(bet).interpret()
  }

  def color(bet: Bet): String = {
    ColorExpression(bet).interpret()
  }

  //Interpreter Pattern
  trait Expression {
    def interpret(): String
  }

  class NumExpression(bet: Bet) extends Expression {
    var retval = ""

    def interpret(): String = {
      if (bet.random_number == bet.bet_number)
        retval = retval.concat(win(bet.player_index, bet.bet_amount, 36))
      else
        retval = retval.concat(lose(bet.player_index, bet.bet_amount))
      retval
    }
  }

  class EOExpression(bet: Bet) extends Expression {
    var retval = ""

    def interpret(): String = {
      bet.bet_odd_or_even match
        case "o" =>
          if (bet.random_number % 2 != 0)
            retval = retval.concat(win(bet.player_index, bet.bet_amount, 2))
          else
            retval = retval.concat(lose(bet.player_index, bet.bet_amount))
        case "e" =>
          if (bet.random_number % 2 == 0)
            retval = retval.concat(win(bet.player_index, bet.bet_amount, 2))
          else
            retval = retval.concat(lose(bet.player_index, bet.bet_amount))
      retval
    }
  }

  class ColorExpression(bet: Bet) extends Expression {
    var retval = ""

    var redNumbers = Array(1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36)
    var blackNumbers = Array(2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35)
    def interpret(): String = {
      bet.bet_color match
        case "r" =>
          if (redNumbers.contains(bet.random_number))
            retval = retval.concat(win(bet.player_index, bet.bet_amount, 2))
          else
            retval = retval.concat(lose(bet.player_index, bet.bet_amount))

        case "b" =>
          if (blackNumbers.contains(bet.random_number))
            retval = retval.concat(win(bet.player_index, bet.bet_amount, 2))
          else
            retval = retval.concat(lose(bet.player_index, bet.bet_amount))
      retval
    }
  }
}