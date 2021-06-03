package com.example.bombgame;

import com.badlogic.gdx.Gdx;

public class BombPhysics {

  BombLiveProperties bombLiveProperties = BombLiveProperties.getInstance();

  public void animate() {
    move();
    bounce();
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
        bombLiveProperties.setBombX(bombLiveProperties.getBombX() + calculateSpeed(
            bombLiveProperties.getDeltaXCoef()));
      } else {
        bombLiveProperties.setBombX(bombLiveProperties.getBombX() - calculateSpeed(
            bombLiveProperties.getDeltaXCoef()));
      }
      if (bombLiveProperties.isGoToUp()) {
        bombLiveProperties.setBombY(bombLiveProperties.getBombY() + calculateSpeed(
            bombLiveProperties.getDeltaYCoef()));
      } else {
        bombLiveProperties.setBombY(bombLiveProperties.getBombY() - calculateSpeed(
            bombLiveProperties.getDeltaYCoef()));
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
  private float calculateSpeed(int delta) {
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
   * Bounce the Bomb if the edges of the canvas was hitten
   */
  private void bounce() {
    if (bombLiveProperties.getBombX() + bombLiveProperties.getBombWidth()
        >= bombLiveProperties.getScreenWidth()) {
      bombLiveProperties.setGoToRight(false);
      friction();
      rotate(false, false, true, false);
    }
    if (bombLiveProperties.getBombY() + bombLiveProperties.getBombHeight()
        >= bombLiveProperties.getScreenHeight()) {
      bombLiveProperties.setGoToUp(false);
      friction();
      rotate(false, true, false, false);
    }
    if (bombLiveProperties.getBombX() <= BombConstants.SCREEN_LEFT) {
      bombLiveProperties.setGoToRight(true);
      friction();
      rotate(true, false, false, false);
    }
    if (bombLiveProperties.getBombY() <= BombConstants.SCREEN_BOTTOM) {
      bombLiveProperties.setGoToUp(true);
      friction();
      rotate(false, false, false, true);
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
   * @param left - if the bomb bounced on the left side
   * @param top - if the bomb bounced on the top
   * @param right - if the bomb bounced on the right side
   * @param bottom - if the bomb bounced on the bottom
   */
  private void rotate(boolean left, boolean top, boolean right, boolean bottom) {
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
    }
    deltaVelocity();
    bombLiveProperties.setBombTouched(false);
  }

  /**
   * Modify the speed of the bomb regarding the distance of the touch
   */
  private void deltaVelocity() {
    if (bombLiveProperties.isBombTouched()) {
      bombLiveProperties.setGoToRight((Gdx.input.getDeltaX() > 0) ? true : false);
      bombLiveProperties.setGoToUp((Gdx.input.getDeltaY() > 0) ? false : true);

      bombLiveProperties.setDeltaXCoef(Math.abs(Gdx.input.getDeltaX()) / 5);
      bombLiveProperties.setDeltaYCoef(Math.abs(Gdx.input.getDeltaY()) / 5);
    }
  }
}
