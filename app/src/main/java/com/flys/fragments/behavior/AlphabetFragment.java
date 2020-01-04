package com.flys.fragments.behavior;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flys.R;
import com.flys.architecture.core.AbstractFragment;
import com.flys.architecture.custom.CoreState;
import com.flys.dao.entities.Alphabet;
import com.flys.fragments.adapters.AlphabetAdapter;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_alphabet_layout)
@OptionsMenu(R.menu.menu_home)
public class AlphabetFragment extends AbstractFragment {

    @ViewById(R.id.recyclerview)
    protected RecyclerView recyclerView;

    private AlphabetAdapter alphabetAdapter;

    private List<Alphabet> alphabets;

    @Override
    public CoreState saveFragment() {
        return new CoreState();
    }

    @Override
    protected int getNumView() {
        return mainActivity.ALPHABET_FRAGMENT;
    }

    @Override
    protected void initFragment(CoreState previousState) {
        ((AppCompatActivity) mainActivity).getSupportActionBar().show();
    }

    @Override
    protected void initView(CoreState previousState) {
        alphabets = new ArrayList<>();
        alphabets.add(new Alphabet("i"));
        alphabets.add(new Alphabet("a"));
        alphabets.add(new Alphabet("u"));
        alphabets.add(new Alphabet("e"));
        alphabets.add(new Alphabet("o"));
        alphabets.add(new Alphabet("ə"));
        alphabets.add(new Alphabet("b"));
        alphabets.add(new Alphabet("ɓ"));
        alphabets.add(new Alphabet("c"));
        alphabets.add(new Alphabet("d"));
        alphabets.add(new Alphabet("ɗ"));
        alphabets.add(new Alphabet("f"));
        alphabets.add(new Alphabet("g"));
        alphabets.add(new Alphabet("gb"));
        alphabets.add(new Alphabet("gw"));
        alphabets.add(new Alphabet("h"));
        alphabets.add(new Alphabet("ˀ"));
        alphabets.add(new Alphabet("j"));
        alphabets.add(new Alphabet("k"));
        alphabets.add(new Alphabet("kp"));
        alphabets.add(new Alphabet("kw"));
        alphabets.add(new Alphabet("l"));
        alphabets.add(new Alphabet("m"));
        alphabets.add(new Alphabet("mb"));
        alphabets.add(new Alphabet("n"));
        alphabets.add(new Alphabet("nd"));
        alphabets.add(new Alphabet("ng"));
        alphabets.add(new Alphabet("ngb"));
        alphabets.add(new Alphabet("ngw"));
        alphabets.add(new Alphabet("nj"));
        alphabets.add(new Alphabet("ny"));
        alphabets.add(new Alphabet("ŋ"));
        alphabets.add(new Alphabet("p"));
        alphabets.add(new Alphabet("r"));
        alphabets.add(new Alphabet("s"));
        alphabets.add(new Alphabet("sl"));
        alphabets.add(new Alphabet("t"));
        alphabets.add(new Alphabet("v"));
        alphabets.add(new Alphabet("vb"));
        alphabets.add(new Alphabet("w"));
        alphabets.add(new Alphabet("y"));
        alphabets.add(new Alphabet("z"));
        alphabets.add(new Alphabet("zl"));
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(activity,4);
        // exploitation de la réponse
        alphabetAdapter = new AlphabetAdapter(alphabets, activity);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(alphabetAdapter);
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
