package com.example.bombgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.Random;


public class BombGame extends ApplicationAdapter {

  BombLiveProperties bombLiveProperties = BombLiveProperties.getInstance();
  GameDrawer gameDrawer;
  BombPhysics bombPhysics;
  GameRules gameRules;
  Random random = new Random();


  @Override
  public void create() {
    gameDrawer = new GameDrawer();

    bombLiveProperties.setScreenWidth(Gdx.graphics.getWidth());
    bombLiveProperties.setScreenHeight(Gdx.graphics.getHeight());

    bombPhysics = new BombPhysics();
    gameRules = new GameRules();

    randomStart();
    randomEnd();
  }

  @Override
  public void render() {
    ScreenUtils.clear(Color.WHITE);
    gameDrawer.drawBomb();

    if (!bombLiveProperties.isGameOver()) {
      bombPhysics.animate();
      gameRules.ruleTheWorld();
    } else {
      gameDrawer.drawExplosion();
    }
  }

  @Override
  public void dispose() {
    gameDrawer.dispose();
    gameDrawer.getBombNormalTexture().dispose();
    gameDrawer.getBombExplosionTexture().dispose();
    bombLiveProperties.reset();
  }

  /**
   * Give random directions to start
   */
  private void randomStart() {
    bombLiveProperties.setBombX(
        bombLiveProperties.getScreenWidth() / 2 - bombLiveProperties.getBombWidth() / 2);
    bombLiveProperties.setBombY(
        bombLiveProperties.getScreenHeight() / 2 - bombLiveProperties.getBombHeight() / 2);

    bombLiveProperties.setGoToUp(random.nextBoolean());
    bombLiveProperties.setGoToRight(random.nextBoolean());
  }

  /**
   * Random time before game over
   */
  private void randomEnd() {
    bombLiveProperties.setEndOfGame(BombConstants.MIN_SECONDS_BEFORE_END + random.nextFloat() * (
        BombConstants.MAX_SECONDS_BEFORE_END - BombConstants.MIN_SECONDS_BEFORE_END));
  }
}
