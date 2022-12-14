package Roulette


import Roulette.aview.GUI
import Roulette.aview.TUI
import Roulette.model.Player
import Roulette.controller.Controller

import javax.swing.plaf.TextUI
import scala.io.StdIn.readLine
import scala.util.Random

@main def main(): Unit =
    println("Welcome to Roulette! \n")
    val playerCount : Int = readLine("How many players are playing? >>>" ).toInt
    val startingMoney : Int = readLine("How much money should each player start with? >>>$" ).toInt
    val controller = Controller(playerCount, startingMoney)

    val tui = TUI(controller)
    val gui = GUI(controller)
