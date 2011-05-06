package com.marakana.yambatab;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;

public class TimelineFragment extends ListFragment implements
    LoaderManager.LoaderCallbacks<Cursor> {
  static final String TAG = StatusData.class.getSimpleName();

  static final String[] FROM = new String[] { StatusData.C_USER,
      StatusData.C_TEXT, StatusData.C_PROFILE_IMAGE_URL };
  static final int[] TO = new int[] { R.id.status_username, R.id.status_text,
      R.id.status_image };

  // This is the Adapter being used to display the list's data.
  SimpleCursorAdapter mAdapter;

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    // Give some text to display if there is no data. In a real
    // application this would come from a resource.
    setEmptyText("No timeline yet...");

    // We have a menu item to show in action bar.
    setHasOptionsMenu(true);

    // Create an empty adapter we will use to display the loaded data.
    mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.status_row,
        null, FROM, TO, 0);
    mAdapter.setViewBinder(VIEW_BINDER);
    setListAdapter(mAdapter);

    // Prepare the loader. Either re-connect with an existing one,
    // or start a new one.
    getLoaderManager().initLoader(0, null, this);
  }

  // These are the status rows that we will retrieve.
  static final String[] PROJECTION = new String[] { StatusData.C_ID,
      StatusData.C_CREATED_AT, StatusData.C_USER, StatusData.C_SCREEN_NAME,
      StatusData.C_PROFILE_IMAGE_URL, StatusData.C_TEXT };

  /* Implementation of LoaderManager.LoaderCallbacks */
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    // This is called when a new Loader needs to be created. This
    // sample only has one Loader, so we don't care about the ID.

    // Now create and return a CursorLoader that will take care of
    // creating a Cursor for the data being displayed.
    return new CursorLoader(getActivity(), StatusProvider.CONTENT_URI,
        PROJECTION, null, null, StatusData.C_CREATED_AT + " DESC");
  }

  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    // Swap the new cursor in. (The framework will take care of closing the
    // old cursor once we return.)
    mAdapter.swapCursor(data);
  }

  public void onLoaderReset(Loader<Cursor> loader) {
    // This is called when the last Cursor provided to onLoadFinished()
    // above is about to be closed. We need to make sure we are no
    // longer using it.
    mAdapter.swapCursor(null);
  }

  // Used to convert the url string to the profile image to the actual image
  // view. It is attached the adapter.
  static final ViewBinder VIEW_BINDER = new ViewBinder() {
    int IMAGE_COLUMN_INDEX = -1;
    DrawableManager drawableManager = new DrawableManager();

    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
      if (view.getId() != R.id.status_image)
        return false;

      // Initialize the index, so we don't do it every single time
      if (IMAGE_COLUMN_INDEX == -1)
        IMAGE_COLUMN_INDEX = cursor
            .getColumnIndex(StatusData.C_PROFILE_IMAGE_URL);
      String imageUrlString = cursor.getString(IMAGE_COLUMN_INDEX);
      ImageView image = (ImageView) view;
      drawableManager.fetchDrawableOnThread(imageUrlString, image);
      Log.d(TAG, "IMAGE: " + cursor.getString(IMAGE_COLUMN_INDEX));
      return true;

    }
  };
}
