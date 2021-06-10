package com.example.bombgame.game.drawer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.example.bombgame.game.BombConstants;
import com.example.bombgame.game.data.BombLiveProperties;

public class BombDrawer extends SpriteBatch {

  private BombLiveProperties bombLiveProperties = BombLiveProperties.getInstance();
  private Texture bombNormalTexture;
  private Texture bombExplodedTexture;
  private TextureRegion bombTextureRegion;

  public BombDrawer() {
    bombNormalTexture = new Texture(BombConstants.BOMB_FILE);
    bombExplodedTexture = new Texture(BombConstants.EXPLOSION_FILE);
    bombTextureRegion = new TextureRegion(bombNormalTexture);
  }

  /**
   * Draws the bomb at its normal state
   */
  public void drawBomb() {
    this.begin();
    this.draw(bombTextureRegion, bombLiveProperties.getBombX(), bombLiveProperties.getBombY(),
        bombLiveProperties.getBombWidth() / 2, bombLiveProperties.getBombHeight() / 2,
        bombLiveProperties.getBombWidth(), bombLiveProperties.getBombHeight(),
        bombLiveProperties.getBombScaleX(), bombLiveProperties.getBombScaleY(),
        bombLiveProperties.getRotationPosition());
    this.end();
  }

  /**
   * Draws the explosion
   */
  public void drawExplosion() {
    this.begin();
    bombTextureRegion.setTexture(bombExplodedTexture);
    this.draw(bombTextureRegion, bombLiveProperties.getBombX(), bombLiveProperties.getBombY(),
        bombLiveProperties.getBombWidth(), bombLiveProperties.getBombHeight());
    this.end();
  }

  public Texture getBombNormalTexture() {
    return bombNormalTexture;
  }

  public Texture getBombExplosionTexture() {
    return bombExplodedTexture;
  }
}
