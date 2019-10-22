package com.flys.activity;

import android.util.Log;

import com.flys.R;
import com.flys.architecture.core.AbstractActivity;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.custom.Session;
import com.flys.dao.service.Dao;
import com.flys.dao.service.IDao;
import com.flys.fragments.behavior.SplashScreenFragment_;
import com.flys.fragments.behavior.TestFragment_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;

@EActivity
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends AbstractActivity {

  // couche [DAO]
  @Bean(Dao.class)
  protected IDao dao;
  // session
  private Session session;

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
  protected IDao getDao() {
    return dao;
  }

  @Override
  protected AbstractFragment[] getFragments() {
    // todo : définir les fragments ici
    return new AbstractFragment[]{new SplashScreenFragment_(),new TestFragment_()};
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
}
