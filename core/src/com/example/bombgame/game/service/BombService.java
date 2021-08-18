package com.example.bombgame.game.service;

import com.example.bombgame.game.holder.FirestoreHolder;

public class BombService {

  private static BombService singleInstance = null;
//  private BombLiveProperties bombLiveProperties = BombLiveProperties.getInstance();

  public static BombService getInstance() {
    if (singleInstance == null) {
      singleInstance = new BombService();
    }
    return singleInstance;
  }

  public void updateBombDatas(String player) {
    FirestoreHolder.getFirestoreDatabase().updateCurrentBombOwner(player);
    FirestoreHolder.getFirestoreDatabase().updateBombSpeed();
    FirestoreHolder.getFirestoreDatabase().updateTimeFromBeginning();
  }

  public void updateCurrentBombOwner(String bombOwner) {
    FirestoreHolder.getFirestoreDatabase().updateCurrentBombOwner(bombOwner);
  }

  public void setEndOfGame(int end) {
    FirestoreHolder.getFirestoreDatabase().setEndOfGame(end);
  }
}
