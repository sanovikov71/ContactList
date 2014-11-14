package com.gmail.sanovikov71.contactlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddItemActivity extends Activity implements OnClickListener {

	private EditText fName;
	private EditText sName;
	private EditText phone;
	private Button ok;
	private Button cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item_activity);

		Log.d("SNOV", "onCreate start");

		fName = (EditText) findViewById(R.id.edit_text_fName);
		sName = (EditText) findViewById(R.id.edit_text_sName);
		phone = (EditText) findViewById(R.id.edit_text_phone);
		ok = (Button) findViewById(R.id.btn_ok);
		cancel = (Button) findViewById(R.id.btn_cancel);

		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);

		setFinishOnTouchOutside(false);

	}

	@Override
	public void onClick(View v) {
		Log.d("SNOV", "onClick start");
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_ok:
			intent.putExtra("ok", true);
			intent.putExtra("fName", fName.getText().toString());
			intent.putExtra("sName", sName.getText().toString());
			intent.putExtra("phone", phone.getText().toString());
			setResult(RESULT_OK, intent);
			break;
		case R.id.btn_cancel:
			setResult(RESULT_CANCELED, intent);
			break;
		}
		Log.d("SNOV", "before finish");
		finish();
	}
}
