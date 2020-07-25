package com.flys.config;

import com.flys.dao.entities.User;
import com.flys.notification.domain.Notification;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.Serializable;

/**
 * Created by User on 23/10/2018.
 */

public class DBConfig extends OrmLiteConfigUtil implements Serializable {

    private static final Class<?>[] classes = new Class[] {
            User.class, Notification.class
    };
    public static void main(String[] args) throws Exception {
        writeConfigFile("ormlite_config.txt", classes);
    }
}