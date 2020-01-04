package com.flys.fragments.behavior;

import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flys.R;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.core.ISession;
import com.flys.architecture.custom.CoreState;
import com.flys.common_tools.utils.Utils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.fragment_home_layout)
@OptionsMenu(R.menu.menu_home)
public class HomeFragment extends AbstractFragment {

    @ViewById(R.id.webview)
    protected WebView webview;

    @Click(R.id.next)
    protected void nextChapitre() {
        mainActivity.navigateToView(mainActivity.DOG_FRAGMENT, ISession.Action.SUBMIT);
    }

    @Click(R.id.previous)
    protected void previousChapitre() {

    }

    @Override
    public CoreState saveFragment() {
        return new CoreState();
    }

    @Override
    protected int getNumView() {
        return mainActivity.HOME_FRAGMENT;
    }

    @Override
    protected void initFragment(CoreState previousState) {
        ((AppCompatActivity) mainActivity).getSupportActionBar().show();
    }

    @Override
    protected void initView(CoreState previousState) {
        Utils.readHtmlFileFromAssets(activity,"html/home.html",webview);
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
