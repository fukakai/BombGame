package com.example.bombgame.game.modele;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.example.bombgame.game.enumerate.PlayersPositionEnum;

public class PlayerGridModel {

  private PlayersPositionEnum playersPosition;
  private Rectangle rectangle;
  private Color color;
  private String playerId;

  private float bombIconX;
  private float bombIconY;
  private float bombIconOffset;

  public PlayerGridModel(PlayersPositionEnum playersPosition, Color color, Rectangle rectangle) {
    this.playersPosition = playersPosition;
    this.color = color;
    this.rectangle = rectangle;
    bombIconInit();
  }

  public PlayersPositionEnum getPlayersPosition() {
    return playersPosition;
  }

  public PlayerGridModel withPlayersPositionEnum(PlayersPositionEnum playersPositionEnum) {
    this.playersPosition = playersPositionEnum;
    return this;
  }

  private void bombIconInit() {
    switch (playersPosition) {
      case BAS_DROITE:
      case BAS_GAUCHE:
        bombIconOffset = (float) (rectangle.getHeight());
        bombIconX = rectangle.getX() + rectangle.getWidth() / 2 - bombIconOffset;
        bombIconY = rectangle.getY() + rectangle.getHeight() * 2;
        break;
      case DROITE_BAS:
      case DROITE_HAUT:
      case DROITE_MILIEU:
        bombIconOffset = (float) (rectangle.getWidth());
        bombIconX = rectangle.getX() - rectangle.getWidth() - bombIconOffset;
        bombIconY = rectangle.getY() + rectangle.getHeight() / 2 - bombIconOffset;
        break;
      case GAUCHE_BAS:
      case GAUCHE_HAUT:
      case GAUCHE_MILIEU:
        bombIconOffset = (float) (rectangle.getWidth());
        bombIconX = rectangle.getX() + rectangle.getWidth() + bombIconOffset;
        bombIconY = rectangle.getY() + rectangle.getHeight() / 2 - bombIconOffset;
        break;
      case HAUT_DROITE:
      case HAUT_GAUCHE:
        bombIconOffset = (float) (rectangle.getHeight());
        bombIconX = rectangle.getX() + rectangle.getWidth() / 2 - bombIconOffset;
        bombIconY = rectangle.getY() - rectangle.getHeight() * 2;
    }
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

  public float getBombIconX() {
    return bombIconX;
  }

  public void setBombIconX(float bombIconX) {
    this.bombIconX = bombIconX;
  }

  public float getBombIconY() {
    return bombIconY;
  }

  public void setBombIconY(float bombIconY) {
    this.bombIconY = bombIconY;
  }

  @Override
  public String toString() {
    return playersPosition.ordinal()
        + " - "
        + rectangle.getX()
        + " / "
        + rectangle.getY()
        + " / "
        + rectangle.getWidth()
        + " / "
        + rectangle.getHeight()
        + " - "
        + color
        + " - "
        + playerId;
  }
}
