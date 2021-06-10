package com.example.bombgame.game.drawer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.example.bombgame.game.data.BombLiveProperties;
import java.awt.Rectangle;

public class PlayersDrawer extends ShapeRenderer {

  private Rectangle rectangle;
  private BombLiveProperties bombLiveProperties = BombLiveProperties.getInstance();
  private Texture playersTexture;

  public PlayersDrawer() {
  }

  /**
   * Draws the bomb at its normal state
   */
  public void drawPlayers() {
    int index = 0;
//    for(PlayerModel playerModel : playerModels){
//      if(playerModels.size() < 4){
//        playerModel.s
//        if(playerModels.size() < 4){
//      index++;
//    }
//    drawPlayer();
  }

  /**
   * Draws the bomb at its normal state
   */
  public void drawPlayer() {
    this.begin();
    rectangle = new Rectangle(0, 0, 30, 10);
    this.rect((float) rectangle.getX(), (float) rectangle.getY(), (float) rectangle.getWidth(),
        bombLiveProperties.getScreenHeight());
    this.setColor(Color.BLUE);
    this.end();
  }
}
