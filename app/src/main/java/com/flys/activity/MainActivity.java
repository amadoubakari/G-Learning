package com.flys.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.flys.R;
import com.flys.architecture.core.AbstractActivity;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.core.ISession;
import com.flys.architecture.core.Utils;
import com.flys.architecture.custom.DApplicationContext;
import com.flys.architecture.custom.Session;
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
import com.flys.fragments.behavior.SettingsFragment_;
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
import com.flys.tools.dialog.MaterialNotificationDialog;
import com.flys.tools.domain.NotificationData;
import com.flys.tools.utils.FileUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FileDownloadTask;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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
    private String TAG = getClass().getSimpleName();

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
        getSupportActionBar().hide();
        bottomNavigationView.setVisibility(View.GONE);
        //If we have fcm pushed notification in course
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("notification")) {
            Notification notification = (Notification) bundle.getSerializable("notification");
            Log.e(getClass().getSimpleName(), " notification from " + notification);
            if (notification != null) {
                try {
                    notification.setDate(new Date());
                    notificationDao.save(notification);
                    getSupportActionBar().show();
                    updateNotificationNumber(1);
                    activateMainButtonMenu(R.id.bottom_menu_me);
                } catch (DaoException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e(getClass().getSimpleName(), "onCreateActivity(): notification null");
            }

        } else {
            Log.e(getClass().getSimpleName(), "onCreateActivity(): bundle null");
        }
        //Subscription on firebase to receive notifications
        if (!session.isSubscribed()) {
            FirebaseMessaging.getInstance().subscribeToTopic("glearning")
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            session.setSubscribed(true);
                            Log.e(TAG, "subscription to the channel for notification is successfully");
                        }
                    });
        }
    }

    @Override
    protected void onResumeActivity() {
        //Update view if user has been connected
        if (updateProfile() != null) {
            updateUserConnectedProfile(updateProfile());        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        //Toast.makeText(this, "bundle: " + bundle, Toast.LENGTH_LONG).show();
        if (bundle != null && bundle.containsKey("notification")) {
            //Toast.makeText(this, "bundle containt key notification : " + bundle, Toast.LENGTH_LONG).show();
            //Log.e(getClass().getSimpleName(), " notification onNewIntent bundle " + bundle);
            Notification notification = (Notification) bundle.getSerializable("notification");
            //Log.e(getClass().getSimpleName(), " notification from " + notification);
            if (notification != null) {
                try {
                    notification.setDate(new Date());
                    notificationDao.save(notification);
                    getSupportActionBar().show();
                    updateNotificationNumber(1);
                    activateMainButtonMenu(R.id.bottom_menu_me);
                    //navigateToView(NOTIFICATION_FRAGMENT, ISession.Action.SUBMIT);
                } catch (DaoException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e(getClass().getSimpleName(), " onNewIntent(): notification null " );
            }

        } else {
            Log.e(getClass().getSimpleName(), " onNewIntent(): bundle null " );
        }
        this.setIntent(intent);
    }

    @Override
    protected IDao getDao() {
        return dao;
    }

    @Override
    protected AbstractFragment[] getFragments() {
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
                new SettingsFragment_()
        };
    }

    @Override
    protected CharSequence getFragmentTitle(int position) {
        return null;
    }

    @Override
    protected void navigateOnTabSelected(int position) {
        //navigation par onglets - définir la vue à afficher lorsque l'onglet n° [position] est sélectionné
    }

    @Override
    protected int getFirstView() {
        //définir le n° de la première vue (fragment) à afficher
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
                                MaterialNotificationDialog notificationDialog = new MaterialNotificationDialog(MainActivity.this, new NotificationData(getString(R.string.app_name), "Merci " + (session.getUser().getNom() != null ? session.getUser().getNom() : "") + ", vous revoir bientôt !", "OK", "Annuler", getDrawable(R.drawable.books), R.style.Theme_MaterialComponents_DayNight_Dialog_Alert), new MaterialNotificationDialog.NotificationButtonOnclickListeneer() {
                                    @Override
                                    public void okButtonAction(DialogInterface dialogInterface, int i) {
                                        try {
                                            userDao.delete(session.getUser());
                                            session.setUser(null);
                                            dialogInterface.dismiss();
                                            onPrepareOptionsMenu(null);
                                            updateUserConnectedProfile(null);
                                        } catch (DaoException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void noButtonAction(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                notificationDialog.show(getSupportFragmentManager(), "material_notification_alert_dialog");
                            }
                            if (task.isComplete()) {

                            }
                            if (task.isCanceled()) {
                                Utils.showErrorMessage(MainActivity.this, findViewById(R.id.main_content), "Déconnexion annulée");
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

    @OptionsItem(R.id.settings)
    public void showSettings() {
        navigateToView(SETTINGS_FRAGMENT, ISession.Action.SUBMIT);
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
        );

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setAuthMethodPickerLayout(customLayout)
                        .setLogo(R.drawable.books)      // Set logo drawable
                        .setTheme(R.style.AuthenticationTheme)      // Set theme
                        /*.setTosAndPrivacyPolicyUrls(
                                "https://example.com/terms.html",
                                "https://example.com/privacy.html")*/
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
                //Mise à jour des informations de l'utilisateur dans la session
                getProviderData(firebaseUser);
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.e(getClass().getSimpleName(), "onActivityResult: sign_in_cancelled");
                    Utils.showErrorMessage(MainActivity.this, findViewById(R.id.main_content), "Connexion annulée");
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.e(getClass().getSimpleName(), "onActivityResult: no_internet_connection");
                    Utils.showErrorMessage(MainActivity.this, findViewById(R.id.main_content), "Oops! Erreur connexion internet");
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e(getClass().getSimpleName(), "onActivityResult: unknown_error");
                    Utils.showErrorMessage(MainActivity.this, findViewById(R.id.main_content), "Oops! Veuillez réessayer..");
                    return;
                }
            }
        }
    }


    @Override
    public Observable<byte[]> downloadUrl(String url) {
        return dao.downloadUrl(url);
    }

    @Override
    public Observable<byte[]> downloadFacebookImage(String url, String type) {
        return dao.downloadFacebookImage(url, type);
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

    @Override
    public void activateMainButtonMenu(int itemMenuId) {
        bottomNavigationView.setSelectedItemId(itemMenuId);
    }

    @Override
    public void updateNotificationNumber(int number) {
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.bottom_menu_me);
        badgeDrawable.setBackgroundColor(getColor(R.color.red_700));
        badgeDrawable.setNumber(number);
        badgeDrawable.setMaxCharacterCount(2);
    }

    @Override
    public void clearNotification() {
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.bottom_menu_me);
        badgeDrawable.setVisible(false);
    }

    /**
     * Return user informations switch provider type
     *
     * @param firebaseUser
     * @return
     */
    public void getProviderData(FirebaseUser firebaseUser) {
        User user = new User();
        for (UserInfo profile : firebaseUser.getProviderData()) {
            //switch provider
            switch (profile.getProviderId()) {
                case "google.com":
                    user.setType(User.Type.GOOGLE);
                    user.setNom(firebaseUser.getDisplayName());
                    user.setEmail(profile.getEmail());
                    user.setImageUrl(profile.getPhotoUrl().toString().replace("s96-c", "s400-c"));
                    if (Utils.isConnectedToNetwork(this)) {
                        downloadUrl(user.getImageUrl())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(bytes -> {
                                    FileUtils.saveToInternalStorage(bytes, "glearning", user.getNom() + ".png", this);
                                    //Update profile
                                    updateUserConnectedProfile(user);
                                    showDialogImage(bytes, user);
                                }, error -> {
                                    // on affiche les messages de la pile d'exceptions du Throwable th
                                    new android.app.AlertDialog.Builder(this).setTitle("Ooops !").setMessage("Vérifiez votre connexion internet et réessayer plus tard.").setNeutralButton("Fermer", null).show();
                                });
                    } else {
                        Utils.showErrorMessage(MainActivity.this, findViewById(R.id.main_content), "Oops! Erreur connexion internet");
                    }
                    break;
                case "facebook.com":
                    user.setType(User.Type.FACEBOOK);
                    user.setNom(firebaseUser.getDisplayName());
                    user.setImageUrl(profile.getPhotoUrl().toString());
                    if (Utils.isConnectedToNetwork(this)) {
                        downloadFacebookImage(user.getImageUrl(), "large")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(bytes -> {
                                    FileUtils.saveToInternalStorage(bytes, "glearning", user.getNom() + ".png", this);
                                    //Update profile
                                    updateUserConnectedProfile(user);
                                    showDialogImage(bytes, user);
                                }, error -> {
                                    // on affiche les messages de la pile d'exceptions du Throwable th
                                    new android.app.AlertDialog.Builder(this).setTitle("Ooops !").setMessage("Vérifiez votre connexion internet et réessayer plus tard.").setNeutralButton("Fermer", null).show();
                                });
                    } else {
                        Utils.showErrorMessage(MainActivity.this, findViewById(R.id.main_content), "Oops! Erreur connexion internet");
                    }
                    break;
                case "phone":
                    user.setType(User.Type.PHONE);
                    user.setPhone(profile.getPhoneNumber());
                    showDialogImage(null, user);
                    break;
                case "password":
                    user.setType(User.Type.MAIL);
                    user.setEmail(profile.getEmail());
                    showDialogImage(null, user);
                    break;
                default:
                    break;
            }

        }
        //Mise à jour de la base de données
        try {
            userDao.save(user);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (updateProfile() != null) {
            connexion.setVisible(false);
        } else {
            connexion.setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * @param bytes
     * @param user
     */
    private void showDialogImage(byte[] bytes, User user) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_contact_image);
        TextView name = dialog.findViewById(R.id.name);
        TextView email = dialog.findViewById(R.id.email_or_number);
        ImageView smallImage = dialog.findViewById(R.id.small_image);
        ImageView image = dialog.findViewById(R.id.large_image);

        switch (user.getType()) {
            case GOOGLE:
                email.setText(user.getEmail());
                name.setText(user.getNom());
                smallImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                image.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                break;
            case FACEBOOK:
                email.setText(user.getNom());
                name.setText(user.getNom());
                smallImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                image.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                break;
            case MAIL:
                email.setText(user.getEmail());
                name.setText(user.getNom());
                break;
            case PHONE:
                email.setText(user.getPhone());
                name.setText(user.getNom());
                break;
        }
        if (bytes != null) {
            smallImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            image.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }
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

    /**
     * @param user
     */
    void updateUserConnectedProfile(User user) {
        View headerNavView = navigationView.getHeaderView(0);
        CircleImageView profile = headerNavView.findViewById(R.id.profile_image);
        TextView title = headerNavView.findViewById(R.id.profile_user_name);
        TextView mail = headerNavView.findViewById(R.id.profile_user_email_address);
        MenuItem disconnect = navigationView.getMenu().findItem(R.id.menu_deconnexion);
        //Si l'utilisateur est connecte?
        if (user != null) {
            disconnect.setVisible(true);
            switch (user.getType()) {
                case GOOGLE:
                    title.setText(user.getNom());
                    mail.setText(user.getEmail());
                    profile.setImageDrawable(user.getImageUrl() != null ? FileUtils.loadImageFromStorage("glearning", user.getNom() + ".png", DApplicationContext.getContext()) : getDrawable(R.drawable.baseline_account_circle_white_48dp));
                    break;
                case FACEBOOK:
                    title.setText(user.getEmail());
                    mail.setText(user.getNom());
                    profile.setImageDrawable(user.getImageUrl() != null ? FileUtils.loadImageFromStorage("glearning", user.getNom() + ".png", DApplicationContext.getContext()) : getDrawable(R.drawable.baseline_account_circle_white_48dp));
                    break;
                case MAIL:
                    title.setText(user.getNom());
                    mail.setText(user.getEmail());
                    profile.setImageDrawable(getDrawable(R.drawable.baseline_account_circle_white_48dp));
                    break;
                case PHONE:
                    title.setText(user.getNom());
                    mail.setText(user.getPhone());
                    profile.setImageDrawable(getDrawable(R.drawable.baseline_account_circle_white_48dp));
                    break;
            }

        } else {
            disconnect.setVisible(false);
            title.setText("Username");
            mail.setText("Email");
            profile.setImageDrawable(getDrawable(R.drawable.baseline_account_circle_white_48dp));
        }
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    File saveFile() {
        // Access your app's directory in the device's Public documents directory
        File docs = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "glearning");
// Make the directory if it does not yet exist
        if (!docs.exists()) {
            docs.mkdirs();
        }
        Log.e(getClass().getSimpleName(), "docs directory created : " + docs.getAbsolutePath());
        return docs;
    }

}
