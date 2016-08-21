package com.experia.experia.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.experia.experia.R;

import fragments.ExampleDataProviderFragment;
import fragments.ItemPinnedMessageDialogFragment;
import fragments.SwipeableWithButtonExampleFragment;
import models.AbstractDataProvider;

/**
 * Created by doc_dungeon on 8/20/16.
 */
public class SwipeableWithButtonExampleActivity extends AppCompatActivity implements ItemPinnedMessageDialogFragment.EventListener {
    private static final String FRAGMENT_TAG_DATA_PROVIDER = "data provider";
    private static final String FRAGMENT_LIST_VIEW = "list view";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(new ExampleDataProviderFragment(), FRAGMENT_TAG_DATA_PROVIDER)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SwipeableWithButtonExampleFragment(), FRAGMENT_LIST_VIEW)
                    .commit();
        }
    }

    /**
     * This method will be called when a list item is pinned
     *
     * @param position The position of the item within data set
     */
    public void onItemPinned(int position) {
    }

    /**
     * This method will be called when a list item is clicked
     *
     * @param position The position of the item within data set
     */
    public void onItemClicked(int position) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);
        AbstractDataProvider.Data data = getDataProvider().getItem(position);

        if (data.isPinned()) {
            // unpin if tapped the pinned item
            data.setPinned(false);
            ((SwipeableWithButtonExampleFragment) fragment).notifyItemChanged(position);
        }
    }
    /**
     * This method will be called when a "button placed under the swipeable view" is clicked
     *
     * @param position The position of the item within data set
     */
    public void onItemButtonClicked(int position) {
        String text = getString(R.string.snack_bar_text_button_clicked, position);

        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.container),
                text,
                Snackbar.LENGTH_SHORT);

        snackbar.show();
    }

    // implements ItemPinnedMessageDialogFragment.EventListener
    @Override
    public void onNotifyItemPinnedDialogDismissed(int itemPosition, boolean ok) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);

        getDataProvider().getItem(itemPosition).setPinned(ok);
        ((SwipeableWithButtonExampleFragment) fragment).notifyItemChanged(itemPosition);
    }

    public AbstractDataProvider getDataProvider() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_DATA_PROVIDER);
        return ((ExampleDataProviderFragment) fragment).getDataProvider();
    }
}
