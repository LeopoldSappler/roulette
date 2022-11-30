package Roulette.controller

import Roulette.model.{Player, PlayerUpdate}
import Roulette.util.{Command, UndoManager}

class PutCommand(player_update: PlayerUpdate) extends Command[Vector[Player]] {
  override def noStep(players: Vector[Player]): Vector[Player] = players
  override def doStep(players: Vector[Player]): Vector[Player] = {
    var updated_money: Int = 0
    if (player_update.add == true)
      updated_money = players(player_update.player_index).getAvailableMoney() + player_update.money
    else
      updated_money = players(player_update.player_index).getAvailableMoney() - player_update.money
    player_update.controller.updatePlayer(player_update.player_index, updated_money)
  }
  override def undoStep(players: Vector[Player]): Vector[Player] = {
    var updated_money = 0
    if (player_update.add == true)
      updated_money = players(player_update.player_index).getAvailableMoney() - player_update.money
    else
      updated_money = players(player_update.player_index).getAvailableMoney() + player_update.money
    player_update.controller.updatePlayer(player_update.player_index, updated_money)
  }
  override def redoStep(players: Vector[Player]): Vector[Player] = {
    var updated_money = 0
    if (player_update.add == true)
      updated_money = players(player_update.player_index).getAvailableMoney() + player_update.money
    else
      updated_money = players(player_update.player_index).getAvailableMoney() - player_update.money
    player_update.controller.updatePlayer(player_update.player_index, updated_money)
  }
}