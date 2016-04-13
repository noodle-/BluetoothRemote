package com.hszuyd.noodle_.testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	private TextView textView;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		textView = (TextView) findViewById(R.id.textView);
		Intent iin = getIntent();
		Bundle b = iin.getExtras();
		name = (String) b.get("NAME_PLAYER");

		textView.setText("Hallo " + name + "!");
	}

	public void button_launch_kickpanel_OnClick(View view) {
		//startActivity(new Intent(MainActivity.this, KickPanelActivity.class));
	}

	public void button_launch_tribot_OnClick(View view) {
		Intent intent = new Intent(getBaseContext(), TribotActivity.class);
		intent.putExtra("NAME_PLAYER", name);
		startActivity(intent);
	}
}
