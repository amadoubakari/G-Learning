package com.flys.architecture.core;

import java.util.ArrayList;
import java.util.List;

public class Utils {

  // liste de messages d'une exception - version 1
  static public List<String> getMessagesFromException(Throwable ex) {
    // on crée une liste avec les msg d'erreur de la pile d'exceptions
    List<String> messages = new ArrayList<>();
    Throwable th = ex;
    while (th != null) {
      messages.add(th.getMessage());
      th = th.getCause();
    }
    return messages;
  }

  // liste de messages d'une exception - version 2
  static public String getMessageForAlert(Throwable th) {
    // on construit le texte à afficher
    StringBuilder texte = new StringBuilder();
    List<String> messages = getMessagesFromException(th);
    int n = messages.size();
    for (String message : messages) {
      texte.append(String.format("%s : %s\n", n, message));
      n--;
    }
    // résultat
    return texte.toString();
  }

  // liste de messages d'une exception - version 3
  static public String getMessageForAlert(List<String> messages) {
    // on construit le texte à afficher
    StringBuilder texte = new StringBuilder();
    int n = messages.size();
    for (String message : messages) {
      texte.append(String.format("%s : %s\n", n, message));
      n--;
    }
    // résultat
    return texte.toString();
  }
}
