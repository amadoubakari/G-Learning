package com.flys.architecture.core;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;

import com.flys.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * Test the connection
     *
     * @param context
     * @return
     */
    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    /**
     * Test if connected network is data mobile or wifi
     * if true mobile data.
     *
     * @param context
     * @return
     */
    public static boolean isMobileDataNetwork(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMetered = cm.isActiveNetworkMetered();
        return isMetered;
    }


    /**
     * @param context
     * @param view
     * @param msg
     */
    public static void showErrorMessage(Context context, View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("CLOSE", v -> {

                })
                .setActionTextColor(context.getColor(R.color.red_A700))
                .setBackgroundTint(context.getColor(R.color.grey_900))
                .setTextColor(context.getColor(R.color.white))
                .show();
    }

    /**
     * Suppression d'un fichier existant
     *
     * @param fileName
     * @param context
     */
    public static boolean fileExist(String dirName, String fileName, Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(dirName, Context.MODE_PRIVATE);
        // Create imageDir
        File file = new File(directory, fileName);
        return file.exists();
    }
}
