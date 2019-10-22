package com.flys.dao.service;

public interface IDao {
  // Url du service web
  void setUrlServiceWebJson(String url);

  // utilisateur
  void setUser(String user, String mdp);

  // timeout du client
  void setTimeout(int timeout);

  // authentification basique
  void setBasicAuthentification(boolean isBasicAuthentificationNeeded);

  // mode debug
  void setDebugMode(boolean isDebugEnabled);

  // délai d'attente en millisecondes du client avant requête
  void setDelay(int delay);

  // todo : déclarez votre interface ici
}
