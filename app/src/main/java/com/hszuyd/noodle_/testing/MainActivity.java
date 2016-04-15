package com.hszuyd.noodle_.testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
	private General g = new General(MainActivity.this);
	private String name;

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
