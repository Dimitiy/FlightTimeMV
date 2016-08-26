package com.android.flighttime.dagger;

import com.android.flighttime.main.MainActivityPresenter;
import com.android.flighttime.main.MainActivityPresenterImpl;
import com.android.flighttime.main.MainActivityView;
import com.android.flighttime.main.MainInteractor;
import com.android.flighttime.main.MainInteractorImpl;


/**
 * Created by oldman on 22.08.16.
 */
//@Module
public class MainActivityModule {

    public final MainActivityView view;

    public MainActivityModule(MainActivityView view) {
        this.view = view;
    }

//    @Provides
    @ActivityScope
    MainActivityView provideMainActivityView(){
        return this.view;
    }

//    @Provides
    @ActivityScope
    MainInteractor provideMainInteractor(MainInteractorImpl interactor){
        return  interactor;
    }

//    @Provides
    @ActivityScope
    MainActivityPresenter provideMainPresenter(MainActivityPresenterImpl presenter){
        return  presenter;
    }
}
