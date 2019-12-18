package com.flys.dao.service;

import org.androidannotations.annotations.EBean;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@EBean(scope = EBean.Scope.Singleton)
public class MyAuthInterceptor implements ClientHttpRequestInterceptor {

  // utilisateur
  private String user;
  // mot de passe
  private String mdp;

  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    // entêtes HTTP de la requête HTTP interceptée
    HttpHeaders headers = request.getHeaders();
    // l'entête HTTP d'authentification basique
    HttpAuthentication auth = new HttpBasicAuthentication(user, mdp);
    // ajout aux entêtes HTTP
    headers.setAuthorization(auth);
    // on continue le cycle de vie de la requête HTTP
    return execution.execute(request, body);
  }

  // éléments de l'authentification
  public void setUser(String user, String mdp) {
    this.user = user;
    this.mdp = mdp;
  }
}