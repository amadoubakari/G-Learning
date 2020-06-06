package com.flys.fragments.behavior;

import com.flys.R;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.custom.CoreState;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;

/**
 * @author AMADOU BAKARI
 * @version 1.0.0
 * @email: amadoubakari1992@gmail.com
 * @since 03/06/2020 15:34
 */
@EFragment(R.layout.fragment_about)
@OptionsMenu(R.menu.menu_vide)
public class AboutFragment  extends AbstractFragment {
    @Override
    public CoreState saveFragment() {
        return new CoreState();
    }

    @Override
    protected int getNumView() {
        return mainActivity.ABOUT_FRAGMENT;
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
