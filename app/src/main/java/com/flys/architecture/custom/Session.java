package com.flys.architecture.custom;

import com.flys.architecture.core.AbstractSession;
import com.flys.dao.entities.User;
import com.flys.notification.domain.Notification;

import java.util.ArrayList;
import java.util.List;

public class Session extends AbstractSession {
    // données à partager entre fragments eux-mêmes et entre fragments et activité
    // les éléments qui ne peuvent être sérialisés en jSON doivent avoir l'annotation @JsonIgnore
    // ne pas oublier les getters et setters nécessaires pour la sérialisation / désérialisation jSON
    private User user;
    //Notification coming from firebase notification
    private Notification notification;
    //saved notificatiosns
    private static List<Notification> notifications=new ArrayList<>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Notification getNotification() {
        return this.notification;
    }

    public static List<Notification> getNotifications() {
        return notifications;
    }

    public static void setNotifications(List<Notification> notifs) {
        notifications = notifs;
    }
}
