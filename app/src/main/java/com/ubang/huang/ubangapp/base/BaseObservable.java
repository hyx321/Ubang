package com.ubang.huang.ubangapp.base;

import java.util.Observable;

public class BaseObservable extends Observable {

    private static volatile BaseObservable observable = null;

    private BaseObservable() {
    }

    public static synchronized BaseObservable getObservable() {
        if (observable == null) {
            observable = new BaseObservable();
        }
        return observable;
    }

    @Override
    public void notifyObservers(Object arg) {
        super.notifyObservers(arg);
        this.setChanged();
    }
}
