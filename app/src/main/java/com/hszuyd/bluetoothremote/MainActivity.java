package com.hszuyd.bluetoothremote;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
	private General g = new General(MainActivity.this);
	private String name;
	private MenuItem mDynamicMenuIcon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Find the views and their ID's
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		TextView textView = (TextView) findViewById(R.id.text_main_welcome);
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

		setSupportActionBar(mToolbar);                                          // Cast toolbar as actionbar
		getSupportActionBar().setDisplayShowHomeEnabled(true);                  // Show home/back button
		mToolbar.setNavigationIcon(R.drawable.back_test);    // Set back button icon
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {      // Initialize the onclick listener to navigate back
			@Override
			public void onClick(View v) {
				supportFinishAfterTransition();                                 // Finish activity with animation
			}
		});

		// Get the player name from login activity and show it
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		name = (String) b.get("NAME_PLAYER");
		assert textView != null;
		textView.setText(getString(R.string.main_welcome, name));

		// Create the floating action button (=fab) with its OnClickListener
		if (fab != null)
			fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Snackbar.make(view, "This is a snackbar!", Snackbar.LENGTH_LONG)
							.setAction("SHOW TOAST", new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									g.showToast("This is a Toast!");
								}
							}).show();
				}
			});
	}

	/**
	 * Creates the optionMenu with a MenuItem & sets the icon.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		mDynamicMenuIcon = menu.findItem(R.id.tb_bluetooth);
		mDynamicMenuIcon.setIcon(R.drawable.ic_favorite_white);
		return true;
	}

	/**
	 * Action when clicking on the icon/button on the toolbar.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();      // get ID of the clicked button so we know where we've clicked and act based on that
		if (id == R.id.tb_bluetooth) {
			if (Math.random() > 0.5) {  // Show a different icon based on random int between 0.0 and 1.0
				mDynamicMenuIcon.setIcon(R.drawable.ic_thumb_up);
			} else {
				mDynamicMenuIcon.setIcon(R.drawable.ic_thumb_down);
			}
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Launch KickPanelActivity and give it the value of name
	 */
	public void buttonLaunchKickpanelOnClick(View view) {
		Intent intent = new Intent(getBaseContext(), KickPanelActivity.class);  // Create intent for going from here to KickPanel
		intent.putExtra("NAME_PLAYER", name);                                   // Add player name to the intent as Extra
		ActivityOptions transitionActivityOptions =                             // Set animation options
				ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, view, "ToKickPanel");
		startActivity(intent, transitionActivityOptions.toBundle());            // Start new activity with player name and animation
	}

	/**
	 * Launch TribotActivity and give it the value of name
	 */
	public void buttonLaunchTribotOnClick(View view) {
		Intent intent = new Intent(getBaseContext(), TribotActivity.class);
		intent.putExtra("NAME_PLAYER", name);
		startActivity(intent);
	}

	/**
	 * Launch TribotNewActivity
	 */
	public void buttonLaunchTribotNewOnClick(View view) {
		Intent intent = new Intent(getBaseContext(), TribotNewActivity.class);
		startActivity(intent);
	}
}
