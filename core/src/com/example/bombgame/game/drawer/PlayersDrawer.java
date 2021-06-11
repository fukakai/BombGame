package com.example.bombgame.game.drawer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.example.bombgame.game.data.BombLiveProperties;
import java.awt.Rectangle;

public class PlayersDrawer extends ShapeRenderer {

  BitmapFont font = new BitmapFont();
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
    int y = 500;

//    this.begin();
//    for(String player : BombLiveProperties.getInstance().getPlayerList()){
//      font.draw(this,player.toString(), 300,y);
//      y+=20;
//    }
//    this.end();

    for (String player : BombLiveProperties.getInstance().getPlayerList()) {
      drawPlayer(index);
      index += 1;
    }

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
  public void drawPlayer(int index) {
    int thick = 50;

    if (index == 0) {
      this.begin(ShapeType.Filled);
      this.rect(
          0,
          BombLiveProperties.getInstance().getScreenHeight(),
          thick,
          (BombLiveProperties.getInstance().getScreenHeight())*-1);
      this.setColor(Color.GREEN);
      this.end();
    }
    if (index == 1) {
      this.begin(ShapeType.Filled);
      this.rect(
          0,
          0,
          BombLiveProperties.getInstance().getScreenWidth(),
          thick);
      this.setColor(Color.RED);
      this.end();
    }
    if (index == 2) {
      this.begin(ShapeType.Filled);
      this.rect(
          BombLiveProperties.getInstance().getScreenWidth(),
          0,
          thick*-1,
          BombLiveProperties.getInstance().getScreenHeight());
      this.setColor(Color.BLUE);
      this.end();
    }
//    if (index == 3) {
//      this.begin(ShapeType.Filled);
//      this.rect(
//          BombLiveProperties.getInstance().getScreenWidth(),
//          BombLiveProperties.getInstance().getScreenHeight(),
//          thick,
//          (BombLiveProperties.getInstance().getScreenHeight()));
//      this.setColor(Color.RED);
//      this.end();
//    }
  }
}
