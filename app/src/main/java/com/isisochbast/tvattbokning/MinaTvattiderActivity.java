package com.isisochbast.tvattbokning;

import android.support.v4.app.Fragment;

/**
 * Håller fragmentet till mina bokningar, fragment skapas och hämtas mha den abstrakta klassen SingleFragmentActivity
 */
public class MinaTvattiderActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MinaTvattiderFragment();
    }
}
