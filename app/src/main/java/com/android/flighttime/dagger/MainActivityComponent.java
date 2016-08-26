package com.android.flighttime.dagger;

import com.android.flighttime.main.MainActivity;


/**
 * Created by oldman on 22.08.16.
 */
@ActivityScope
//@Subcomponent(modules = MainActivityModule.class)
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
