package com.mikhaellopez.dualshot.controller.activity.generic;

import android.view.MenuItem;

/**
 * Created by Mikhael LOPEZ on 04/01/16.
 */
public class ABaseBackActivity extends ABaseActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
