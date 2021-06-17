package com.example.bombgame.game.drawer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.example.bombgame.game.data.BombLiveProperties;
import com.example.bombgame.game.enumerate.PlayersPositionEnum;
import com.example.bombgame.game.modele.PlayerGridModel;
import java.util.ArrayList;
import java.util.Random;

public class PlayersDrawer extends ShapeRenderer {

  private BombLiveProperties bombLiveProperties = BombLiveProperties.getInstance();
  private ArrayList<PlayerGridModel> playersGridUnpicked = new ArrayList<>();

  public PlayersDrawer() {
    playersGridInit();
  }

  public void playersGridInit() {
    int thick = 50;

    bombLiveProperties.getPlayersGrid()
        .add(PlayersPositionEnum.GAUCHE_HAUT.ordinal(), new PlayerGridModel()
        .withPlayersPositionEnum(PlayersPositionEnum.GAUCHE_HAUT)
        .withColor(Color.BLUE)
        .withRectangle(new Rectangle(
            0,
            bombLiveProperties.getScreenHeight(),
            thick,
            -bombLiveProperties.getScreenHeight()*1/3
        )));

    bombLiveProperties.getPlayersGrid()
        .add(new PlayerGridModel()
        .withPlayersPositionEnum(PlayersPositionEnum.GAUCHE_MILIEU)
        .withColor(Color.GREEN)
        .withRectangle(new Rectangle(
            0,
            bombLiveProperties.getScreenHeight()*2/3,
            thick,
            -bombLiveProperties.getScreenHeight()*1/3
        )));

    bombLiveProperties.getPlayersGrid()
        .add(new PlayerGridModel()
        .withPlayersPositionEnum(PlayersPositionEnum.GAUCHE_BAS)
        .withColor(Color.YELLOW)
        .withRectangle(new Rectangle(
            0,
            bombLiveProperties.getScreenHeight()*1/3,
            thick,
            -bombLiveProperties.getScreenHeight()*1/3
        )));

    bombLiveProperties.getPlayersGrid()
        .add(new PlayerGridModel()
        .withPlayersPositionEnum(PlayersPositionEnum.BAS_GAUCHE)
        .withColor(Color.BROWN)
        .withRectangle(new Rectangle(
            0,
            0,
            bombLiveProperties.getScreenWidth()*1/2,
            thick
        )));

    bombLiveProperties.getPlayersGrid()
        .add(new PlayerGridModel()
        .withPlayersPositionEnum(PlayersPositionEnum.BAS_DROITE)
        .withColor(Color.MAGENTA)
        .withRectangle(new Rectangle(
            bombLiveProperties.getScreenWidth()*1/2,
            0,
            bombLiveProperties.getScreenWidth()*1/2,
            thick
        )));

    bombLiveProperties.getPlayersGrid()
        .add(new PlayerGridModel()
        .withPlayersPositionEnum(PlayersPositionEnum.DROITE_BAS)
        .withColor(Color.BLACK)
        .withRectangle(new Rectangle(
            bombLiveProperties.getScreenWidth(),
            0,
            -thick,
            bombLiveProperties.getScreenHeight()*1/3
        )));

    bombLiveProperties.getPlayersGrid()
        .add(new PlayerGridModel()
        .withPlayersPositionEnum(PlayersPositionEnum.DROITE_MILIEU)
        .withColor(Color.CYAN)
        .withRectangle(new Rectangle(
            bombLiveProperties.getScreenWidth(),
            bombLiveProperties.getScreenHeight()*1/3,
            -thick,
            bombLiveProperties.getScreenHeight()*1/3
        )));

    bombLiveProperties.getPlayersGrid()
        .add(new PlayerGridModel()
        .withPlayersPositionEnum(PlayersPositionEnum.DROITE_HAUT)
        .withColor(Color.ORANGE)
        .withRectangle(new Rectangle(
            bombLiveProperties.getScreenWidth(),
            bombLiveProperties.getScreenHeight()*2/3,
            -thick,
            bombLiveProperties.getScreenHeight()*1/3
        )));

    bombLiveProperties.getPlayersGrid()
        .add(new PlayerGridModel()
        .withPlayersPositionEnum(PlayersPositionEnum.HAUT_DROITE)
        .withColor(Color.DARK_GRAY)
        .withRectangle(new Rectangle(
            bombLiveProperties.getScreenWidth(),
            bombLiveProperties.getScreenHeight(),
            -bombLiveProperties.getScreenWidth()*1/2,
            -thick
            )));

    bombLiveProperties.getPlayersGrid()
        .add(new PlayerGridModel()
        .withPlayersPositionEnum(PlayersPositionEnum.HAUT_GAUCHE)
        .withColor(Color.NAVY)
        .withRectangle(new Rectangle(
            bombLiveProperties.getScreenWidth()*1/2,
            bombLiveProperties.getScreenHeight(),
            -bombLiveProperties.getScreenWidth(),
            -thick
        )));

    playersGridUnpicked.addAll(bombLiveProperties.getPlayersGrid());
  }

  public void refreshPlayers() {
    if (bombLiveProperties.getPlayerList() != null) {
      for (String playerId : bombLiveProperties.getPlayerList()) {
        for (PlayerGridModel playerGridModel : bombLiveProperties.getPlayersGrid()) {
          if (playerId.equals(playerGridModel.getPlayerId())) {
            drawPlayer(playerId, playerGridModel);
          }
        }
      }
    }
  }

  public void initDrawPlayers() {
    if (bombLiveProperties.getPlayerList() != null) {
      for (String playerId : bombLiveProperties.getPlayerList()) {
        drawPlayer(playerId, getRandomPlayerGridModel(playerId));
      }
    }
  }

  /**
   * Draws the bomb at its normal state
   */
  public void drawPlayer(String player, PlayerGridModel playerGridModel) {
    this.setColor(playerGridModel.getColor());
    this.begin(ShapeType.Filled);
    this.rect(
        playerGridModel.getRectangle().getX(),
        playerGridModel.getRectangle().getY(),
        playerGridModel.getRectangle().getWidth(),
        playerGridModel.getRectangle().getHeight());
    this.end();
  }

  private PlayerGridModel getRandomPlayerGridModel(String player) {
    Random random = new Random();

    // Pick player in the unpicked players list
    int size = playersGridUnpicked.size();
    int randomInt = random.nextInt(size);

    PlayerGridModel playerPicked = playersGridUnpicked
        .get(randomInt).withPlayerId(player);

    // Remove the player from the unpicked players list
    playersGridUnpicked.remove(randomInt);

    // update id in the playersGrid
    bombLiveProperties.getPlayersGrid().get(playerPicked.getPlayersPositionEnum().ordinal()).setPlayerId(player);

    return playerPicked;
  }
}
