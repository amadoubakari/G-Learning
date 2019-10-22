package com.flys.architecture.core;

public class MenuItemState {

  // identifiant de l'option de menu
  private int menuItemId;
  // visibilité de l'option
  private boolean isVisible;

  // constructeurs
  public MenuItemState() {

  }

  public MenuItemState(int menuItemId, boolean isVisible) {
    this.menuItemId = menuItemId;
    this.isVisible = isVisible;
  }

  // getters et setters

  public int getMenuItemId() {
    return menuItemId;
  }

  public void setMenuItemId(int menuItemId) {
    this.menuItemId = menuItemId;
  }

  public boolean isVisible() {
    return isVisible;
  }

  public void setVisible(boolean visible) {
    isVisible = visible;
  }
}
