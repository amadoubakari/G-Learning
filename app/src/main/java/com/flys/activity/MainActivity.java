package com.flys.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.flys.R;
import com.flys.architecture.core.AbstractActivity;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.core.ISession;
import com.flys.architecture.custom.Session;
import com.flys.common_tools.dialog.MaterialNotificationDialog;
import com.flys.common_tools.domain.NotificationData;
import com.flys.common_tools.utils.FileUtils;
import com.flys.dao.entities.User;
import com.flys.dao.service.Dao;
import com.flys.dao.service.IDao;
import com.flys.fragments.behavior.AlphabetFragment_;
import com.flys.fragments.behavior.ArcFragment_;
import com.flys.fragments.behavior.AuthenticationFragment_;
import com.flys.fragments.behavior.BottleFragment_;
import com.flys.fragments.behavior.BouclierFragment_;
import com.flys.fragments.behavior.CalaoFragment_;
import com.flys.fragments.behavior.CamelFragment_;
import com.flys.fragments.behavior.CatFragment_;
import com.flys.fragments.behavior.ChauveSourisFragment_;
import com.flys.fragments.behavior.CoqFragment_;
import com.flys.fragments.behavior.CouteauFragment_;
import com.flys.fragments.behavior.CrucheFragment_;
import com.flys.fragments.behavior.DogFragment_;
import com.flys.fragments.behavior.EchelleFragment_;
import com.flys.fragments.behavior.FishDogFragment_;
import com.flys.fragments.behavior.FishFragment_;
import com.flys.fragments.behavior.FrogFragment_;
import com.flys.fragments.behavior.GoatFragment_;
import com.flys.fragments.behavior.GrainierFragment_;
import com.flys.fragments.behavior.GrenouilleFragment_;
import com.flys.fragments.behavior.GuitarFragment_;
import com.flys.fragments.behavior.HangarFragment_;
import com.flys.fragments.behavior.HomeFragment_;
import com.flys.fragments.behavior.HouseFragment_;
import com.flys.fragments.behavior.HyeneFragment_;
import com.flys.fragments.behavior.KnifeFragment_;
import com.flys.fragments.behavior.LanceFragment_;
import com.flys.fragments.behavior.LapinFragment_;
import com.flys.fragments.behavior.LeafFragment_;
import com.flys.fragments.behavior.MancheFragment_;
import com.flys.fragments.behavior.MilFragment_;
import com.flys.fragments.behavior.MortierFragment_;
import com.flys.fragments.behavior.MoulinFragment_;
import com.flys.fragments.behavior.PanierFragment_;
import com.flys.fragments.behavior.PintadeFragment_;
import com.flys.fragments.behavior.PlantoirFragment_;
import com.flys.fragments.behavior.PorcEpicFragment_;
import com.flys.fragments.behavior.QueueFragment_;
import com.flys.fragments.behavior.SheepFragment_;
import com.flys.fragments.behavior.ShoeFragment_;
import com.flys.fragments.behavior.SingeFragment_;
import com.flys.fragments.behavior.SplashScreenFragment_;
import com.flys.fragments.behavior.SquirryFragment_;
import com.flys.fragments.behavior.SummarizeFragment_;
import com.flys.fragments.behavior.TerrasseFragment_;
import com.flys.fragments.behavior.TortoiseFragment_;
import com.flys.fragments.behavior.TreeFragment_;
import com.flys.fragments.behavior.TroncFragment_;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends AbstractActivity implements MaterialNotificationDialog.NotificationButtonOnclickListeneer {

    //
    @OptionsMenuItem(R.id.deconnexion)
    MenuItem deconnexion;
    @OptionsMenuItem(R.id.connexion)
    MenuItem connexion;

    // couche [DAO]
    @Bean(Dao.class)
    protected IDao dao;
    // session
    private Session session;
    //Notification
    private MaterialNotificationDialog dialog;
    //Connected user
    private User user;

    private static final int RC_SIGN_IN = 123;

    // méthodes classe parent -----------------------
    @Override
    protected void onCreateActivity() {
        // log
        if (IS_DEBUG_ENABLED) {
            Log.d(className, "onCreateActivity");
        }
        // session
        this.session = (Session) super.session;
        // todo : on continue les initialisations commencées par la classe parent
    }

    @Override
    protected void onResumeActivity() {
        if (getIntent().hasExtra("notification")) {
            navigateToView(10, ISession.Action.SUBMIT);
        }
    }

    @Override
    protected IDao getDao() {
        return dao;
    }

    @Override
    protected AbstractFragment[] getFragments() {
        // todo : définir les fragments ici
        return new AbstractFragment[]{
                new SplashScreenFragment_(), new HomeFragment_(), new FishFragment_(),
                new DogFragment_(), new FishDogFragment_(), new LeafFragment_(),
                new CrucheFragment_(), new ArcFragment_(), new BottleFragment_(),
                new PanierFragment_(), new TortoiseFragment_(), new MancheFragment_(),
                new KnifeFragment_(), new SummarizeFragment_(), new ShoeFragment_(),
                new HangarFragment_(), new TreeFragment_(), new SquirryFragment_(),
                new SingeFragment_(), new PlantoirFragment_(), new CoqFragment_(),
                new PorcEpicFragment_(), new CatFragment_(), new FrogFragment_(),
                new SheepFragment_(), new LanceFragment_(), new EchelleFragment_(),
                new CalaoFragment_(), new CamelFragment_(), new CouteauFragment_(),
                new PintadeFragment_(), new MortierFragment_(), new BouclierFragment_(),
                new GuitarFragment_(), new LapinFragment_(), new MoulinFragment_(),
                new HouseFragment_(), new GoatFragment_(), new ChauveSourisFragment_(),
                new GrainierFragment_(), new QueueFragment_(), new GrenouilleFragment_(),
                new TerrasseFragment_(), new MilFragment_(), new TroncFragment_(),
                new HyeneFragment_(), new AlphabetFragment_(), new AuthenticationFragment_()};
    }

    @Override
    protected CharSequence getFragmentTitle(int position) {
        // todo : définir les titres des fragments ici
        return null;
    }

    @Override
    protected void navigateOnTabSelected(int position) {
        // todo : navigation par onglets - définir la vue à afficher lorsque l'onglet n° [position] est sélectionné
    }

    @Override
    protected int getFirstView() {
        // todo : définir le n° de la première vue (fragment) à afficher
        return 0;
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0 || mViewPager.getCurrentItem() == 1) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            this.dialog = new MaterialNotificationDialog(this, new NotificationData(getString(R.string.app_name), "Voudriez-vous quitter l'application?", "OUI", "NON", getDrawable(R.drawable.books), R.style.Theme_MaterialComponents_DayNight_Dialog_Alert), this);
            this.dialog.show(getSupportFragmentManager(), "material_notification_alert_dialog");
        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void okButtonAction() {
        super.onBackPressed();
    }

    @Override
    public void noButtonAction() {
        this.dialog.dismiss();
    }

    @OptionsItem(R.id.connexion)
    public void connexion() {
        createSignInIntent();
    }

    @OptionsItem(R.id.deconnexion)
    public void deconnexion() {
        // on navigue vers la vue 2
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    // ...
                    connexion.setVisible(true);
                    deconnexion.setVisible(false);
                });
    }

    /**
     * Authentication using firebase: login
     */
    public void createSignInIntent() {
        // Choose authentication providers
        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.fragment_login)
                .setGoogleButtonId(R.id.connexion_google_id)
                .setEmailButtonId(R.id.connexion_mail_id)
                .setFacebookButtonId(R.id.connexion_facebook_id)
                .setPhoneButtonId(R.id.connexion_phone_id)
                .build();

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
                //new AuthUI.IdpConfig.TwitterBuilder().build()
        );

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setAuthMethodPickerLayout(customLayout)
                        .setLogo(R.drawable.books)      // Set logo drawable
                        .setTheme(R.style.AuthenticationTheme)      // Set theme
                        .setTosAndPrivacyPolicyUrls(
                                "https://example.com/terms.html",
                                "https://example.com/privacy.html")
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(getClass().getSimpleName(), "user before :" + requestCode + " resultCode : " + resultCode);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.e(getClass().getSimpleName(), "user :" + user.getDisplayName());
                Log.e(getClass().getSimpleName(), "email :" + user.getEmail());
                Log.e(getClass().getSimpleName(), "phone :" + user.getPhoneNumber());
                Log.e(getClass().getSimpleName(), "photo :" + user.getPhotoUrl());
                this.user = new User(user.getDisplayName(), user.getEmail());
                if (user.getPhotoUrl() != null) {
                    downloadUrl(user.getPhotoUrl().toString()).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bytes -> {
                                FileUtils.saveToInternalStorage(bytes, "glearning", user.getDisplayName() + ".png", this);
                            });
                }

                //Mise à jour des informations de l'utilisateur dans la session
                session.setUser(this.user);
                connexion.setVisible(false);
                deconnexion.setVisible(true);
                // ...
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.e(getClass().getSimpleName(), "onActivityResult: sign_in_cancelled");
                    showErrorMessage("To bad... It seems like you cancelled :/");
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.e(getClass().getSimpleName(), "onActivityResult: no_internet_connection");
                    showErrorMessage("Ups! There's no internet connection");
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e(getClass().getSimpleName(), "onActivityResult: unknown_error");
                    showErrorMessage("Woot! Something unexpected just happend.");
                    return;
                }
            }
        }
    }

    private void showErrorMessage(String s) {
        Snackbar.make(findViewById(R.id.main_content), "This is main activity", Snackbar.LENGTH_LONG)
                .setAction("CLOSE", view -> {

                })
                .setActionTextColor(getColor(R.color.red_A700))
                .show();
    }

    @Override
    public Observable<byte[]> downloadUrl(String url) {
        return dao.downloadUrl(url);
    }

    @Override
    public User updateProfile() {
        return session.getUser();
    }
    // [END auth_fui_result]
}
