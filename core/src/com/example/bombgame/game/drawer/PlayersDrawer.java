package com.example.bombgame.game.drawer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.example.bombgame.game.BombConstants;
import com.example.bombgame.game.data.BombLiveProperties;
import com.example.bombgame.game.enumerate.PlayersPositionEnum;
import com.example.bombgame.game.modele.PlayerGridModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayersDrawer extends ShapeRenderer {

  private BombLiveProperties bombLiveProperties = BombLiveProperties.getInstance();
  private ArrayList<PlayerGridModel> playersGridUnpicked = new ArrayList<>();
  private BombIconDrawer bombIconDrawer = new BombIconDrawer();

  public PlayersDrawer() {
    playersGridInit();
  }

  public void playersGridInit() {

    bombLiveProperties
        .getPlayersGrid()
        .add(
            PlayersPositionEnum.GAUCHE_HAUT.ordinal(),
            new PlayerGridModel(
                PlayersPositionEnum.GAUCHE_HAUT,
                Color.BLUE,
                new Rectangle(
                    0,
                    bombLiveProperties.getScreenHeight() * 2 / 3,
                    BombConstants.PLAYER_THICKNESS,
                    bombLiveProperties.getScreenHeight() / 3)));

    bombLiveProperties
        .getPlayersGrid()
        .add(
            new PlayerGridModel(
                PlayersPositionEnum.GAUCHE_MILIEU,
                Color.GREEN,
                new Rectangle(
                    0,
                    bombLiveProperties.getScreenHeight() / 3,
                    BombConstants.PLAYER_THICKNESS,
                    bombLiveProperties.getScreenHeight() / 3)));

    bombLiveProperties
        .getPlayersGrid()
        .add(
            new PlayerGridModel(
                PlayersPositionEnum.GAUCHE_BAS,
                Color.YELLOW,
                new Rectangle(
                    0,
                    0,
                    BombConstants.PLAYER_THICKNESS,
                    bombLiveProperties.getScreenHeight() / 3)));

    bombLiveProperties
        .getPlayersGrid()
        .add(
            new PlayerGridModel(
                PlayersPositionEnum.BAS_GAUCHE,
                Color.BROWN,
                new Rectangle(
                    0,
                    0,
                    bombLiveProperties.getScreenWidth() / 2,
                    BombConstants.PLAYER_THICKNESS)));

    bombLiveProperties
        .getPlayersGrid()
        .add(
            new PlayerGridModel(
                PlayersPositionEnum.BAS_DROITE,
                Color.MAGENTA,
                new Rectangle(
                    bombLiveProperties.getScreenWidth() / 2,
                    0,
                    bombLiveProperties.getScreenWidth() / 2,
                    BombConstants.PLAYER_THICKNESS)));

    bombLiveProperties
        .getPlayersGrid()
        .add(
            new PlayerGridModel(
                PlayersPositionEnum.DROITE_BAS,
                Color.BLACK,
                new Rectangle(
                    bombLiveProperties.getScreenWidth() - BombConstants.PLAYER_THICKNESS,
                    0,
                    BombConstants.PLAYER_THICKNESS,
                    bombLiveProperties.getScreenHeight() / 3)));

    bombLiveProperties
        .getPlayersGrid()
        .add(
            new PlayerGridModel(
                PlayersPositionEnum.DROITE_MILIEU,
                Color.CYAN,
                new Rectangle(
                    bombLiveProperties.getScreenWidth() - BombConstants.PLAYER_THICKNESS,
                    bombLiveProperties.getScreenHeight() / 3,
                    BombConstants.PLAYER_THICKNESS,
                    bombLiveProperties.getScreenHeight() / 3)));

    bombLiveProperties
        .getPlayersGrid()
        .add(
            new PlayerGridModel(
                PlayersPositionEnum.DROITE_HAUT,
                Color.ORANGE,
                new Rectangle(
                    bombLiveProperties.getScreenWidth() - BombConstants.PLAYER_THICKNESS,
                    bombLiveProperties.getScreenHeight() * 2 / 3,
                    BombConstants.PLAYER_THICKNESS,
                    bombLiveProperties.getScreenHeight() / 3)));

    bombLiveProperties
        .getPlayersGrid()
        .add(
            new PlayerGridModel(
                PlayersPositionEnum.HAUT_DROITE,
                Color.DARK_GRAY,
                new Rectangle(
                    bombLiveProperties.getScreenWidth() / 2,
                    bombLiveProperties.getScreenHeight() - BombConstants.PLAYER_THICKNESS,
                    bombLiveProperties.getScreenWidth() / 2,
                    BombConstants.PLAYER_THICKNESS)));

    bombLiveProperties
        .getPlayersGrid()
        .add(
            new PlayerGridModel(
                PlayersPositionEnum.HAUT_GAUCHE,
                Color.NAVY,
                new Rectangle(
                    0,
                    bombLiveProperties.getScreenHeight() - BombConstants.PLAYER_THICKNESS,
                    bombLiveProperties.getScreenWidth() / 2,
                    BombConstants.PLAYER_THICKNESS)));

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
    if (bombLiveProperties.getPlayerList() != null && bombLiveProperties.getLocalPlayer() != null) {
      List<String> otherPlayersList = bombLiveProperties.getPlayerList();
      otherPlayersList.remove(bombLiveProperties.getLocalPlayer());
      for (String playerId : otherPlayersList) {
        drawPlayer(playerId, getRandomPlayerGridModel(playerId));
      }
    }
  }

  /** Draws the bomb at its normal state */
  public void drawPlayer(String player, PlayerGridModel playerGridModel) {
    this.setColor(playerGridModel.getColor());
    this.begin(ShapeType.Filled);
    this.rect(
        playerGridModel.getRectangle().getX(),
        playerGridModel.getRectangle().getY(),
        playerGridModel.getRectangle().getWidth(),
        playerGridModel.getRectangle().getHeight());
    if (this.bombLiveProperties.getCurrentBombOwner().equals(player)) {
      bombIconDrawer.drawBombIcon(playerGridModel);
    }
    this.end();
  }

  private PlayerGridModel getRandomPlayerGridModel(String player) {
    Random random = new Random();

    // Pick player in the unpicked players list
    int size = playersGridUnpicked.size();
    int randomInt = random.nextInt(size);

    PlayerGridModel playerPicked = playersGridUnpicked.get(randomInt).withPlayerId(player);

    // Remove the player from the unpicked players list
    playersGridUnpicked.remove(randomInt);

    // update id in the playersGrid
    bombLiveProperties
        .getPlayersGrid()
        .get(playerPicked.getPlayersPosition().ordinal())
        .setPlayerId(player);

    return playerPicked;
  }
}
