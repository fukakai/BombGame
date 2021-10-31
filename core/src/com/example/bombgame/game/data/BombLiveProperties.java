package com.example.bombgame.game.data;

import com.example.bombgame.game.modele.Coordinates;
import com.example.bombgame.game.modele.PlayerGridModel;

import java.util.ArrayList;

/**
 * Singleton to be able to use and update the bomb properties from anywhere in the application
 */
public class BombLiveProperties {

  private String currentBombOwner;
  private String gameId;
  private String localPlayer;
  private ArrayList<String> playerList;
  private ArrayList<PlayerGridModel> playersGrid = new ArrayList<>();
  private boolean hasJustBeenTouched;
  private int screenWidth;
  private int screenHeight;
  private int touchedX;
  private int touchedY;
  private float deltaXCoef;
  private float deltaYCoef;
  private float bombX;
  private float bombY;
  private float bombScaleX = 1;
  private float bombScaleY = 1;
  private int bombWidth = 300;
  private float bombIconScale = 0.25f;
  private int bombIconWidth = (int) (bombWidth * bombIconScale);
  private int bombHeight = bombWidth;
  private int bombMaxSize = bombWidth + 50;
  private int bombMinSize = bombWidth - 50;
  private float deceleration = 0;
  private float timeFromBeginning = 0;
  private float rotationPosition = 0;

  private double decelerationCoefficient = 0.01;
  private float rotationAxis = -1;

  private boolean bombWantsToExplode = false;
  private boolean goToUp;
  private boolean goToRight;
  private boolean bombShouldDeflate;
  private boolean isBombTouched;
  private boolean isGameOver = false;
  private float endOfGame;

  private static BombLiveProperties singleInstance = null;

  public static BombLiveProperties getInstance() {
      if (singleInstance == null) {
          singleInstance = new BombLiveProperties();
      }
    return singleInstance;
  }

  public void reset(){
    singleInstance = null;
  }

  public int getScreenWidth() {
    return screenWidth;
  }

  public void setScreenWidth(int screenWidth) {
    this.screenWidth = screenWidth;
  }

  public int getScreenHeight() {
    return screenHeight;
  }

  public void setScreenHeight(int screenHeight) {
    this.screenHeight = screenHeight;
  }

  public int getTouchedX() {
    return touchedX;
  }

  public void setTouchedX(int touchedX) {
    this.touchedX = touchedX;
  }

  public int getTouchedY() {
    return touchedY;
  }

  public void setTouchedY(int touchedY) {
    this.touchedY = touchedY;
  }

  public final float getDeltaXCoef() {
    return deltaXCoef;
  }

  public final void setDeltaXCoef(float deltaXCoef) {
    this.deltaXCoef = deltaXCoef;
  }

  public float getDeltaYCoef() {
    return deltaYCoef;
  }

  public void setDeltaYCoef(float deltaYCoef) {
    this.deltaYCoef = deltaYCoef;
  }

  public float getBombX() {
    return bombX;
  }

  public void setBombX(float bombX) {
    this.bombX = bombX;
  }

  public float getBombY() {
    return bombY;
  }

  public void setBombY(float bombY) {
    this.bombY = bombY;
  }

  public float getBombScaleX() {
    return bombScaleX;
  }

  public void setBombScaleX(float bombScaleX) {
    this.bombScaleX = bombScaleX;
  }

  public float getBombScaleY() {
    return bombScaleY;
  }

  public void setBombScaleY(float bombScaleY) {
    this.bombScaleY = bombScaleY;
  }

  public int getBombWidth() {
    return bombWidth;
  }

  public void setBombWidth(int bombWidth) {
    this.bombWidth = bombWidth;
  }

  public int getBombHeight() {
    return bombHeight;
  }

  public void setBombHeight(int bombHeight) {
    this.bombHeight = bombHeight;
  }

  public int getBombMaxSize() {
    return bombMaxSize;
  }

  public int getBombMinSize() {
    return bombMinSize;
  }

  public float getDeceleration() {
    return deceleration;
  }

  public void setDeceleration(float deceleration) {
    this.deceleration = deceleration;
  }

  public double getDecelerationCoefficient() {
    return decelerationCoefficient;
  }

  public void setDecelerationCoefficient(double decelerationCoefficient) {
    this.decelerationCoefficient = decelerationCoefficient;
  }

  public float getTimeFromBeginning() {
    return timeFromBeginning;
  }

  public void setTimeFromBeginning(float timeFromBeginning) {
    this.timeFromBeginning = timeFromBeginning;
  }

  public float getRotationPosition() {
    return rotationPosition;
  }

  public void setRotationPosition(float rotationPosition) {
    this.rotationPosition = rotationPosition;
  }


  public float getRotationAxis() {
    return rotationAxis;
  }

  public void setRotationAxis(float rotationAxis) {
    this.rotationAxis = rotationAxis;
  }

  public boolean isBombWantsToExplode() {
    return bombWantsToExplode;
  }

  public void setBombWantsToExplode(boolean bombWantsToExplode) {
    this.bombWantsToExplode = bombWantsToExplode;
  }

  public boolean isGoToUp() {
    return goToUp;
  }

  public void setGoToUp(boolean goToUp) {
    this.goToUp = goToUp;
  }

  public boolean isGoToRight() {
    return goToRight;
  }

  public void setGoToRight(boolean goToRight) {
    this.goToRight = goToRight;
  }

  public boolean isBombShouldDeflate() {
    return bombShouldDeflate;
  }

  public void setBombShouldDeflate(boolean bombShouldDeflate) {
    this.bombShouldDeflate = bombShouldDeflate;
  }

  public boolean isBombTouched() {
    return isBombTouched;
  }

  public void setBombTouched(boolean bombTouched) {
    isBombTouched = bombTouched;
  }

  public boolean isGameOver() {
    return isGameOver;
  }

  public void setGameOver(boolean gameOver) {
    isGameOver = gameOver;
  }

  public float getEndOfGame() {
    return endOfGame;
  }

  public void setEndOfGame(float endOfGame) {
    this.endOfGame = endOfGame;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public String getLocalPlayer() {
    return localPlayer;
  }

  public void setLocalPlayer(String localPlayer) {
    this.localPlayer = localPlayer;
  }

  public boolean isHasJustBeenTouched() {
    return hasJustBeenTouched;
  }

  public void setHasJustBeenTouched(boolean hasBeenTouched) {
    this.hasJustBeenTouched = hasBeenTouched;
  }

  public ArrayList<String> getPlayerList() {
    return playerList;
  }

  public void setPlayerList(ArrayList<String> playerList) {
    this.playerList = playerList;
  }

  public ArrayList<PlayerGridModel> getPlayersGrid() {
    return playersGrid;
  }

  public void setPlayersGrid(
      ArrayList<PlayerGridModel> playersGrid) {
    this.playersGrid = playersGrid;
  }

  public String getCurrentBombOwner() {
    return currentBombOwner;
  }

  public void setCurrentBombOwner(String currentBombOwner) {
    this.currentBombOwner = currentBombOwner;
  }

  public Coordinates getMiddleRight(){
    return new Coordinates()
        .withX(bombX+bombWidth)
        .withY(bombY+bombHeight/2);
  }

  public Coordinates getMiddleLeft(){
    return new Coordinates()
        .withX(bombX)
        .withY(bombY+bombHeight/2);
  }

  public Coordinates getMiddleBottom(){
    return new Coordinates()
        .withX(bombX+bombWidth/2)
        .withY(bombY);
  }

  public Coordinates getMiddleTop(){
    return new Coordinates()
        .withX(bombX+bombWidth/2)
        .withY(bombY+bombHeight);
  }
  public float getBombIconScale() {
    return bombIconScale;
  }

  public void setBombIconScale(float bombIconScale) {
    this.bombIconScale = bombIconScale;
  }

  public int getBombIconWidth() {
    return bombIconWidth;
  }

  public void setBombIconWidth(int bombIconWidth) {
    this.bombIconWidth = bombIconWidth;
  }

}
