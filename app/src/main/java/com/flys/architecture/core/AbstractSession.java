package com.flys.architecture.core;

import com.flys.architecture.custom.CoreState;
import com.flys.architecture.custom.IMainActivity;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class AbstractSession implements ISession {
  // n° de la vue précédente
  private int preViousView;

  // état des vues
  private CoreState[] coreStates = new CoreState[0];

  // action en cours
  private Action action = Action.NONE;

  // onglet sélectionné précédemment
  private int previousTab;

  // navigation sur sélection onglet
  @JsonIgnore
  private boolean navigationOnTabSelectionNeeded = true;

  // constructeur
  public AbstractSession() {
    // on initialise le tableau des états des fragments
    coreStates = new CoreState[IMainActivity.FRAGMENTS_COUNT];
    for (int i = 0; i < coreStates.length; i++) {
      coreStates[i] = new CoreState();
    }
  }


  // interface ISession ---------------------------------------------------------
  @Override
  public int getPreviousView() {
    return preViousView;
  }

  @Override
  public void setPreviousView(int numView) {
    this.preViousView = numView;
  }

  @Override
  public CoreState getCoreState(int numView) {
    return coreStates[numView];
  }

  @Override
  public void setCoreState(int numView, CoreState coreState) {
    coreStates[numView] = coreState;
  }

  @Override
  public Action getAction() {
    return action;
  }

  @Override
  public void setAction(Action action) {
    this.action = action;
  }

  @Override
  public CoreState[] getCoreStates() {
    return coreStates;
  }

  @Override
  public void setCoreStates(CoreState[] coreStates) {
    this.coreStates = coreStates;
  }

  @Override
  public int getPreviousTab() {
    return previousTab;
  }

  @Override
  public void setPreviousTab(int position) {
    this.previousTab = position;
  }

  @Override
  public boolean isNavigationOnTabSelectionNeeded() {
    return navigationOnTabSelectionNeeded;
  }

  @Override
  public void setNavigationOnTabSelectionNeeded(boolean navigationOnTabSelectionNeeded) {
    this.navigationOnTabSelectionNeeded = navigationOnTabSelectionNeeded;
  }
}
