package com.flys.fragments.behavior;

import com.flys.R;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.custom.CoreState;
import com.flys.fragments.state.DummyFragmentState;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;

@EFragment(R.layout.fragment_dummy_layout)
@OptionsMenu(R.menu.menu_vide)
public class DummyFragment extends AbstractFragment {

  // champs hérités de la classe parent -------------------------------------------------------

  // mode debug
  //-- final protected boolean isDebugEnabled = IMainActivity.IS_DEBUG_ENABLED;
  // nom de la classe
  //-- protected String className;
  // tâches asynchrones
  //-- protected int numberOfRunningTasks;
  // activité
  //-- protected IMainActivity mainActivity;
  //-- protected Activity activity;
  // session
  //-- protected Session session;

  // méthodes héritées de la classe parent -------------------------------------------------------

  // affichage options de menu
  //-- protected void setAllMenuOptionsStates(boolean isVisible) {
  //-- protected void setMenuOptionsStates(MenuItemState[] menuItemStates) {
  // gestion de l'attente de la fin d'une série de tâches asynchrones
  //-- protected void beginRunningTasks(int numberOfRunningTasks) {
  //-- protected void cancelWaitingTasks() {
  // exécution d'une tâche asynchrone avec RxAndroid
  //-- protected <T> void executeInBackground(Observable<T> process, Action1<T> consumeResult) {
  // annulation des tâches
  //-- protected void cancelRunningTasks() {
  // affichage alerte sur exception
  //-- protected void showAlert(Throwable th) {
  // affichage liste de messages
  //-- protected void showAlert(List<String> messages) {

  // méthodes imposées par la classe parent -------------------------------------------------------

  @Override
  public CoreState saveFragment() {
    // il faut sauvegarder le fragment
    DummyFragmentState state=new DummyFragmentState();
    // ...
    return state;
    // s'il n'y a rien à sauvegarder faire [return new CoreState();] et supprimer la classe [DummyFragmentState]
  }

  @Override
  protected int getNumView() {
    // il faut retourner le n° du fragment dans le tableau des fragments gérés par l'activité (cf MainActivity)
    return 0;
  }

  @Override
  protected void initFragment(CoreState previousState) {
    // le fragment devient visible et a subi une constuction dans cette étape ou une étape précédente
    // cela se produit au démarrage de l'application et à chaque rotation du périphérique Android
    // est forcément suivie par l'exécution de [initFragment]
    // il faut initialiser les champs du fragment qui a été reconstruit
    // previousState est la dernière sauvegarde du fragment - vaut null si c'est la 1ère visite du fragment
  }

  @Override
  protected void initView(CoreState previousState) {
    // le fragment devient visible et la vue associée a été reconstruite dans cette étape ou une étape précédente
    // cela se produit à cahque fois que [initFragment] est exécutée et à chaque fois que le fragment sort de l'adjacence du frgament affiché
    // il faut initialiser les composants de la vue qui a été reconstruite
    // previousState est la dernière sauvegarde du fragment - vaut null si c'est la 1ère visite du fragment

  }

  @Override
  protected void updateOnSubmit(CoreState previousState) {
    // est exécutée après [initFragment, initView] si ces méthodes sont exécutées
    // la vue va être affichée après une opération de type SUBMIT
    // il faut en général initialiser fragment et vue associée à partir de la session
    // previousState est la dernière sauvegarde du fragment - vaut null si c'est la 1ère visite du fragment
    // il n'y a rien à faire si on ne peut arriver au fragment par une opération SUBMIT
    // si on peut arriver sur le fragment par plusieurs opérations SUBMIT à partir de fragments différents, on peut connaître la vue précédente par [session.getPreviousView]
    // si on peut arriver sur le fragment par plusieurs opérations SUBMIT à partir du même fragment, alors il faut mettre en session un indicateur pour différencier
    // les différents types de SUBMIT à partir de ce fragment
  }

  @Override
  protected void updateOnRestore(CoreState previousState) {
    // est exécutée après [initFragment, initView] si ces méthodes sont exécutées
    // la vue va être affichée après une opération de type RESTORE ou NAVIGATION
    // previousState est la dernière sauvegarde du fragment - ne vaut jamais null
    // il faut remettre la vue dans son état précédent

  }

  @Override
  protected void notifyEndOfUpdates() {
    // intervient après les méthodes [updateOnSubmit, updateOnRestore]
    // lorsqu'on est là, la vue a été construite et initialisée
    // il n'y a souvent rien à faire ici mais on peut aussi factoriser ici des actions qu'il faudrait faire quelque soit la façon dont on arrive sur cette vue
  }

  @Override
  protected void notifyEndOfTasks(boolean runningTasksHaveBeenCanceled) {
    // appelée lorsque les tâches asynchrones lancées par le fragment sont soit terminées soit annulées
    // ces deux cas peuvent être différenciés grâce au paramètre runningTasksHaveBeenCanceled
    // il faut en général remettre la vue dans un état différent de celui qu'elle avait pendant qu'elle attendait les réponses des tâches asynchrones

  }
}
