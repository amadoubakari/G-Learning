package com.flys.fragments.behavior;

import com.flys.R;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.core.ISession;
import com.flys.architecture.custom.CoreState;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;

@EFragment(R.layout.fragment_lance_layout)
@OptionsMenu(R.menu.menu_home)
public class LanceFragment extends AbstractFragment {

    @Click(R.id.next)
    protected void nextChapitre() {
        mainActivity.navigateToView(mainActivity.ECHELLE_FRAGMENT, ISession.Action.SUBMIT);
    }
    @Override
    public CoreState saveFragment() {
        return new CoreState();
    }

    @Override
    protected int getNumView() {
        return mainActivity.LANCE_FRAGMENT;
    }

    @Override
    protected void initFragment(CoreState previousState) {

    }

    @Override
    protected void initView(CoreState previousState) {

    }

    @Override
    protected void updateOnSubmit(CoreState previousState) {

    }

    @Override
    protected void updateOnRestore(CoreState previousState) {

    }

    @Override
    protected void notifyEndOfUpdates() {

    }

    @Override
    protected void notifyEndOfTasks(boolean runningTasksHaveBeenCanceled) {

    }

    @Override
    protected boolean hideNavigationBottomView() {
        return false;
    }
}
