package com.example.bombgame.game.dto;

import java.util.HashMap;

public class Player extends HashMap<String, Object> {
  String username;
  Boolean ready = false;

  public Player(String username){
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Boolean getReady() {
    return ready;
  }

  public void setReady(Boolean ready) {
    this.ready = ready;
  }
}
