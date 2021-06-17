package com.example.bombgame.game.service;

import com.example.bombgame.game.data.BombLiveProperties;
import com.example.bombgame.game.holder.FirestoreHolder;
import java.util.Random;

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
}
