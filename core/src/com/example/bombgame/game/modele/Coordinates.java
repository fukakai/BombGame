package com.example.bombgame.game.modele;

public class Coordinates {
  private float x;
  private float y;

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public Coordinates withX(float x) {
    this.x = x;
    return this;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public Coordinates withY(float y) {
    this.y = y;
    return this;
  }
}
