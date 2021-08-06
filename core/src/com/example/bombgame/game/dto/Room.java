package com.example.bombgame.game.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Room extends HashMap<String, Object> {
  String bombState = "";
  String currentBombOwner = "";
  Float deltaXCoef = 7F;
  Float deltaYCoef = 7F;
  Float endOfGame = 0F;
  Float timeFromBeginning = 0F;
  String gameId = "";
//  ArrayList<Player> playerList = new ArrayList<>();

  public String getBombState() {
    return bombState;
  }

  public void setBombState(String bombState) {
    this.bombState = bombState;
  }

  public String getCurrentBombOwner() {
    return currentBombOwner;
  }

  public void setCurrentBombOwner(String currentBombOwner) {
    this.currentBombOwner = currentBombOwner;
  }

  public Float getDeltaXCoef() {
    return deltaXCoef;
  }

  public void setDeltaXCoef(Float deltaXCoef) {
    this.deltaXCoef = deltaXCoef;
  }

  public Float getDeltaYCoef() {
    return deltaYCoef;
  }

  public void setDeltaYCoef(Float deltaYCoef) {
    this.deltaYCoef = deltaYCoef;
  }

  public Float getEndOfGame() {
    return endOfGame;
  }

  public void setEndOfGame(Float endOfGame) {
    this.endOfGame = endOfGame;
  }

  public Float getTimeFromBeginning() {
    return timeFromBeginning;
  }

  public void setTimeFromBeginning(Float timeFromBeginning) {
    this.timeFromBeginning = timeFromBeginning;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

//  public ArrayList<Player> getPlayerList() {
//    return playerList;
//  }
//
//  public void setPlayerList(ArrayList<Player> playerList) {
//    this.playerList = playerList;
//  }
}

//
//class Room : HashMap<String, Any>() {
//    var bombState: String = ""
//    var currentBombOwner: String = "Romain"
//    var deltaXCoef: Number = 7
//    var deltaYCoef: Number = 7
//    var endOfGame: Number = 0
//    var timeFromBeginning: Number = 0
//    var gameId: String = "YGQBCC";
//    var playerList: MutableList<Player> = ArrayList()
//    }
//

//data class Room (
//    var bombState: String = "",
//    var currentBombOwner: String = "Romain",
//    var deltaXCoef: Number = 7,
//    var deltaYCoef: Number = 7,
//    var endOfGame: Number = 0,
//    var timeFromBeginning: Number = 0,
//    va