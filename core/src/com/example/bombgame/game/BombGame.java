package com.example.bombgame.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.example.bombgame.game.data.BombLiveProperties;
import com.example.bombgame.game.drawer.BombDrawer;
import com.example.bombgame.game.drawer.PlayersDrawer;
import com.example.bombgame.game.physics.BombPhysics;
import com.example.bombgame.game.rules.GameRules;
import com.example.bombgame.game.service.BombService;

import java.util.Random;

public class BombGame extends ApplicationAdapter {

  private BombLiveProperties bombLiveProperties = BombLiveProperties.getInstance();
  private BombService bombService = BombService.getInstance();
  private BombDrawer bombDrawer;
  private PlayersDrawer playersDrawer;
  private BombPhysics bombPhysics;
  private GameRules gameRules;
  private Random random = new Random();

  public BombGame() {}

  @Override
  public void create() {
    bombLiveProperties.setScreenWidth(Gdx.graphics.getWidth());
    bombLiveProperties.setScreenHeight(Gdx.graphics.getHeight());

    bombDrawer = new BombDrawer();
    bombPhysics = new BombPhysics();
    gameRules = new GameRules();
    playersDrawer = new PlayersDrawer();
    playersDrawer.initDrawPlayers();

    randomStart();
    randomEnd();
  }

  @Override
  public void render() {
    ScreenUtils.clear(Color.WHITE);
    // TODO: No NPE anymore but code is UGLY

    // Update le currentBombOwner pour le début de partie.
    if (bombLiveProperties.getCurrentBombOwner() != null
        && bombLiveProperties.getCurrentBombOwner().equals("")
        && bombLiveProperties.getPlayerList() != null) {
      String randomPlayer =
          bombLiveProperties
              .getPlayerList()
              .get(random.nextInt(bombLiveProperties.getPlayerList().size()));
      bombLiveProperties.setCurrentBombOwner(randomPlayer);
      bombService.updateCurrentBombOwner(bombLiveProperties.getCurrentBombOwner());
    }

    if ((bombLiveProperties.getCurrentBombOwner() != null
            && bombLiveProperties.getLocalPlayer() != null)
        && (bombLiveProperties.getCurrentBombOwner().equals(bombLiveProperties.getLocalPlayer()))) {
      bombDrawer.drawBomb();
      if (!bombLiveProperties.isGameOver()) {
        bombPhysics.animate();
      } else {
        bombDrawer.drawExplosion();
      }
    }
    playersDrawer.refreshPlayers();
    gameRules.ruleTheWorld();
  }

  @Override
  public void dispose() {
    bombDrawer.dispose();
    bombDrawer.getBombNormalTexture().dispose();
    bombDrawer.getBombExplosionTexture().dispose();
    bombLiveProperties.reset();
  }

  /** Give random directions to start */
  private void randomStart() {
    bombLiveProperties.setBombX(
        bombLiveProperties.getScreenWidth() / 2 - bombLiveProperties.getBombWidth() / 2);
    bombLiveProperties.setBombY(
        bombLiveProperties.getScreenHeight() / 2 - bombLiveProperties.getBombHeight() / 2);

    bombLiveProperties.setGoToUp(random.nextBoolean());
    bombLiveProperties.setGoToRight(random.nextBoolean());
  }

  /** Random time before game over */
  private void randomEnd() {
    bombLiveProperties.setEndOfGame(
        BombConstants.MIN_SECONDS_BEFORE_END
            + random.nextFloat()
                * (BombConstants.MAX_SECONDS_BEFORE_END - BombConstants.MIN_SECONDS_BEFORE_END));
  }
}
