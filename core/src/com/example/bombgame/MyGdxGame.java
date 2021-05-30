package com.example.bombgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;


public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture bombNormalTexture;
    int screenWidth;
    int screenHeight;
    int touchedX;
    int touchedY;
    int deltaXCoef;
    int deltaYCoef;
    Random random = new Random();

    float bombX;
    float bombY;
    int bombWidth = 300;
    int bombHeight = bombWidth;
    int bombMaxSize = bombWidth + 50;
    int bombMinSize = bombWidth - 50;
    int bombInflationSpeed = 8;
    int initialBombSpeed = 5;
    float deceleration = 0;
    double decelerationCoefficient = 0.01;
    double frictionCoefficient = 0.07;
    double screenLeft = 0;
    double screenBottom = 0;
    float inflateTime = 10f;
    float timeState;

    boolean doesBombWantToExplode = false;
    boolean goToUp;
    boolean goToRight;
    boolean bombShouldDeflate;
    boolean isBombTouched;
    boolean isGameOver = false;
    float endOfGame;


    @Override
    public void create() {
        batch = new SpriteBatch();
        bombNormalTexture = new Texture("bomb.png");

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        randomStart();
        randomEnd();
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.WHITE);
        batch.begin();
        batch.draw(bombNormalTexture, bombX, bombY, bombWidth, bombHeight);
        batch.end();

        if(!isGameOver) {
            move();
            bounce();
            wantToExplode();
            touchBomb();
            checkTime();
            checkGameOver();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        bombNormalTexture.dispose();
    }

    /**
     * Check the game time at every tick
     */
    private void checkTime() {
        timeState += Gdx.graphics.getDeltaTime();
    }

    /**
     * Game Over Actions
     */
    private void checkGameOver() {
        if (timeState + inflateTime > endOfGame) {
            doesBombWantToExplode = true;
        }
        if (timeState >= endOfGame) {
            isGameOver = true;
            doesBombWantToExplode = false;
            bombNormalTexture = new Texture("boum.png");
            batch.begin();
            batch.draw(bombNormalTexture, bombX, bombY, bombWidth, bombHeight);
            batch.end();
        }
    }

    /**
     * Move the Bomb
     */
    private void move() {
        if (!isBombTouched) {
            if (goToRight) {
                bombX += calculateSpeed(deltaXCoef);
            } else {
                bombX -= calculateSpeed(deltaXCoef);
            }
            if (goToUp) {
                bombY += calculateSpeed(deltaYCoef);
            } else {
                bombY -= calculateSpeed(deltaYCoef);
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
        float speed = (initialBombSpeed + delta) - deceleration;
        return (speed > 0) ? speed : 0;
    }

    /**
     * Continuous Deceleration
     */
    private void decelerate() {
        deceleration += decelerationCoefficient;
    }

    /**
     * Bounce the Bomb if the edges of the canvas was hitten
     */
    private void bounce() {
        if (bombX + bombWidth >= screenWidth) {
            goToRight = false;
            friction();
        }
        if (bombY + bombHeight >= screenHeight) {
            goToUp = false;
            friction();
        }
        if (bombX <= screenLeft) {
            goToRight = true;
            friction();
        }
        if (bombY <= screenBottom) {
            goToUp = true;
            friction();
        }
    }

    /**
     * Apply friction coefficient when the bomb touches the edges of the screen
     */
    private void friction() {
        deceleration += frictionCoefficient;
    }

    /**
     * Make the bomb want to explode
     */
    private void wantToExplode() {
        if (doesBombWantToExplode) {
            if (bombWidth >= bombMaxSize) {
                bombShouldDeflate = true;
            }
            if (bombWidth <= bombMinSize) {
                bombShouldDeflate = false;
            }
            inflateOrDeflate();
        }
    }

    /**
     * Inflates or deflates the bomb if it wants to explode
     */
    private void inflateOrDeflate() {
        if (bombShouldDeflate) {
            bombWidth -= bombInflationSpeed;
            bombHeight -= bombInflationSpeed;
        } else {
            bombWidth += bombInflationSpeed;
            bombHeight += bombInflationSpeed;
        }
    }

    /**
     * Make the Bomb follow the touch / finger
     */
    private void touchBomb() {
        if (Gdx.input.isTouched()) {
            deceleration = 0;
            touchedX = Gdx.input.getX();
            touchedY = (-Gdx.input.getY()) + screenHeight;

            if ((touchedX >= bombX && touchedX <= (bombX + bombWidth + 30))
                    && (touchedY >= bombY && touchedY <= (bombY + bombHeight + 30))) {
                isBombTouched = true;
                bombX = touchedX - bombWidth / 2;
                bombY = touchedY - bombHeight / 2;
            }
        }
        deltaVelocity();
        isBombTouched = false;
    }

    /**
     * Modify the speed of the bomb regarding the distance of the touch
     */
    private void deltaVelocity() {
        if (isBombTouched) {
            goToRight = (Gdx.input.getDeltaX() > 0) ? true : false;
            goToUp = (Gdx.input.getDeltaY() > 0) ? false : true;

            deltaXCoef = Math.abs(Gdx.input.getDeltaX()) / 5;
            deltaYCoef = Math.abs(Gdx.input.getDeltaY()) / 5;
        }
    }

    /**
     * Give random directions to start
     */
    private void randomStart() {
        bombX = screenWidth / 2 - bombWidth / 2;
        bombY = screenHeight / 2 - bombHeight / 2;

        goToUp = random.nextBoolean();
        goToRight = random.nextBoolean();
    }

    /**
     * Random time before game over
     */
    private void randomEnd() {
        endOfGame = 15 + random.nextFloat() * (45 - 15);
    }
}
