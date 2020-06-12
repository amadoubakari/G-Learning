package com.flys.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.flys.R;
import com.flys.architecture.core.AbstractActivity;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.core.ISession;
import com.flys.architecture.core.Utils;
import com.flys.architecture.custom.Session;
import com.flys.common_tools.dialog.MaterialNotificationDialog;
import com.flys.common_tools.domain.NotificationData;
import com.flys.common_tools.utils.FileUtils;
import com.flys.dao.db.NotificationDao;
import com.flys.dao.db.NotificationDaoImpl;
import com.flys.dao.db.UserDao;
import com.flys.dao.db.UserDaoImpl;
import com.flys.dao.entities.User;
import com.flys.dao.service.Dao;
import com.flys.dao.service.IDao;
import com.flys.fragments.behavior.AboutFragment_;
import com.flys.fragments.behavior.AlphabetFragment_;
import com.flys.fragments.behavior.ArcFragment_;
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
import com.flys.fragments.behavior.NotificationFragment_;
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
import com.flys.generictools.dao.daoException.DaoException;
import com.flys.notification.domain.Notification;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends AbstractActivity implements MaterialNotificationDialog.NotificationButtonOnclickListeneer {

    @OptionsMenuItem(R.id.connexion)
    MenuItem connexion;

    // couche [DAO]
    @Bean(Dao.class)
    protected IDao dao;

    @Bean(UserDaoImpl.class)
    protected UserDao userDao;

    @Bean(NotificationDaoImpl.class)
    protected NotificationDao notificationDao;
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
       if (getIntent().getExtras() != null) {
            Notification notification = (Notification) getIntent().getSerializableExtra("notification");
            Log.e(getClass().getSimpleName(), " notification from "+notification);
            if(notification!=null){
                try {
                    notificationDao.save(notification);
                    navigateToView(NOTIFICATION_FRAGMENT, ISession.Action.SUBMIT);
                } catch (DaoException e) {
                    Log.e(getClass().getSimpleName(), " notification exception :::  "+e);
                    Log.e(getClass().getSimpleName(), " notification message :::  "+e.getMessage());
                    Log.e(getClass().getSimpleName(), " notification cause :::  "+e.getCause());
                    e.printStackTrace();
                }

            }

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
                new AlphabetFragment_(),
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
                new HyeneFragment_(), new NotificationFragment_(), new AboutFragment_(),
        };
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
        return SPLASHSCREEN_FRAGMENT;
    }

    @Override
    protected void disconnect() {
        MaterialNotificationDialog dialog = new MaterialNotificationDialog(this, new NotificationData(getString(R.string.app_name), "Voudriez-vous vous déconnecter de l'application?", "OUI", "NON", getDrawable(R.drawable.books), R.style.Theme_MaterialComponents_DayNight_Dialog_Alert), new MaterialNotificationDialog.NotificationButtonOnclickListeneer() {
            @Override
            public void okButtonAction(DialogInterface dialogInterface, int i) {
                // Disconnection
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                try {
                                    userDao.delete(session.getUser());
                                    session.setUser(null);
                                    dialogInterface.dismiss();
                                } catch (DaoException e) {
                                    e.printStackTrace();
                                }

                            }
                            if (task.isComplete()) {

                            }
                            if (task.isCanceled()) {

                            }
                        });
            }

            @Override
            public void noButtonAction(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show(getSupportFragmentManager(), "material_notification_alert_dialog");
    }

    @Override
    public void onBackPressed() {
        drawerLayout.closeDrawers();
        if (mViewPager.getCurrentItem() == HOME_FRAGMENT) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            this.dialog = new MaterialNotificationDialog(this, new NotificationData(getString(R.string.app_name), "Voudriez-vous quitter l'application?", "OUI", "NON", getDrawable(R.drawable.books), R.style.Theme_MaterialComponents_DayNight_Dialog_Alert), this);
            this.dialog.show(getSupportFragmentManager(), "material_notification_alert_dialog");
        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }


    /**
     *
     */
    @OptionsItem(R.id.connexion)
    public void connexion() {
        createSignInIntent();
    }

    @OptionsItem(R.id.menu_profil)
    public void showProfile() {
        drawerLayout.openDrawer(Gravity.LEFT, true);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Call for authentication
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                user = getProviderData(firebaseUser);
                //
                //If there is image
                if (user.getImageUrl() != null) {
                    //Tester la connexion internet
                    if (Utils.isConnectedToNetwork(this)) {
                        downloadUrl(user.getImageUrl())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(bytes -> {
                                    FileUtils.saveToInternalStorage(bytes, "glearning", user.getNom() + ".png", this);
                                    showDialogImage(bytes);
                                }, error -> {
                                    // on affiche les messages de la pile d'exceptions du Throwable th
                                    new android.app.AlertDialog.Builder(this).setTitle("Ooops !").setMessage("Vérifiez votre connexion internet et réessayer plus tard.").setNeutralButton("Fermer", null).show();
                                });
                    } else {
                        showErrorMessage("Oops! Erreur connexion internet");
                    }
                }

                //Mise à jour des informations de l'utilisateur dans la session
                try {
                    userDao.save(this.user);
                } catch (DaoException e) {
                    e.printStackTrace();
                }
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.e(getClass().getSimpleName(), "onActivityResult: sign_in_cancelled");
                    showErrorMessage("Connexion annulée");
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.e(getClass().getSimpleName(), "onActivityResult: no_internet_connection");
                    showErrorMessage("Oops! Erreur connexion internet");
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e(getClass().getSimpleName(), "onActivityResult: unknown_error");
                    showErrorMessage("Oops! Veuillez réessayer..");
                    return;
                }
            }
        }
    }

    /**
     * @param msg
     */
    private void showErrorMessage(String msg) {
        Snackbar.make(findViewById(R.id.main_content), msg, Snackbar.LENGTH_LONG)
                .setAction("CLOSE", view -> {

                })
                .setActionTextColor(getColor(R.color.red_A700))
                .setBackgroundTint(getColor(R.color.grey_900))
                .setTextColor(getColor(R.color.white))
                .show();
    }

    @Override
    public Observable<byte[]> downloadUrl(String url) {
        return dao.downloadUrl(url);
    }

    @Override
    public User updateProfile() {
        //Check if the session content user
        if (session.getUser() != null) {
            return session.getUser();
        } else {
            //Check in the database if the user was connected
            List<User> users = null;
            try {
                users = userDao.getAll();
                if (users != null && !users.isEmpty()) {
                    session.setUser(users.get(0));
                }
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
        return session.getUser();
    }

    /**
     * Return user informations switch provider type
     *
     * @param firebaseUser
     * @return
     */
    public User getProviderData(FirebaseUser firebaseUser) {
        User user = new User();
        if (user != null) {
            for (UserInfo profile : firebaseUser.getProviderData()) {
                //switch provider
                switch (profile.getProviderId()) {
                    case "google.com":
                        user.setNom(firebaseUser.getDisplayName());
                        user.setEmail(profile.getEmail());
                        user.setImageUrl(profile.getPhotoUrl().toString().replace("s96-c", "s400-c"));
                        break;
                    case "facebook.com":
                        user.setNom(firebaseUser.getDisplayName());
                        user.setImageUrl(profile.getPhotoUrl().toString().concat("?type=large"));
                        break;
                    case "phone":
                        user.setPhone(profile.getPhoneNumber());
                        break;
                    case "password":
                        user.setEmail(profile.getEmail());
                        break;
                }

            }
        }
        return user;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (updateProfile() != null) {
            connexion.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void showDialogImage(byte[] bytes) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_contact_image);
        TextView name = dialog.findViewById(R.id.name);
        TextView email = dialog.findViewById(R.id.email_or_number);
        ImageView smallImage = dialog.findViewById(R.id.small_image);
        ImageView image = dialog.findViewById(R.id.large_image);
        smallImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        image.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        email.setText("amadoubakari@gmail.com");
        name.setText("AMADOU BAKARI");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        (dialog.findViewById(R.id.bt_close)).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void okButtonAction(DialogInterface dialogInterface, int i) {
        super.onBackPressed();
    }

    @Override
    public void noButtonAction(DialogInterface dialogInterface, int i) {
        this.dialog.dismiss();
    }
}
