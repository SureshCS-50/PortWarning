package com.example.portwarning;

import android.app.Application;

import com.example.portwarning.Models.Alert;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orm.SugarApp;

public class PortWarningApplication extends SugarApp {

    private static PortWarningApplication mApplication;

    private static DatabaseReference mFirebaseDatabase;
    private static FirebaseDatabase mFirebaseInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        getInstance();
        getFirebaseInstance();
    }

    public static PortWarningApplication getInstance(){
        if(mApplication == null){
            mApplication = new PortWarningApplication();

        }
        return mApplication;
    }

    public DatabaseReference getFirebaseDatabase(){
        if(mFirebaseDatabase == null){
            // get reference to 'alerts' node
            mFirebaseDatabase = mFirebaseInstance.getReference(Alert.CHILD_NAME);
        }
        return mFirebaseDatabase;
    }

    public FirebaseDatabase getFirebaseInstance(){
        if(mFirebaseInstance == null){
            mFirebaseInstance = FirebaseDatabase.getInstance();
        }
        return mFirebaseInstance;
    }
}
