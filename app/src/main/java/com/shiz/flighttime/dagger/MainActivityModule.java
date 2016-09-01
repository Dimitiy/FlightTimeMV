package com.shiz.flighttime.dagger;

import com.shiz.flighttime.main.MainActivityPresenter;
import com.shiz.flighttime.main.MainActivityPresenterImpl;
import com.shiz.flighttime.main.MainActivityView;
import com.shiz.flighttime.main.MainInteractor;
import com.shiz.flighttime.main.MainInteractorImpl;


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
    MainActivityView provideMainActivityView() {
        return this.view;
    }

    //    @Provides
    @ActivityScope
    MainInteractor provideMainInteractor(MainInteractorImpl interactor) {
        return interactor;
    }

    //    @Provides
    @ActivityScope
    MainActivityPresenter provideMainPresenter(MainActivityPresenterImpl presenter) {
        return presenter;
    }
}
