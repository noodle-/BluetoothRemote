package com.hszuyd.noodle_.testing;

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
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		TextView textView = (TextView) findViewById(R.id.text_main_welcome);
		Intent iin = getIntent();
		Bundle b = iin.getExtras();
		name = (String) b.get("NAME_PLAYER");

		assert textView != null;
		textView.setText(getString(R.string.main_welcome, name));

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
	 * Creates the optionmenu with a MenuItem & sets the icon.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		mDynamicMenuIcon = menu.findItem(R.id.tb_bluetooth);
		mDynamicMenuIcon.setIcon(R.drawable.ic_favorite_white);
		return true;
	}

	/**
	 * Action when clicked on the icon/button on the toolbar.
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
		return super.onOptionsItemSelected(item);   // Why?
	}

	public void button_launch_kickpanel_OnClick(View view) {
		Intent intent = new Intent(getBaseContext(), KickPanelActivity.class);
		intent.putExtra("NAME_PLAYER", name);
		startActivity(intent);
	}

	public void button_launch_tribot_OnClick(View view) {
		Intent intent = new Intent(getBaseContext(), TribotActivity.class);
		intent.putExtra("NAME_PLAYER", name);
		startActivity(intent);
	}
}
