package com.flys.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.flys.tools.utils.CommonToolsConstants;
import com.flys.tools.utils.FacebookProfile;
import com.flys.tools.utils.FacebookUrl;
import com.flys.tools.utils.FileUtils;
import com.flys.utils.Constants;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.messaging.FirebaseMessaging;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;

import java.util.Arrays;
import java.util.Date;
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
    private String TAG = getClass().getSimpleName();
    // Register Callback - Call this in your app start!
    private ObjectMapper objectMapper;


    // méthodes classe parent -----------------------
    @Override
    protected void onCreateActivity() {
        // log
        if (IS_DEBUG_ENABLED) {
            Log.d(className, "onCreateActivity");
        }
        // session
        this.session = (Session) super.session;
        getSupportActionBar().hide();
        bottomNavigationView.setVisibility(View.GONE);
        //If we have fcm pushed notification in course
        handleNotifications(getIntent());
        //Subscription on firebase to receive notifications
        if (!session.isSubscribed()) {
            FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.firebase_subscription))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            session.setSubscribed(true);
                            Log.e(TAG, "subscription to the channel for notification is successfully");
                        }
                    });
        }
        //
        //Initializations
        objectMapper = new ObjectMapper();
        //Check network
        com.flys.tools.utils.Utils.registerNetworkCallback(this);
    }

    @Override
    protected void onResumeActivity() {
        //Update view if user has been connected
        if (updateProfile() != null && updateProfile().getType() != null) {
            updateUserConnectedProfile(updateProfile());
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Handle pushed notification if exist
        handleNotifications(intent);
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
        disconnectHandle();
    }

    @Override
    public void onBackPressed() {
        drawerLayout.closeDrawers();
        if (mViewPager.getCurrentItem() == HOME_FRAGMENT) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            this.dialog = new MaterialNotificationDialog(this, new NotificationData(getString(R.string.app_name), "Voudriez-vous quitter l'application?", "OUI", "NON", getDrawable(R.drawable.books), R.style.customMaterialAlertEditDialog), this);
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
                //Mise à jour des informations de l'utilisateur dans la session
                getProviderData(FirebaseAuth.getInstance().getCurrentUser());
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.e(getClass().getSimpleName(), "onActivityResult: sign_in_cancelled");
                    Utils.showErrorMessage(MainActivity.this, findViewById(R.id.main_content), getColor(R.color.red_700), "Connexion annulée");
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.e(getClass().getSimpleName(), "onActivityResult: no_internet_connection");
                    Utils.showErrorMessage(MainActivity.this, findViewById(R.id.main_content), getColor(R.color.red_700), "Oops! Erreur connexion internet");
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e(getClass().getSimpleName(), "onActivityResult: unknown_error");
                    Utils.showErrorMessage(MainActivity.this, findViewById(R.id.main_content), getColor(R.color.red_700), "Oops! Veuillez réessayer..");
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
    public Observable<byte[]> downloadFacebookProfileImage(String baseUrl, String ext, String params, String facebookAppId) {
        return dao.downloadFacebookProfileImage(baseUrl, ext, params, facebookAppId);
    }

    @Override
    public Observable<List<Notification>> loadNotificationsFromDatabase() {
        return dao.loadNotificationsFromDatabase();
    }

    @Override
    public Observable<List<Notification>> loadNotificationsFromDatabase(String property, Object value) {
        return dao.loadNotificationsFromDatabase(property, value);
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
                Log.e(getClass().getSimpleName(), "Dao Exception!", e);
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

    @Override
    public void popupSnackbarForCompleteUpdate(AppUpdateManager appUpdateManager) {
        Snackbar snackbar =
                Snackbar.make(
                        mViewPager,
                        getString(R.string.main_activity_completed_download),
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.main_activity_download_completed_restart, view -> {
            appUpdateManager.completeUpdate();
        });
        bottomNavigationView.setVisibility(View.GONE);
        snackbar.setAnchorView(bottomNavigationView);
        snackbar.setActionTextColor(getColor(R.color.red_700));
        snackbar.show();
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
                //Connected with google account
                case Constants.SOCIAL_MEDIA_GOOGLE_COM:
                    //Connection using google
                    googleConnect(firebaseUser, user, profile);
                    break;
                //Connected with facebook account
                case Constants.SOCIAL_MEDIA_FACEBOOK_COM:
                    //Connection using facebook
                    facebookConnect(user);
                    break;
                //connected with phone number
                case Constants.SOCIAL_MEDIA_PHONE:
                    //Connection using phone number
                    phoneNumberConnect(user, profile);
                    break;
                //connected with an email address
                case Constants.SOCIAL_MEDIA_PASSWORD:
                    //Connection using email address
                    mailConnect(user, profile);
                    break;
                default:
                    break;
            }

        }
        try {
            Log.e(getClass().getSimpleName(), "Mainactivity user before save : " + user);
            if (user != null) {
                userDao.save(user);
            }
        } catch (DaoException e) {
            Log.e(getClass().getSimpleName(), "Dao Exception!", e);
        }
    }

    /**
     * @param user
     * @param profile
     */
    private void phoneNumberConnect(User user, UserInfo profile) {
        user.setType(User.Type.PHONE);
        user.setPhone(profile.getPhoneNumber());
        showDialogImage(null, user);
    }

    /**
     * @param user
     * @param profile
     */
    private void mailConnect(User user, UserInfo profile) {
        user.setType(User.Type.MAIL);
        user.setEmail(profile.getEmail());
        showDialogImage(null, user);
    }

    /**
     * @param firebaseUser
     * @param user
     * @param profile
     */
    private void googleConnect(FirebaseUser firebaseUser, User user, UserInfo profile) {
        user.setType(User.Type.GOOGLE);
        user.setNom(firebaseUser.getDisplayName());
        user.setEmail(profile.getEmail());
        user.setImageUrl(profile.getPhotoUrl().toString().replace("s96-c", "s400-c"));
        if (CommonToolsConstants.isNetworkConnected) {
            //Launch the loader
            beginWaiting();
            downloadUrl(user.getImageUrl())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bytes -> {
                        //Save the user avator in internal storage
                        FileUtils.saveToInternalStorage(bytes, "glearning", user.getNom() + ".png", this);
                        //Update profile
                        updateUserConnectedProfile(user);
                        //cancel the loading
                        cancelWaiting();
                        //Show user dialog with user resume
                        showDialogImage(bytes, user);
                    }, error -> {
                        //on affiche les messages de la pile d'exceptions du Throwable th
                        new AlertDialog.Builder(this).setTitle("Ooops !").setMessage(R.string.activity_main_check_your_connection_and_try_again).setNeutralButton(R.string.activity_main_button_close, null).show();
                    });
        } else {
            Utils.showErrorMessage(MainActivity.this, findViewById(R.id.main_content), getColor(R.color.blue_500), getString(R.string.activity_main_network_issue));
        }
    }


    /**
     * @param user
     */
    private void facebookConnect(User user) {
        if (CommonToolsConstants.isNetworkConnected) {
            downloadFacebookUserProfileImage(user);
        } else {
            Utils.showErrorMessage(MainActivity.this, findViewById(R.id.main_content), getColor(R.color.blue_500), getString(R.string.oops_connection_issue_msg));
        }
    }

    /**
     * @param user
     */
    private void downloadFacebookUserProfileImage(User user) {
        //
        user.setType(User.Type.FACEBOOK);
        Bundle params = new Bundle();
        params.putString("fields", "id, name, birthday,hometown,email,gender,cover,picture.width(640).height(640)");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                response -> {
                    if (response != null) {
                        try {
                            FacebookProfile facebookProfile = objectMapper.readValue(response.getRawResponse(), new TypeReference<FacebookProfile>() {

                            });
                            //Collect user information
                            user.setNom(facebookProfile.getName());
                            user.setImageUrl(facebookProfile.getPicture().getData().getUrl());
                            user.setEmail(facebookProfile.getEmail());
                            //Updating connected user
                            session.setUser(userDao.update(user));
                            //Update profile
                            updateUserConnectedProfile(user);
                            FacebookUrl facebookUrl = com.flys.tools.utils.Utils.facebookProfileImageUrlSplit(facebookProfile.getPicture().getData().getUrl());
                            beginWaiting();
                            downloadFacebookProfileImage(facebookUrl.getBaseUrl(), facebookUrl.getExt(), facebookUrl.getHash(), facebookUrl.getAsid())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(bytes -> {
                                        //
                                        FileUtils.saveToInternalStorage(bytes, "glearning", user.getNom() + ".png", this);
                                        //Update profile
                                        updateUserConnectedProfile(user);
                                        //Cancel waiting
                                        cancelWaiting();
                                        //launch dialog fragment to show connection details
                                        showDialogImage(bytes, user);
                                    }, error -> {
                                        // on affiche les messages de la pile d'exceptions du Throwable th
                                        new AlertDialog.Builder(this).setTitle("Ooops !").setMessage(getString(R.string.activity_main_check_your_connection_and_try_again)).setNeutralButton(getString(R.string.activity_main_button_close), null).show();
                                    });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).executeAsync();
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
        if (user != null) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.dialog_contact_image);
            TextView name = dialog.findViewById(R.id.name);
            TextView email = dialog.findViewById(R.id.email_or_number);
            ImageView smallImage = dialog.findViewById(R.id.small_image);
            ImageView image = dialog.findViewById(R.id.large_image);
            switch (user.getType()) {
                case GOOGLE:
                case MAIL:
                    email.setText(user.getEmail());
                    name.setText(user.getNom());
                    break;
                case FACEBOOK:
                    email.setText(user.getNom());
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
     * Update the user profile
     *
     * @param user
     */
    void updateUserConnectedProfile(User user) {
        View headerNavView = navigationView.getHeaderView(0);
        ShapeableImageView profile = headerNavView.findViewById(R.id.profile_image);
        TextView title = headerNavView.findViewById(R.id.profile_user_name);
        TextView mail = headerNavView.findViewById(R.id.profile_user_email_address);
        MenuItem disconnect = navigationView.getMenu().findItem(R.id.menu_deconnexion);
        LinearLayout userInfo=headerNavView.findViewById(R.id.profile_user_info);
        //Si l'utilisateur est connecte?
        if (user != null && (user.getEmail() != null || user.getPhone() != null)) {
            disconnect.setVisible(true);
            switch (user.getType()) {
                case GOOGLE:
                case FACEBOOK:
                    title.setText(user.getNom());
                    mail.setText(user.getEmail());
                    profile.setImageDrawable(user.getImageUrl() != null ? FileUtils.loadImageFromStorage("glearning", user.getNom() + ".png", DApplicationContext.getContext()) : getDrawable(R.drawable.ic_outline_account_circle_24));
                    break;
                case MAIL:
                    title.setText(user.getNom());
                    mail.setText(user.getEmail());
                    profile.setImageDrawable(getDrawable(R.drawable.ic_outline_account_circle_24));
                    break;
                case PHONE:
                    title.setText(user.getNom());
                    mail.setText(user.getPhone());
                    profile.setImageDrawable(getDrawable(R.drawable.ic_outline_account_circle_24));
                    break;
            }
            profile.setStrokeColor(getColorStateList(R.color.color_secondary));
            profile.setStrokeWidth((float) 0.5);
            profile.setOnClickListener(null);
            userInfo.setVisibility(View.VISIBLE);
        } else {
            userInfo.setVisibility(View.GONE);
            profile.setStrokeColor(null);
            profile.setStrokeWidth(0);
            disconnect.setVisible(false);
            profile.setImageDrawable(getDrawable(R.drawable.ic_outline_account_circle_24));
            profile.setOnClickListener(v -> {
                createSignInIntent();
            });
        }
    }

    /**
     * @param intent
     */
    private void handleNotifications(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey(Constants.NOTIFICATION)) {
            Notification notification = (Notification) bundle.getSerializable(Constants.NOTIFICATION);
            if (notification != null) {
                try {
                    notification.setDate(new Date());
                    notificationDao.save(notification);
                    getSupportActionBar().show();
                    updateNotificationNumber(1);
                    activateMainButtonMenu(R.id.bottom_menu_me);
                    navigateToView(NOTIFICATION_FRAGMENT, ISession.Action.SUBMIT);
                } catch (DaoException e) {
                    Log.e(getClass().getSimpleName(), "Dao Exception!", e);
                }

            } else {
                Log.e(getClass().getSimpleName(), " onNewIntent(): notification null ");
            }

        } else {
            Log.e(getClass().getSimpleName(), " onNewIntent(): bundle null ");
        }
        this.setIntent(intent);
    }

    /**
     * handle disconnection
     */
    private void disconnectHandle() {
        MaterialNotificationDialog dialog = new MaterialNotificationDialog(this, new NotificationData(getString(R.string.app_name), getString(R.string.activity_main_do_you_want_to_disconnect), getString(R.string.activity_main_button_yes_msg), getString(R.string.activity_main_button_no_msg), getDrawable(R.drawable.books), R.style.customMaterialAlertEditDialog), new MaterialNotificationDialog.NotificationButtonOnclickListeneer() {
            @Override
            public void okButtonAction(DialogInterface dialogInterface, int i) {
                // Disconnection
                Disconnection();
            }

            @Override
            public void noButtonAction(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show(getSupportFragmentManager(), "material_notification_alert_dialog");
    }

    /**
     *
     */
    private void Disconnection() {
        MaterialNotificationDialog notificationDialog = new MaterialNotificationDialog(MainActivity.this, new NotificationData(getString(R.string.app_name), getString(R.string.activity_main_thanks_msg) + (session.getUser().getNom() != null ? session.getUser().getNom() : "") + getString(R.string.activity_main_see_you_soon), getString(R.string.activity_main_button_yes_msg), getString(R.string.activity_main_button_cancel), getDrawable(R.drawable.books), R.style.customMaterialAlertEditDialog), new MaterialNotificationDialog.NotificationButtonOnclickListeneer() {
            @Override
            public void okButtonAction(DialogInterface dialogInterface, int i) {
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                try {
                                    //if disconnect, clear the session and delete the user from de database
                                    userDao.delete(session.getUser());
                                    session.setUser(null);
                                    onPrepareOptionsMenu(null);
                                    updateUserConnectedProfile(null);
                                    dialogInterface.dismiss();
                                } catch (DaoException e) {
                                    Log.e(getClass().getSimpleName(), "Dao Exception!", e);
                                }
                            }
                            if (task.isCanceled()) {
                                Utils.showErrorMessage(MainActivity.this, findViewById(R.id.main_content), getColor(R.color.blue_500), getString(R.string.activity_main_disconnect_canceled));
                            }
                        });
            }

            @Override
            public void noButtonAction(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        notificationDialog.show(getSupportFragmentManager(), "material_notification_alert_dialog");
    }
    @Override
    public void scrollUp() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void scrollDown() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

}
