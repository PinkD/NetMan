package moe.pinkd.netman.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import moe.pinkd.netman.bean.AppStatus;

/**
 * Created by PinkD on 2017/5/25.
 * StatusUpdater
 */

public class StatusUpdater {

    public static List<AppStatus> GLOBAL_APP_STATUS = new ArrayList<>();
    private static StatusObservable statusObservable = new StatusObservable();

    public static void addStatusUpdate(Observer observer) {
        statusObservable.addObserver(observer);
    }

    public static void notifyStatusUpdate(int position) {
        statusObservable.notifyObservers(position);
    }

    public static void notifyStatusUpdate() {
        statusObservable.notifyObservers();
    }

    private static class StatusObservable extends Observable {
        @Override
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }

        @Override
        public void notifyObservers() {
            setChanged();
            super.notifyObservers();
        }
    }

}
