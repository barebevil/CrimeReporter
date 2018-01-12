package com.derekpoon.crimereporter;

import android.support.v4.app.Fragment;

/**
 * Created by derekpoon on 12/01/2018.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

}
