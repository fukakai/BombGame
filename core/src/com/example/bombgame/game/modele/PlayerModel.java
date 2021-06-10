package com.example.bombgame.game.modele;

public class PlayerModel {
  int id;
  String name;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public PlayerModel withId(int id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PlayerModel withName(String name) {
    this.name = name;
    return this;
  }
}
