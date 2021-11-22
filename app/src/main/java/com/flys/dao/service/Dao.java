package com.flys.dao.service;

import android.util.Log;

import com.flys.dao.db.NotificationDao;
import com.flys.dao.db.NotificationDaoImpl;
import com.flys.generictools.dao.daoException.DaoException;
import com.flys.notification.domain.Notification;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import rx.Observable;

@EBean(scope = EBean.Scope.Singleton)
public class Dao extends AbstractDao implements IDao {

    // client du service web
    @RestService
    protected WebClient webClient;
    // sécurité
    @Bean
    protected MyAuthInterceptor authInterceptor;
    // le RestTemplate
    private RestTemplate restTemplate;
    // factory du RestTemplate
    private SimpleClientHttpRequestFactory factory;

    @Bean(NotificationDaoImpl.class)
    protected NotificationDao notificationDao;

    @AfterInject
    public void afterInject() {
        // log
        Log.d(className, "afterInject");
        // on construit le restTemplate
        factory = new SimpleClientHttpRequestFactory();
        restTemplate = new RestTemplate(factory);
        // on fixe le convertisseur jSON
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        // on fixe le restTemplate du client web
        webClient.setRestTemplate(restTemplate);
    }

    @Override
    public void setUrlServiceWebJson(String url) {
        // on fixe l'URL du service web
        webClient.setRootUrl(url);
    }

    @Override
    public void setUser(String user, String mdp) {
        // on enregistre l'utilisateur dans l'intercepteur
        authInterceptor.setUser(user, mdp);
    }

    @Override
    public void setTimeout(int timeout) {
        if (isDebugEnabled) {
            Log.d(className, String.format("setTimeout thread=%s, timeout=%s", Thread.currentThread().getName(), timeout));
        }
        // configuration factory
        factory.setReadTimeout(timeout);
        factory.setConnectTimeout(timeout);
    }

    @Override
    public void setBasicAuthentification(boolean isBasicAuthentificationNeeded) {
        if (isDebugEnabled) {
            Log.d(className, String.format("setBasicAuthentification thread=%s, isBasicAuthentificationNeeded=%s", Thread.currentThread().getName(), isBasicAuthentificationNeeded));
        }
        // intercepteur d'authentification ?
        if (isBasicAuthentificationNeeded) {
            // on ajoute l'intercepteur d'authentification
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
            interceptors.add(authInterceptor);
            restTemplate.setInterceptors(interceptors);
        }
    }

    @Override
    public Observable<byte[]> downloadUrl(String url) {
        return getResponse(() -> webClient.downloadUrl(url));
    }

    @Override
    public Observable<byte[]> downloadFacebookImage(String url, String type) {
        return getResponse(() -> webClient.downloadFacebookImage(url, type));
    }

    @Override
    public Observable<byte[]> downloadFacebookProfileImage(String baseUrl, String ext, String params, String facebookAppId) {
        return getResponse(() -> webClient.downloadFacebookProfileImage(baseUrl, ext, params, facebookAppId));
    }

    // méthodes privées -------------------------------------------------
    private void log(String message) {
        if (isDebugEnabled) {
            Log.d(className, message);
        }
    }

    @Override
    public Observable<List<Notification>> loadNotificationsFromDatabase() {
        return Observable.create(subscriber -> {
            try {
                List<Notification> notifications = notificationDao.getAll();
                if (notifications != null) {
                    subscriber.onNext(notifications.stream()
                            .distinct()
                            .sorted(Comparator.comparing(Notification::getDate).reversed())
                            .collect(Collectors.toList()));
                } else {
                    subscriber.onNext(new ArrayList<>());
                }
                subscriber.onCompleted();
            } catch (DaoException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Observable<List<Notification>> loadNotificationsFromDatabase(String property, Object value) {
        return Observable.create(subscriber -> {
            try {
                List<Notification> notifications = notificationDao.findByPropertyName(property, value);
                if (notifications != null) {
                    subscriber.onNext(notifications.stream()
                            .distinct()
                            .sorted(Comparator.comparing(Notification::getDate).reversed())
                            .collect(Collectors.toList()));
                } else {
                    subscriber.onNext(new ArrayList<>());
                }
                subscriber.onCompleted();
            } catch (DaoException e) {
                e.printStackTrace();
            }
        });
    }
}
