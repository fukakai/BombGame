package com.example.bombgame.game.physics;

import com.badlogic.gdx.Gdx;
import com.example.bombgame.game.BombConstants;
import com.example.bombgame.game.Tools;
import com.example.bombgame.game.data.BombLiveProperties;
import com.example.bombgame.game.modele.Coordinates;
import com.example.bombgame.game.modele.PlayerGridModel;
import com.example.bombgame.game.service.BombService;

public class BombPhysics {

  private BombLiveProperties bombLiveProperties = BombLiveProperties.getInstance();
  private BombService bombService = BombService.getInstance();

  public void animate() {
    move();
    if (!hitPlayer()) {
      bounce();
    }
    perpetualRotation();
    touchBomb();

    if (bombLiveProperties.isBombWantsToExplode()) {
      inflateOrDeflate();
    }
  }

  /**
   * Moves the Bomb when it is not touched
   */
  private void move() {
    if (!bombLiveProperties.isBombTouched()) {
      if (bombLiveProperties.isGoToRight()) {
        bombLiveProperties.setBombX(bombLiveProperties.getBombX()
            + calculateSpeed(bombLiveProperties.getDeltaXCoef()));
      } else {
        bombLiveProperties.setBombX(bombLiveProperties.getBombX()
            - calculateSpeed(bombLiveProperties.getDeltaXCoef()));
      }
      if (bombLiveProperties.isGoToUp()) {
        bombLiveProperties.setBombY(bombLiveProperties.getBombY()
            + calculateSpeed(bombLiveProperties.getDeltaYCoef()));
      } else {
        bombLiveProperties.setBombY(bombLiveProperties.getBombY()
            - calculateSpeed(bombLiveProperties.getDeltaYCoef()));
      }
      decelerate();
    }
  }

  /**
   * Calculate the speed of the bomb regarding
   * <p>- Initial Bomb Speed</p>
   * <p>- Player actions (Delta)</p>
   * <p>- Deceleration</p>
   *
   * @param delta
   * @return
   */
  private float calculateSpeed(final float delta) {
    float speed =
        (BombConstants.INITIAL_BOMB_SPEED + delta) - bombLiveProperties.getDeceleration();
    return (speed > 0) ? speed : 0;
  }

  /**
   * Continuous Deceleration
   */
  private void decelerate() {
    bombLiveProperties.setDeceleration(
        (float) (bombLiveProperties.getDeceleration() + BombConstants.DECELERATION_COEFFICIENT));
  }

  /**
   * If the bomb has just been touched by the player detect the bomb collisions with every current
   * player
   *
   * @return a boolean to indicate the bomb has hitten a player
   */
  private boolean hitPlayer() {
    if (bombLiveProperties.isHasJustBeenTouched()) {
      for (PlayerGridModel playerGridModel : bombLiveProperties.getPlayersGrid()) {
        if (playerGridModel.getPlayerId() != null) {

          if (isBombInCollisionWithAplayer(bombLiveProperties.getMiddleBottom(),
              playerGridModel)
              || isBombInCollisionWithAplayer(bombLiveProperties.getMiddleRight(),
              playerGridModel)
              || isBombInCollisionWithAplayer(bombLiveProperties.getMiddleTop(),
              playerGridModel)
              || isBombInCollisionWithAplayer(bombLiveProperties.getMiddleLeft(),
              playerGridModel)) {
            bombLiveProperties.setHasJustBeenTouched(false);
            bombService.updateBombDatas(playerGridModel.getPlayerId());
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * uses proper bomb coordinates (a dot in the middle of each side of the texture) to detect
   * collisions with the players areas (rectangles)
   *
   * @param coordinates
   * @param playerGridModel
   * @return a boolean indicating if there is a collision between the bomb and a rectlanger (player)
   */
  private boolean isBombInCollisionWithAplayer(Coordinates coordinates,
      PlayerGridModel playerGridModel) {
    return (Tools.isBetween(coordinates.getX(), playerGridModel.getRectangle().getX(),
        playerGridModel.getRectangle().getX() + playerGridModel.getRectangle().getWidth())
        && Tools.isBetween(coordinates.getY(), playerGridModel.getRectangle().getY(),
        playerGridModel.getRectangle().getY() + playerGridModel.getRectangle().getHeight()));
  }

  /**
   * Bounce the Bomb if the edges of the canvas was hitten
   */
  private void bounce() {
    if (getBombRight() >= bombLiveProperties.getScreenWidth()) {
      bombLiveProperties.setGoToRight(false);
      friction();
      rotate(false, false, true, false);
      bombLiveProperties.setHasJustBeenTouched(false);
    }
    if (getBombTop() >= bombLiveProperties.getScreenHeight()) {
      bombLiveProperties.setGoToUp(false);
      friction();
      rotate(false, true, false, false);
      bombLiveProperties.setHasJustBeenTouched(false);
    }
    if (getBombLeft() <= BombConstants.SCREEN_LEFT) {
      bombLiveProperties.setGoToRight(true);
      friction();
      rotate(true, false, false, false);
      bombLiveProperties.setHasJustBeenTouched(false);
    }
    if (getBombBottom() <= BombConstants.SCREEN_BOTTOM) {
      bombLiveProperties.setGoToUp(true);
      friction();
      rotate(false, false, false, true);
      bombLiveProperties.setHasJustBeenTouched(false);
    }
  }

  /**
   * Makes the bomb rotate
   */
  private void perpetualRotation() {
    bombLiveProperties.setRotationPosition(bombLiveProperties.getRotationPosition()
        + (Gdx.graphics.getDeltaTime() * BombConstants.ROTATION_SPEED) * bombLiveProperties
        .getRotationAxis());
  }

  /**
   * Rotates the bomb regarding its direction
   *
   * @param left   - if the bomb bounced on the left side
   * @param top    - if the bomb bounced on the top
   * @param right  - if the bomb bounced on the right side
   * @param bottom - if the bomb bounced on the bottom
   */
  private void rotate(final boolean left, final boolean top, final boolean right,
      final boolean bottom) {
    if ((left && bombLiveProperties.isGoToUp()) || (top && bombLiveProperties.isGoToRight())
        || (right && !bombLiveProperties.isGoToUp()) || (bottom && !bombLiveProperties
        .isGoToRight())) {
      bombLiveProperties.setRotationAxis(1);
    } else {
      bombLiveProperties.setRotationAxis(-1);
    }
    bombLiveProperties.setRotationPosition(bombLiveProperties.getRotationPosition()
        + BombConstants.ROTATION_CHOC * bombLiveProperties.getRotationAxis());
  }

  /**
   * Apply friction coefficient when the bomb touches the edges of the screen
   */
  private void friction() {
    bombLiveProperties.setDeceleration(
        (float) (bombLiveProperties.getDeceleration() + BombConstants.FRICTION_COEFFICIENT));
  }

  /**
   * Inflates or deflates the bomb if it wants to explode
   */
  private void inflateOrDeflate() {
    if (bombLiveProperties.isBombShouldDeflate()) {
      bombLiveProperties
          .setBombWidth(bombLiveProperties.getBombWidth() - BombConstants.BOMB_INFLATION_SPEED);
      bombLiveProperties.setBombHeight(
          bombLiveProperties.getBombHeight() - BombConstants.BOMB_INFLATION_SPEED);
    } else {
      bombLiveProperties
          .setBombWidth(bombLiveProperties.getBombWidth() + BombConstants.BOMB_INFLATION_SPEED);
      bombLiveProperties.setBombHeight(
          bombLiveProperties.getBombHeight() + BombConstants.BOMB_INFLATION_SPEED);
    }
  }

  /**
   * Make the Bomb follow the touch / finger
   */
  private void touchBomb() {
    if (Gdx.input.isTouched()) {
      bombLiveProperties.setDeceleration(0);
      bombLiveProperties.setTouchedX(Gdx.input.getX());
      bombLiveProperties
          .setTouchedY((-Gdx.input.getY()) + bombLiveProperties.getScreenHeight());

      if ((bombLiveProperties.getTouchedX() >= bombLiveProperties.getBombX()
          && bombLiveProperties.getTouchedX() <= (bombLiveProperties.getBombX()
          + bombLiveProperties.getBombWidth() + 30))
          && (bombLiveProperties.getTouchedY() >= bombLiveProperties.getBombY()
          && bombLiveProperties.getTouchedY() <= (bombLiveProperties.getBombY()
          + bombLiveProperties.getBombHeight() + 30))) {
        bombLiveProperties.setBombTouched(true);
        bombLiveProperties.setBombX(
            bombLiveProperties.getTouchedX() - bombLiveProperties.getBombWidth() / 2);
        bombLiveProperties.setBombY(
            bombLiveProperties.getTouchedY() - bombLiveProperties.getBombHeight() / 2);
      }
    } else if (bombLiveProperties.isBombTouched()) {
      bombLiveProperties.setHasJustBeenTouched(true);
      bombLiveProperties.setBombTouched(false);
    }
    deltaVelocity();
  }

  /**
   * Modify the speed of the bomb regarding the distance of the touch
   */
  private void deltaVelocity() {
    if (bombLiveProperties.isBombTouched()) {
      bombLiveProperties.setGoToRight((Gdx.input.getDeltaX() > 0) ? true : false);
      bombLiveProperties.setGoToUp((Gdx.input.getDeltaY() > 0) ? false : true);

      bombLiveProperties.setDeltaXCoef(Math.abs(Gdx.input.getDeltaX()) / 5L);
      bombLiveProperties.setDeltaYCoef(Math.abs(Gdx.input.getDeltaY()) / 5L);
    }
  }

  private float getBombRight() {
    return bombLiveProperties.getBombX() + bombLiveProperties.getBombWidth();
  }

  private float getBombTop() {
    return bombLiveProperties.getBombY() + bombLiveProperties.getBombHeight();
  }

  private float getBombLeft() {
    return bombLiveProperties.getBombX();
  }

  private float getBombBottom() {
    return bombLiveProperties.getBombY();
  }

}
