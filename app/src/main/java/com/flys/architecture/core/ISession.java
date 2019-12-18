package com.flys.architecture.core;

import com.flys.architecture.custom.CoreState;

public interface ISession {

  // numéro de la dernière vue affichée
  int getPreviousView();

  void setPreviousView(int numView);

  // dernier état d'une vue
  CoreState getCoreState(int numView);

  void setCoreState(int numView, CoreState coreState);

  // action en cours
  enum Action {
    SUBMIT, NAVIGATION, RESTORE, NONE
  }

  Action getAction();

  void setAction(Action action);

  // états de toutes les vues -
  // pas utilisé par le code mais est nécessaire pour sérialisation / désérialisation jSON
  CoreState[] getCoreStates();

  void setCoreStates(CoreState[] coreStates);

  // n° du dernier onglet sélectionné
  int getPreviousTab();

  void setPreviousTab(int position);

  // navigation sur sélection onglet
  boolean isNavigationOnTabSelectionNeeded();

  void setNavigationOnTabSelectionNeeded(boolean navigationOnTabSelection);
}
