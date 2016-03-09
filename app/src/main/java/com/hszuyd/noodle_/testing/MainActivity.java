package com.hszuyd.noodle_.testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		/*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});*/

		textView = (TextView) findViewById(R.id.textView);
		Intent iin = getIntent();
		Bundle b = iin.getExtras();

		if (b != null) {
			String name = (String) b.get("NAME_PLAYER");
			textView.setText("Hallo " + name + "!");
		}
	}

	public void button_launch_kickpanel_OnClick(View view) {
		startActivity(new Intent(MainActivity.this, KickPanelActivity.class));
	}

	public void button_launch_tribot_OnClick(View view) {
		startActivity(new Intent(MainActivity.this, TribotActivity.class));
	}
}
