package com.example.bombgame.game.rules;

import com.badlogic.gdx.Gdx;
import com.example.bombgame.game.BombConstants;
import com.example.bombgame.game.data.BombLiveProperties;

public class GameRules {

  private BombLiveProperties bombLiveProperties = BombLiveProperties.getInstance();

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
        .setTimeFromBeginning(bombLiveProperties.getTimeFromBeginning() + Gdx.graphics.getDeltaTime());
  }

  /**
   * Game Over Rules
   */
  private void checkGameOver() {
    if (bombLiveProperties.getTimeFromBeginning() + BombConstants.INFLATE_TIME > bombLiveProperties
        .getEndOfGame()) {
      bombLiveProperties.setBombWantsToExplode(true);
    }
    if (bombLiveProperties.getTimeFromBeginning() >= bombLiveProperties.getEndOfGame()) {
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
