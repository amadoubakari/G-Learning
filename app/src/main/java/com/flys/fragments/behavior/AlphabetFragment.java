package com.flys.fragments.behavior;

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
@OptionsMenu(R.menu.menu_vide)
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
        return 2;
    }

    @Override
    protected void initFragment(CoreState previousState) {

    }

    @Override
    protected void initView(CoreState previousState) {
        alphabets = new ArrayList<>();
        alphabets.add(new Alphabet("A"));
        alphabets.add(new Alphabet("B"));
        alphabets.add(new Alphabet("C"));
        alphabets.add(new Alphabet("D"));
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(activity,2);
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
