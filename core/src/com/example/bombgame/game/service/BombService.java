package com.example.bombgame.game.service;

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

  public void updateBombProperties() {
    FirestoreHolder.getFirestoreDatabase().updateBombDatas("It works!"+ new Random());
//    bombGateway.updateBombProperties(bombLiveProperties);
  }
}
