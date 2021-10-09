package com.flys.architecture.custom;

import com.flys.architecture.core.ISession;
import com.flys.dao.entities.User;
import com.flys.dao.service.IDao;
import com.google.android.play.core.appupdate.AppUpdateManager;

import org.androidannotations.annotations.Background;

public interface IMainActivity extends IDao {

    // accès à la session
    ISession getSession();

    // changement de vue
    void navigateToView(int position, ISession.Action action);

    // gestion de l'attente
    void beginWaiting();

    void cancelWaiting();

    //update profile
    User updateProfile();

    // constantes de l'application (à modifier) -------------------------------------

    // mode debug
    boolean IS_DEBUG_ENABLED = false;

    // délai maximal d'attente de la réponse du serveur
    int TIMEOUT = 10000;

    // délai d'attente avant exécution de la requête client
    int DELAY = 0;

    // authentification basique
    boolean IS_BASIC_AUTHENTIFICATION_NEEDED = false;

    // adjacence des fragments
    int OFF_SCREEN_PAGE_LIMIT = 1;

    // barre d'onglets
    boolean ARE_TABS_NEEDED = false;

    // image d'attente
    boolean IS_WAITING_ICON_NEEDED = true;

    // nombre de fragments de l'application
    int FRAGMENTS_COUNT = 52;

    //Fragments number

    int ALPHABET_FRAGMENT = 0;
    int SPLASHSCREEN_FRAGMENT = 1;
    int HOME_FRAGMENT = 2;
    int FISH_FRAGMENT = 3;
    int DOG_FRAGMENT = 4;
    int FISH_AND_DOG_FRAGMENT = 5;
    int LEAF_FRAGMENT = 6;
    int CRUCHE_FRAGMENT = 7;
    int ARC_FRAGMENT = 8;
    int BOTTLE_FRAGMENT = 9;
    int PANIER_FRAGMENT = 10;
    int TORTOISE_FRAGMENT = 11;
    int MANCHE_FRAGMENT = 12;
    int KNIFE_FRAGMENT = 13;
    int SUMMARIZE_FRAGMENT = 14;
    int SHOE_FRAGMENT = 15;
    int HANGAR_FRAGMENT = 16;
    int TREE_FRAGMENT = 17;
    int SQUIRRY_FRAGMENT = 18;
    int SINGE_FRAGMENT = 19;
    int PLANTOIR_FRAGMENT = 20;
    int COQ_FRAGMENT = 21;
    int PORCEPIC_FRAGMENT = 22;
    int CAT_FRAGMENT = 23;
    int FROG_FRAGMENT = 24;
    int SHEEP_FRAGMENT = 25;
    int LANCE_FRAGMENT = 26;
    int ECHELLE_FRAGMENT = 27;
    int CALAO_FRAGMENT = 28;
    int CAMEL_FRAGMENT = 29;
    int COUTEAU_FRAGMENT = 30;
    int PINTADE_FRAGMENT = 31;
    int MORTIER_FRAGMENT = 32;
    int BOUCLIER_FRAGMENT = 33;
    int GUITAR_FRAGMENT = 34;
    int LAPIN_FRAGMENT = 35;
    int MOULIN_FRAGMENT = 36;
    int HOUSE_FRAGMENT = 37;
    int GOAT_FRAGMENT = 38;
    int CHAUVE_SOURIS_FRAGMENT = 39;
    int GRAINIER_FRAGMENT = 40;
    int QUEUE_FRAGMENT = 41;
    int GRENOUILLE_FRAGMENT = 42;
    int TERRASSE_FRAGMENT = 43;
    int MIL_FRAGMENT = 44;
    int TRONC_FRAGMENT = 45;
    int HYENE_FRAGMENT = 46;
    int NOTIFICATION_FRAGMENT = 47;
    int ABOUT_FRAGMENT = 48;
    int AUTH_FRAGMENT = 49;
    int SETTINGS_FRAGMENT = 50;

    //hide or show navigation bottom view
    void hideNavigationView(boolean hide);

    //swif fragment from this one
    boolean swiffFragment();

    //Select the default bottomviem item
    void activateMainButtonMenu(int itemId);

    //update notifications
    void updateNotificationNumber(int number);

    //add new notification on number of notifications
    void clearNotification();

    void popupSnackbarForCompleteUpdate(AppUpdateManager appUpdateManager);

    //When user scroll up the view
    void scrollUp();

    //When user scroll down the view
    void scrollDown();

}
