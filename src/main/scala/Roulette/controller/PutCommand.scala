package Roulette.controller

import Roulette.model.{Player, PlayerUpdate}
import Roulette.util.{Command, UndoManager}

class PutCommand(player_update: PlayerUpdate, controller: Controller) extends Command {
  override def doStep(): Unit = {
    var updated_money: Int = 0
    if (player_update.add == true)
      updated_money = controller.players(player_update.player_index).getAvailableMoney() + player_update.money
    else
      updated_money = controller.players(player_update.player_index).getAvailableMoney() - player_update.money
    controller.players = controller.updatePlayer(player_update.player_index, updated_money)
  }
  override def undoStep(): Unit = {
    var updated_money: Int = 0
    if (player_update.add == true)
      updated_money = controller.players(player_update.player_index).getAvailableMoney() - player_update.money
    else
      updated_money = controller.players(player_update.player_index).getAvailableMoney() + player_update.money
    controller.players = controller.updatePlayer(player_update.player_index, updated_money)
  }
  override def redoStep(): Unit = {
    var updated_money: Int = 0
    if (player_update.add == true)
      updated_money = controller.players(player_update.player_index).getAvailableMoney() + player_update.money
    else
      updated_money = controller.players(player_update.player_index).getAvailableMoney() - player_update.money
    controller.players = controller.updatePlayer(player_update.player_index, updated_money)
  }
}