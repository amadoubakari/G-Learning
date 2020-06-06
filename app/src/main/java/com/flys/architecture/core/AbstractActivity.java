package com.flys.architecture.core;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flys.R;
import com.flys.architecture.custom.CustomTabLayout;
import com.flys.architecture.custom.DApplicationContext;
import com.flys.architecture.custom.IMainActivity;
import com.flys.architecture.custom.Session;
import com.flys.common_tools.dialog.AbstractDialogActivity;
import com.flys.common_tools.dialog.AbstractDialogFragmentInterface;
import com.flys.common_tools.utils.DepthPageTransformer;
import com.flys.common_tools.utils.FileUtils;
import com.flys.common_tools.utils.Utils;
import com.flys.dao.entities.User;
import com.flys.dao.service.IDao;
import com.flys.notification.domain.Notification;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.androidannotations.annotations.EApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class AbstractActivity extends AppCompatActivity implements IMainActivity, AbstractDialogFragmentInterface {
    // couche [DAO]
    private IDao dao;
    // la session
    protected ISession session;

    // le conteneur des fragments
    protected MyPager mViewPager;
    // la barre d'outils
    private Toolbar toolbar;
    // l'image d'attente
    private ProgressBar loadingPanel;
    // barre d'onglets
    protected TabLayout tabLayout;

    // le gestionnaire de fragments ou sections
    private FragmentPagerAdapter mSectionsPagerAdapter;
    // nom de la classe
    protected String className;
    // mappeur jSON
    private ObjectMapper jsonMapper;
    //la fenetre de navigation
    protected DrawerLayout drawerLayout;
    //Action sur l'icone du menu principal
    private ActionBarDrawerToggle actionBarDrawerToggle;
    //Bottom navigation view
    private BottomNavigationView bottomNavigationView;
    //image du profil
    private CircleImageView profile;

    // constructeur
    public AbstractActivity() {
        // nom de la classe
        className = getClass().getSimpleName();
        // log
        if (IS_DEBUG_ENABLED) {
            Log.d(className, "constructeur");
        }
        // jsonMapper
        jsonMapper = new ObjectMapper();

    }

    // implémentation IMainActivity --------------------------------------------------------------------
    @Override
    public ISession getSession() {
        return session;
    }

    @Override
    public void navigateToView(int position, ISession.Action action) {
        if (IS_DEBUG_ENABLED) {
            Log.d(className, String.format("navigation vers vue %s sur action %s", position, action));
        }
        //

        // affichage nouveau fragment
        mViewPager.setCurrentItem(position);
        mViewPager.setScrollingEnabled(true);
        // on note l'action en cours lors de ce changement de vue
        session.setAction(action);
    }

    // gestion sauvegarde / restauration de l'activité ------------------------------------
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // parent
        super.onSaveInstanceState(outState);
        // sauvegarde session sous la forme d'une chaîne jSON
        try {
            outState.putString("session", jsonMapper.writeValueAsString(session));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // log
        if (IS_DEBUG_ENABLED) {
            try {
                Log.d(className, String.format("onSaveInstanceState session=%s", jsonMapper.writeValueAsString(session)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // parent
        super.onCreate(savedInstanceState);
        // log
        if (IS_DEBUG_ENABLED) {
            Log.d(className, "onCreate");
        }
        // qq chose à restaurer ?
        if (savedInstanceState != null) {
            // récupération session
            try {
                session = jsonMapper.readValue(savedInstanceState.getString("session"), new TypeReference<Session>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            // log
            if (IS_DEBUG_ENABLED) {
                try {
                    Log.d(className, String.format("onCreate session=%s", jsonMapper.writeValueAsString(session)));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // session
            session = new Session();
        }
        // couche [DAO]
        dao = getDao();
        if (dao != null) {
            // configuration de la couche [DAO]
            setDebugMode(IS_DEBUG_ENABLED);
            setTimeout(TIMEOUT);
            setDelay(DELAY);
            setBasicAuthentification(IS_BASIC_AUTHENTIFICATION_NEEDED);
        }
        // vue associée
        setContentView(R.layout.activity_main);
        // composants de la vue ---------------------
        // barre d'outils
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.main_content);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // image d'attente ?
        if (IS_WAITING_ICON_NEEDED) {
            // on ajoute l'image d'attente
            if (IS_DEBUG_ENABLED) {
                Log.d(className, "adding loadingPanel");
            }
            // création ProgressBar
            loadingPanel = new ProgressBar(this);
            loadingPanel.setVisibility(View.INVISIBLE);
            // ajout du ProgressBar à la barre d'outils
            toolbar.addView(loadingPanel);
        }
        // barre d'onglets ?
        if (ARE_TABS_NEEDED) {
            // on ajoute la barre d'onglets
            if (IS_DEBUG_ENABLED) {
                Log.d(className, "adding tablayout");
            }
            // pas de navigation sur sélection jusqu'à l'affichage d'un fragment
            session.setNavigationOnTabSelectionNeeded(false);
            // création barre d'onglets
            tabLayout = new CustomTabLayout(this);
            tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_text));
            // ajout de la barre d'onglets à la barre d'application
            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
            appBarLayout.addView(tabLayout);
            // gestionnaire d'évt de la barre d'onglets
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    // un onglet a été sélectionné
                    if (IS_DEBUG_ENABLED) {
                        Log.d(className, String.format("onTabSelected n° %s, action=%s, tabCount=%s isNavigationOnTabSelectionNeeded=%s",
                                tab.getPosition(), session.getAction(), tabLayout.getTabCount(), session.isNavigationOnTabSelectionNeeded()));
                    }
                    if (session.isNavigationOnTabSelectionNeeded()) {
                        // position de l'onglet
                        int position = tab.getPosition();
                        // mémoire
                        session.setPreviousTab(position);
                        // affichage fragment associé ?
                        navigateOnTabSelected(position);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
        // instanciation du gestionnaire de fragments
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // le conteneur de fragments est associé au gestionnaire de fragments
        // ç-à-d que le fragment n° i du conteneur de fragments est le fragment n° i délivré par le gestionnaire de fragments
        mViewPager = findViewById(R.id.container);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // on inhibe le swipe entre fragments
        mViewPager.setSwipeEnabled(true);
        // adjacence des fragments
        mViewPager.setOffscreenPageLimit(OFF_SCREEN_PAGE_LIMIT);
        // qu'on associe à notre gestionnaire de fragments
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // on affiche la 1ère vue
        if (session.getAction() == ISession.Action.NONE) {
            navigateToView(getFirstView(), ISession.Action.NONE);
        }

        //Action sur les éléments de menu
        //Navigation drawer
        NavigationView navigationView = findViewById(R.id.navigation);
        //Navigation drawer
        View headerNavView = navigationView.getHeaderView(0);

        //Nous appliquons le même style aux éléments de menu
        Utils.applyFontStyleToMenu(this, navigationView.getMenu(), "fonts/libre_franklin_thin.ttf");

        profile = headerNavView.findViewById(R.id.profile_image);
        TextView title = headerNavView.findViewById(R.id.profile_user_name);
        TextView mail = headerNavView.findViewById(R.id.profile_user_email_address);
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    // close drawer when item is tapped
                    switch (menuItem.getItemId()) {
                        case R.id.menu_alphabet:
                            navigateToView(ALPHABET_FRAGMENT, ISession.Action.SUBMIT);
                            break;
                        case R.id.menu_home:
                            navigateToView(HOME_FRAGMENT, ISession.Action.SUBMIT);
                            break;
                        case R.id.menu_recommander:
                            showEditDialog();
                            break;
                        case R.id.about:
                            navigateToView(ABOUT_FRAGMENT, ISession.Action.SUBMIT);
                        default:
                            break;
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                User user = updateProfile();
                if (user != null) {
                    title.setText(user.getNom());
                    mail.setText(user.getEmail());
                    profile.setImageDrawable(FileUtils.loadImageFromStorage("glearning", user.getNom() + ".png", DApplicationContext.getContext()));
                }

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        //Action listener on bottom navigation view
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.bottom_menu_home:
                    navigateToView(HOME_FRAGMENT, ISession.Action.SUBMIT);
                    break;
                case R.id.bottom_menu_book:
                    navigateToView(FISH_FRAGMENT, ISession.Action.SUBMIT);
                    break;
                case R.id.bottom_menu_me:
                    navigateToView(NOTIFICATION_FRAGMENT, ISession.Action.SUBMIT);
                    break;
            }
            return true;
        });
        //Badge on bottom navigation menu
        bottomNavigationView.getOrCreateBadge(R.id.bottom_menu_me).setNumber(10);
        //Check if the user device has google play services installed and if not install them
        onCreateActivity();
    }

    @Override
    public void onResume() {
        // parent
        super.onResume();
        if (IS_DEBUG_ENABLED) {
            Log.d(className, "onResume");
        }
        // si restauration, alors il faut restaurer le dernier onglet sélectionné
        if (ARE_TABS_NEEDED && session.getAction() == ISession.Action.RESTORE) {
            tabLayout.getTabAt(session.getPreviousTab()).select();
        }
        //getIntent().putExtra("notification","notification");
        onResumeActivity();
        // todo : on continue les initialisations commencées par la classe parent
    }

    // gestion de l'image d'attente ---------------------------------
    public void cancelWaiting() {
        if (loadingPanel != null) {
            loadingPanel.setVisibility(View.INVISIBLE);
        }
    }

    public void beginWaiting() {
        if (loadingPanel != null) {
            loadingPanel.setVisibility(View.VISIBLE);
        }
    }


    // interface IDao -----------------------------------------------------
    @Override
    public void setUrlServiceWebJson(String url) {
        dao.setUrlServiceWebJson(url);
    }

    @Override
    public void setUser(String user, String mdp) {
        dao.setUser(user, mdp);
    }

    @Override
    public void setTimeout(int timeout) {
        dao.setTimeout(timeout);
    }

    @Override
    public void setBasicAuthentification(boolean isBasicAuthentificationNeeded) {
        dao.setBasicAuthentification(isBasicAuthentificationNeeded);
    }

    @Override
    public void setDebugMode(boolean isDebugEnabled) {
        dao.setDebugMode(isDebugEnabled);
    }

    @Override
    public void setDelay(int delay) {
        dao.setDelay(delay);
    }

    //Gestion du menu principal
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Récupération du texte issu de la boite de dialogue
    @Override
    public void receivedDate(String data) {
        Utils.shareText(this, "Dubun GUIZIGA", data + "  https://play.google.com/store/apps/details?id=com.flys.glearning", "Recommandation de l'application.");
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AbstractDialogActivity dialogActivity = new AbstractDialogActivity("Recommandation", R.mipmap.ic_launcher, R.style.AlertDialogTheme, R.style.BodyTextStyle);
        dialogActivity.show(fm, "fragment_edit_name");
    }


    // le gestionnaire de fragments --------------------------------
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        AbstractFragment[] fragments;

        // constructeur
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            // fragments de la classe fille
            fragments = getFragments();
        }

        // doit rendre le fragment n° i avec ses éventuels arguments
        @Override
        public AbstractFragment getItem(int position) {
            // on rend le fragment
            return fragments[position];
        }

        // rend le nombre de fragments à gérer
        @Override
        public int getCount() {
            return fragments.length;
        }

        // rend le titre du fragment n° position
        @Override
        public CharSequence getPageTitle(int position) {
            return getFragmentTitle(position);
        }
    }

    @Override
    public void hideNavigationView(boolean hide) {
        if (hide) {
            bottomNavigationView.setVisibility(View.GONE);
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    // classes filles
    protected abstract void onCreateActivity();

    protected abstract void onResumeActivity();

    protected abstract IDao getDao();

    protected abstract AbstractFragment[] getFragments();

    protected abstract CharSequence getFragmentTitle(int position);

    protected abstract void navigateOnTabSelected(int position);

    protected abstract int getFirstView();

    @Override
    public boolean swiffFragment() {
        return true;
    }
}
