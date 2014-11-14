package com.gmail.sanovikov71.contactlist;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

	private static final float TEXT_SIZE = 32;

	public static DetailsFragment newInstance(int index) {
		DetailsFragment f = new DetailsFragment();

		Bundle args = new Bundle();
		args.putInt("index", index);
		f.setArguments(args);

		return f;
	}

	public int getShownIndex() {
		return getArguments().getInt("index", 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View v = inflater.inflate(R.layout.details_fragment, container, false);
		TextView fName = (TextView) v.findViewById(R.id.fName);
		TextView sName = (TextView) v.findViewById(R.id.sName);
		TextView phone = (TextView) v.findViewById(R.id.phone);
		fName.setTextSize(TypedValue.COMPLEX_UNIT_PX, TEXT_SIZE);
		sName.setTextSize(TypedValue.COMPLEX_UNIT_PX, TEXT_SIZE);
		phone.setTextSize(TypedValue.COMPLEX_UNIT_PX, TEXT_SIZE);

		DBHelper dbh = DBHelper.getInstance(getActivity().getApplication());
		SQLiteDatabase db = dbh.getWritableDatabase();

		Cursor c = db.query(DBHelper.TABLE_CONTACT_LIST, null,
				DBHelper.COLUMN_ID + " = " + (getShownIndex() + 1), null, null,
				null, null);

		if (c.getCount() > 0) {
			
			int fNameColumn = c.getColumnIndex(DBHelper.COLUMN_FIRST_NAME);
			int sNameColumn = c.getColumnIndex(DBHelper.COLUMN_SECOND_NAME);
			int phoneColumn = c.getColumnIndex(DBHelper.COLUMN_PHONE);
			
			c.moveToFirst();

			fName.setText(c.getString(fNameColumn));
			sName.setText(c.getString(sNameColumn));
			phone.setText(c.getString(phoneColumn));

		}

		return v;

	}

}
