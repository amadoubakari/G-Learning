package com.flys.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.flys.R;
import com.flys.architecture.core.AbstractActivity;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.core.ISession;
import com.flys.architecture.custom.Session;
import com.flys.dao.service.Dao;
import com.flys.dao.service.IDao;
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
import com.flys.fragments.behavior.TestFragment_;
import com.flys.fragments.behavior.TortoiseFragment_;
import com.flys.fragments.behavior.TreeFragment_;
import com.flys.fragments.behavior.TroncFragment_;

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
        if(getIntent().hasExtra("notification")){
            navigateToView(10, ISession.Action.SUBMIT);
        }
    }

    @Override
    protected void onResumeActivity() {
        if(getIntent().hasExtra("notification")){
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
                new HyeneFragment_(), new AlphabetFragment_()};
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

   /* @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(getClass().getSimpleName(),"on new intent :"+intent.getExtras());
        if(intent.getExtras().containsKey("notification")){
            navigateToView(10, ISession.Action.SUBMIT);
        }
    }*/
}
