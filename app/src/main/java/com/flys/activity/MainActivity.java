package com.flys.activity;

import android.util.Log;

import com.flys.R;
import com.flys.architecture.core.AbstractActivity;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.custom.Session;
import com.flys.dao.service.Dao;
import com.flys.dao.service.IDao;
import com.flys.fragments.behavior.AlphabetFragment_;
import com.flys.fragments.behavior.ArcFragment_;
import com.flys.fragments.behavior.BottleFragment_;
import com.flys.fragments.behavior.CatFragment_;
import com.flys.fragments.behavior.CoqFragment_;
import com.flys.fragments.behavior.CrucheFragment_;
import com.flys.fragments.behavior.DogFragment_;
import com.flys.fragments.behavior.FishDogFragment_;
import com.flys.fragments.behavior.FishFragment_;
import com.flys.fragments.behavior.HangarFragment_;
import com.flys.fragments.behavior.HomeFragment_;
import com.flys.fragments.behavior.KnifeFragment_;
import com.flys.fragments.behavior.LeafFragment_;
import com.flys.fragments.behavior.MancheFragment_;
import com.flys.fragments.behavior.PanierFragment_;
import com.flys.fragments.behavior.PlantoirFragment_;
import com.flys.fragments.behavior.PorcEpicFragment_;
import com.flys.fragments.behavior.ShoeFragment_;
import com.flys.fragments.behavior.SingeFragment_;
import com.flys.fragments.behavior.SplashScreenFragment_;
import com.flys.fragments.behavior.SquirryFragment_;
import com.flys.fragments.behavior.SummarizeFragment_;
import com.flys.fragments.behavior.TestFragment_;
import com.flys.fragments.behavior.TortoiseFragment_;
import com.flys.fragments.behavior.TreeFragment_;

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
    return new AbstractFragment[]{new SplashScreenFragment_(),new AlphabetFragment_(),
            new FishFragment_(),new DogFragment_(),new FishDogFragment_(),new LeafFragment_(),
    new CrucheFragment_(),new ArcFragment_(),new BottleFragment_(),new PanierFragment_(),
    new TortoiseFragment_(),new MancheFragment_(),
    new KnifeFragment_(),new SummarizeFragment_(),new ShoeFragment_(),
    new HangarFragment_(),new TreeFragment_(),new SquirryFragment_(),
    new SingeFragment_(),new PlantoirFragment_(),new CoqFragment_(),
    new PorcEpicFragment_(),new CatFragment_()};
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
