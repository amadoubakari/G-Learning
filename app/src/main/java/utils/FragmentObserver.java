package utils;

import java.util.Observable;

public class FragmentObserver extends Observable {
    @Override
    public void notifyObservers(Object o) {
        setChanged();
        super.notifyObservers(o);
    }
}