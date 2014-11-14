package com.gmail.sanovikov71.contactlist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends Activity implements OnClickListener {

	private EditText fName;
	private EditText sName;
	private EditText phone;
	private Button ok;
	private Button cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_item_activity);

		fName = (EditText) findViewById(R.id.edit_text_fName);
		sName = (EditText) findViewById(R.id.edit_text_sName);
		phone = (EditText) findViewById(R.id.edit_text_phone);
		ok = (Button) findViewById(R.id.btn_ok);
		cancel = (Button) findViewById(R.id.btn_cancel);

		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);

		setFinishOnTouchOutside(false);

		long position;
		Bundle extras;
		if (savedInstanceState == null) {
			extras = getIntent().getExtras();
			if (extras == null) {
				position = 1;
			} else {
				position = extras.getLong("position");
			}
		} else {
			position = (Long) savedInstanceState.getSerializable("position");
		}

		DBHelper dbh = DBHelper.getInstance(getApplication());
		SQLiteDatabase db = dbh.getWritableDatabase();

		Cursor c = db.query(DBHelper.TABLE_CONTACT_LIST, null,
				DBHelper.COLUMN_ID + " = " + (position + 1), null, null, null,
				null);

		if (c.getCount() > 0) {

			int fNameColumn = c.getColumnIndex(DBHelper.COLUMN_FIRST_NAME);
			int sNameColumn = c.getColumnIndex(DBHelper.COLUMN_SECOND_NAME);
			int phoneColumn = c.getColumnIndex(DBHelper.COLUMN_PHONE);

			c.moveToFirst();

			fName.setText(c.getString(fNameColumn));
			sName.setText(c.getString(sNameColumn));
			phone.setText(c.getString(phoneColumn));

		}

		c.close();

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
