package com.example.bombgame.game.drawer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.example.bombgame.game.BombConstants;
import com.example.bombgame.game.data.BombLiveProperties;
import com.example.bombgame.game.modele.PlayerGridModel;

public class BombIconDrawer extends SpriteBatch {

    private BombLiveProperties bombLiveProperties = BombLiveProperties.getInstance();
    private Texture bombNormalTexture;
    private TextureRegion bombTextureRegion;

    public BombIconDrawer() {
        bombNormalTexture = new Texture(BombConstants.BOMB_FILE);
        bombTextureRegion = new TextureRegion(bombNormalTexture);
    }

    public void drawBombIcon(PlayerGridModel playerGridModel) {
        this.begin();
    this.draw(
        bombTextureRegion,
        playerGridModel.getBombIconX(),
        playerGridModel.getBombIconY(),
        bombLiveProperties.getBombWidth() * bombLiveProperties.getBombIconScale() / 2,
        bombLiveProperties.getBombHeight() * bombLiveProperties.getBombIconScale() / 2,
        bombLiveProperties.getBombWidth() * bombLiveProperties.getBombIconScale(),
        bombLiveProperties.getBombHeight() * bombLiveProperties.getBombIconScale(),
        1,
        1,
        0);
        this.end();
    }

}
