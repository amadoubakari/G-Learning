package com.flys.architecture.custom;

import com.flys.architecture.core.ISession;
import com.flys.dao.service.IDao;

public interface IMainActivity extends IDao {

  // accès à la session
  ISession getSession();

  // changement de vue
  void navigateToView(int position, ISession.Action action);

  // gestion de l'attente
  void beginWaiting();

  void cancelWaiting();

  // constantes de l'application (à modifier) -------------------------------------

  // mode debug
  boolean IS_DEBUG_ENABLED = true;

  // délai maximal d'attente de la réponse du serveur
  int TIMEOUT = 1000;

  // délai d'attente avant exécution de la requête client
  int DELAY = 0;

  // authentification basique
  boolean IS_BASIC_AUTHENTIFICATION_NEEDED = false;

  // adjacence des fragments
  int OFF_SCREEN_PAGE_LIMIT = 1;

  // barre d'onglets
  boolean ARE_TABS_NEEDED = false;

  // image d'attente
  boolean IS_WAITING_ICON_NEEDED = false;

  // nombre de fragments de l'application
  int FRAGMENTS_COUNT = 2;

  // todo ajoutez ici vos constantes et autres méthodes
}
