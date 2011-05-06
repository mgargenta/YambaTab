package com.marakana.yambatab;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

public class MainActivity extends Activity implements TabListener {
  private static final String TAG = "Yamba";
  private int mThemeId = -1;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Update theme, if previously set
    if( savedInstanceState != null ) {
      mThemeId = savedInstanceState.getInt("theme");
      setTheme(mThemeId);
    }

    setContentView(R.layout.main);

    // Set the action bar
    ActionBar bar = getActionBar();
    bar.addTab( bar.newTab().setText("Timeline" ).setTabListener(this) );
    bar.addTab( bar.newTab().setText("@Mentions" ).setTabListener(this) );
    
    bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO);
    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    bar.setDisplayShowHomeEnabled(true);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    SearchView searchView = (SearchView) menu.findItem(R.id.itemSearch).getActionView();
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.itemAuthorize:
      startActivity( new Intent(this, OAuthActivity.class) );
      return true;
    case R.id.toggleTheme:
      if(mThemeId == android.R.style.Theme_Holo_Light) {
        mThemeId = android.R.style.Theme_Holo;
      } else {
        mThemeId = android.R.style.Theme_Holo_Light;
      }
      Log.d(TAG, "mThemeId="+mThemeId);
      this.recreate();
      return true;
    case R.id.itemRefresh:
      startService(new Intent(this, UpdaterService.class) );
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

  /** Saves state of the activity before it gets recreated */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("theme", mThemeId);
  }

  /* TabListener callback when current tab was re-selected */
  public void onTabReselected(Tab tab, FragmentTransaction ft) {
    
  }
  /* TabListener callback when tab was selected */
  public void onTabSelected(Tab tab, FragmentTransaction ft) {
    
  }
  /* TabListener callback was unselected */
  public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    
  }

}