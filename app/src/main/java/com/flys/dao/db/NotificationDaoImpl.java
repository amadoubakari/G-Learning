package com.flys.dao.db;

import android.util.Log;

import com.flys.R;
import com.flys.architecture.custom.DApplicationContext;
import com.flys.generictools.dao.daoImpl.GenericDaoImpl;
import com.flys.generictools.dao.db.DatabaseHelper;
import com.flys.notification.domain.Notification;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;

@EBean(scope = EBean.Scope.Singleton)
public class NotificationDaoImpl extends GenericDaoImpl<Notification, Long> implements NotificationDao {

    DatabaseHelper<Notification, Long> databaseHelper;

    @Override
    public Dao<Notification, Long> getDao() {
        databaseHelper = new DatabaseHelper(DApplicationContext.getContext(), R.raw.ormlite_config);
        try {
            return (Dao<Notification, Long>) databaseHelper.getDao(getEntityClassManaged());
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), "Getting Dao Exception!", e);
        }

        return null;
    }

    @Override
    public void flush() {
        if(databaseHelper.isOpen()){
            databaseHelper.close();
        }
    }

    @Override
    public Class<Notification> getEntityClassManaged() {
        return Notification.class;
    }
}
