package test.project.firestore_sample.controls;

//Created by ioBirdOussama on 09/04/2017.

import android.os.SystemClock;
import android.view.View;

/**
 * Custom {@link View.OnClickListener} preventing unwanted double click.
 */
public abstract class OnSingleClickListener implements View.OnClickListener {

    /**
     * The time between two consecutive click events, in milliseconds.
     */
    private static final long MIN_CLICK_INTERVAL = Constants.TIME_ONE_SECOND;

    private long mLastClickTime = 0;

    protected abstract void onSingleClick();

    @Override
    public final void onClick(View v) {
        if (SystemClock.uptimeMillis() - mLastClickTime < MIN_CLICK_INTERVAL)
            return;
        mLastClickTime = SystemClock.uptimeMillis();

        onSingleClick();
    }

}