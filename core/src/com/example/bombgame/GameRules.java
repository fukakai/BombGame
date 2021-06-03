package com.example.bombgame;

import com.badlogic.gdx.Gdx;

public class GameRules {

  BombLiveProperties bombLiveProperties = BombLiveProperties.getInstance();

  public void ruleTheWorld() {
    wantsToExplode();
    checkTime();
    checkGameOver();
  }

  /**
   * Update the game clock
   */
  private void checkTime() {
    bombLiveProperties
        .setTimeState(bombLiveProperties.getTimeState() + Gdx.graphics.getDeltaTime());
  }

  /**
   * Game Over Rules
   */
  private void checkGameOver() {
    if (bombLiveProperties.getTimeState() + BombConstants.INFLATE_TIME > bombLiveProperties
        .getEndOfGame()) {
      bombLiveProperties.setBombWantsToExplode(true);
    }
    if (bombLiveProperties.getTimeState() >= bombLiveProperties.getEndOfGame()) {
      bombLiveProperties.setGameOver(true);
      bombLiveProperties.setBombWantsToExplode(false);
    }
  }

  /**
   * Make the bomb want to explode
   */
  private void wantsToExplode() {
    if (bombLiveProperties.isBombWantsToExplode()) {
      if (bombLiveProperties.getBombWidth() >= bombLiveProperties.getBombMaxSize()) {
        bombLiveProperties.setBombShouldDeflate(true);
      }
      if (bombLiveProperties.getBombWidth() <= bombLiveProperties.getBombMinSize()) {
        bombLiveProperties.setBombShouldDeflate(false);
      }
    }
  }

}
