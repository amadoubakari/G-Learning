package com.flys.architecture.custom;

import com.flys.architecture.core.MenuItemState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
// todo : ajouter ici les sous-classes de [CoreState]
/*@JsonSubTypes({
  @JsonSubTypes.Type(value = Class1.class),
  @JsonSubTypes.Type(value = Class2.class)}
)*/
public class CoreState {
  // fragment visité ou non
  protected boolean hasBeenVisited = false;
  // état de l'éventuel menu du fragment
  protected MenuItemState[] menuOptionsState;

  // getters et setters

  public boolean getHasBeenVisited() {
    return hasBeenVisited;
  }

  public void setHasBeenVisited(boolean hasBeenVisited) {
    this.hasBeenVisited = hasBeenVisited;
  }

  public MenuItemState[] getMenuOptionsState() {
    return menuOptionsState;
  }

  public void setMenuOptionsState(MenuItemState[] menuOptionsState) {
    this.menuOptionsState = menuOptionsState;
  }
}
