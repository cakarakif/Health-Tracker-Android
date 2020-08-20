package com.calculate.akifcakar.healthtracker;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmDatabase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // The default Realm file is "default.realm" in Context.getFilesDir();
        // we'll change it to "myrealm.realm"
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("personInfoDatabase").build();
        Realm.setDefaultConfiguration(config);
    }
}
