package com.example.bombgame.game.modele;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.example.bombgame.game.enumerate.PlayersPositionEnum;

public class PlayerGridModel {

  private PlayersPositionEnum playersPositionEnum;
  private Rectangle rectangle;
  private Color color;
  private String playerId;

  public PlayersPositionEnum getPlayersPositionEnum() {
    return playersPositionEnum;
  }

  public PlayerGridModel withPlayersPositionEnum(
      PlayersPositionEnum playersPositionEnum) {
    this.playersPositionEnum = playersPositionEnum;
    return this;
  }

  public Rectangle getRectangle() {
    return rectangle;
  }

  public PlayerGridModel withRectangle(Rectangle rectangle) {
    this.rectangle = rectangle;
    return this;
  }

  public Color getColor() {
    return color;
  }

  public PlayerGridModel withColor(Color color) {
    this.color = color;
    return this;
  }

  public String getPlayerId() {
    return playerId;
  }

  public PlayerGridModel withPlayerId(String playerId) {
    this.playerId = playerId;
    return this;
  }
  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  @Override
  public String toString(){
    return playersPositionEnum.ordinal()
        + " - " + rectangle.getX()+ " / " + rectangle.getY()+ " / " + rectangle.getWidth()+ " / " + rectangle.getHeight()
        + " - " + color
        + " - " + playerId;
  }
}
