package com.example.bombgame.game;

public class BombConstants {

  private BombConstants() {
  }

  public static String BOMB_FILE = "bomb.png";
  public static String EXPLOSION_FILE = "boum.png";
  public static int BOMB_INFLATION_SPEED = 8;
  public static double DECELERATION_COEFFICIENT = 0.01;
  public static double FRICTION_COEFFICIENT = 0.07;
  public static double SCREEN_LEFT = 0;
  public static double SCREEN_BOTTOM = 0;
  public static float ROTATION_SPEED = 50;
  public static float ROTATION_CHOC = 10;
  public static float MIN_SECONDS_BEFORE_END = 7;
  public static float MAX_SECONDS_BEFORE_END = 30;
  public static float INFLATE_TIME = 7f;
  public static float INITIAL_BOMB_SPEED = 7f;
}
