package com.flys.fragments.behavior;

import androidx.appcompat.app.AppCompatActivity;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;

import com.flys.R;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.custom.CoreState;

@EFragment(R.layout.fragment_dummy_layout)
@OptionsMenu(R.menu.menu_vide)
public class TestFragment extends AbstractFragment {
    @Override
    public CoreState saveFragment() {
        return new CoreState();
    }

    @Override
    protected int getNumView() {
        return 1;
    }

    @Override
    protected void initFragment(CoreState previousState) {
        ((AppCompatActivity) mainActivity).getSupportActionBar().show();
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
}
