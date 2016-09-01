package com.shiz.flighttime.dagger;

import com.shiz.flighttime.main.MainActivity;


/**
 * Created by oldman on 22.08.16.
 */
@ActivityScope
//@Subcomponent(modules = MainActivityModule.class)
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
