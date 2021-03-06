package com.example.bombgame.game.holder;

public class FirestoreHolder {

  private static AndroidFirestoreInterface firestoreDatabase;

  public static void setFirestoreDatabase(AndroidFirestoreInterface androidFirestoreInterface) {
    firestoreDatabase = androidFirestoreInterface;
  }

  public static AndroidFirestoreInterface getFirestoreDatabase() {
    return firestoreDatabase;
  }

  public interface AndroidFirestoreInterface {
    void updateCurrentBombOwner(String username);
    void updateBombSpeed();
    void updateTimeFromBeginning();
    void setEndOfGame(int end);
  }
}
