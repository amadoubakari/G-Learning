package com.flys.dao.service;

import com.flys.notification.domain.Notification;

import java.util.List;

import rx.Observable;

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

    Observable<byte[]> downloadUrl(String url);

    Observable<byte[]> downloadFacebookImage(String url,String type);

    //Download facebook profile image
    Observable<byte[]> downloadFacebookProfileImage(String baseUrl, String ext, String params, String facebookAppId);

    //Load notifications from the data base
    Observable<List<Notification>> loadNotificationsFromDatabase();

    //Load notifications from the data base
    Observable<List<Notification>> loadNotificationsFromDatabase(String property, Object value);
}
