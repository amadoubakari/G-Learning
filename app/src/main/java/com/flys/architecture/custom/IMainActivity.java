package com.flys.architecture.custom;

import com.flys.architecture.core.ISession;
import com.flys.dao.service.IDao;

public interface IMainActivity extends IDao {

    // accès à la session
    ISession getSession();

    // changement de vue
    void navigateToView(int position, ISession.Action action);

    // gestion de l'attente
    void beginWaiting();

    void cancelWaiting();

    // constantes de l'application (à modifier) -------------------------------------

    // mode debug
    boolean IS_DEBUG_ENABLED = true;

    // délai maximal d'attente de la réponse du serveur
    int TIMEOUT = 1000;

    // délai d'attente avant exécution de la requête client
    int DELAY = 0;

    // authentification basique
    boolean IS_BASIC_AUTHENTIFICATION_NEEDED = false;

    // adjacence des fragments
    int OFF_SCREEN_PAGE_LIMIT = 1;

    // barre d'onglets
    boolean ARE_TABS_NEEDED = false;

    // image d'attente
    boolean IS_WAITING_ICON_NEEDED = false;

    // nombre de fragments de l'application
    int FRAGMENTS_COUNT = 48;

    //Fragments number
    int ALPHABET_FRAGMENT = 0;
    int HOME_FRAGMENT = 1;
    int FISH_FRAGMENT = 2;
    int DOG_FRAGMENT = 3;
    int FISH_AND_DOG_FRAGMENT = 4;
    int LEAF_FRAGMENT = 5;
    int CRUCHE_FRAGMENT = 6;
    int ARC_FRAGMENT = 7;
    int BOTTLE_FRAGMENT = 8;
    int PANIER_FRAGMENT = 9;
    int TORTOISE_FRAGMENT = 10;
    int MANCHE_FRAGMENT = 11;
    int KNIFE_FRAGMENT = 12;
    int SUMMARIZE_FRAGMENT = 13;
    int SHOE_FRAGMENT=14;
    int HANGAR_FRAGMENT=15;
    int TREE_FRAGMENT=16;
    int SQUIRRY_FRAGMENT=17;
    int SPLASHSCREEN_FRAGMENT=18;
    int SINGE_FRAGMENT=19;
    int PLANTOIR_FRAGMENT=20;
    int COQ_FRAGMENT=21;
    int PORCEPIC_FRAGMENT=22;
    int CAT_FRAGMENT=23;
    int FROG_FRAGMENT=24;
    int SHEEP_FRAGMENT=25;
    int LANCE_FRAGMENT=26;
    int ECHELLE_FRAGMENT=27;
    int CALAO_FRAGMENT=28;
    int CAMEL_FRAGMENT=29;
    int COUTEAU_FRAGMENT=30;
    int PINTADE_FRAGMENT=31;
    int MORTIER_FRAGMENT=32;
    int BOUCLIER_FRAGMENT=33;
    int GUITAR_FRAGMENT=34;
    int LAPIN_FRAGMENT=35;
    int MOULIN_FRAGMENT=36;
    int HOUSE_FRAGMENT=37;
    int GOAT_FRAGMENT=38;
    int CHAUVE_SOURIS_FRAGMENT=39;
    int GRAINIER_FRAGMENT=40;
    int QUEUE_FRAGMENT=41;
    int GRENOUILLE_FRAGMENT=42;
    int TERRASSE_FRAGMENT=43;
    int MIL_FRAGMENT=44;
    int TRONC_FRAGMENT=45;
    int HYENE_FRAGMENT=46;

    // todo ajoutez ici vos constantes et autres méthodes
    //hide or show navigation bottom view
    void hideNavigationView(boolean hide);

    //swif fragment from this one
    boolean swiffFragment();
}
