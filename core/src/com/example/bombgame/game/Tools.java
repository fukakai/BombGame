package com.example.bombgame.game;

public class Tools {

  public static boolean isBetween(float variable, float inclusiveValue1, float inclusiveValue2) {
    if (inclusiveValue2 >= inclusiveValue1) {
      return variable >= inclusiveValue1 && variable <= inclusiveValue2;
    }else {
      return variable >= inclusiveValue2 && variable <= inclusiveValue1;
    }
  }
}
