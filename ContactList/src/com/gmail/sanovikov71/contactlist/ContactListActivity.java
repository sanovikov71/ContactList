package com.gmail.sanovikov71.contactlist;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gmail.sanovikov71.contactlist.HeadlinesFragment.OnHeadlineSelectedListener;

public class ContactListActivity extends Activity implements
		OnHeadlineSelectedListener {

	private static final int ADD_ITEM_CODE = 1;
	private static final int EDIT_ITEM_CODE = 2;

	private FragmentTransaction fTrans;

	private boolean mDualPane;
	private int mCurCheckPosition = 0;

	private long listItemPosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		HeadlinesFragment mainFrag = new HeadlinesFragment();

		fTrans = getFragmentManager().beginTransaction();
		fTrans.add(R.id.headlines, mainFrag);
		fTrans.commit();

		View detailsFrame = findViewById(R.id.details);
		mDualPane = detailsFrame != null
				&& detailsFrame.getVisibility() == View.VISIBLE;

		if (savedInstanceState != null) {
			mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
		}

		if (mDualPane) {
			onArticleSelected(mCurCheckPosition);
		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bundle bundle;
		switch (requestCode) {
		case ADD_ITEM_CODE:
			if (data == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
				bundle = data.getExtras();
				createNewDBRecord(bundle.getString("fName"),
						bundle.getString("sName"), bundle.getString("phone"));
				updateHeadlinesFragment();
			}
			break;
		case EDIT_ITEM_CODE:
			if (data == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
				bundle = data.getExtras();
				editDBRecord(bundle.getString("fName"),
						bundle.getString("sName"), bundle.getString("phone"));
				updateHeadlinesFragment();
			}
			break;
		}

	}

	private void createNewDBRecord(String fName, String sName, String phone) {

		DBHelper dbHelper = DBHelper.getInstance(getApplication());
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_FIRST_NAME, fName);
		cv.put(DBHelper.COLUMN_SECOND_NAME, sName);
		cv.put(DBHelper.COLUMN_PHONE, phone);

		db.insert(DBHelper.TABLE_CONTACT_LIST, null, cv);

	}

	private void editDBRecord(String fName, String sName, String phone) {

		DBHelper dbHelper = DBHelper.getInstance(getApplication());
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_ID, listItemPosition + 1);
		cv.put(DBHelper.COLUMN_FIRST_NAME, fName);
		cv.put(DBHelper.COLUMN_SECOND_NAME, sName);
		cv.put(DBHelper.COLUMN_PHONE, phone);

		db.update(DBHelper.TABLE_CONTACT_LIST, cv, DBHelper.COLUMN_ID + " = "
				+ (listItemPosition + 1), null);

	}

	private void updateHeadlinesFragment() {

		HeadlinesFragment newHeadlinesFragment = new HeadlinesFragment();

		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		transaction.replace(R.id.headlines, newHeadlinesFragment);

		transaction.commit();

	}

	@Override
	public void onArticleSelected(int position) {

		mCurCheckPosition = position;

		if (mDualPane) {
			if (mCurCheckPosition != 0) {
				DetailsFragment details = (DetailsFragment) getFragmentManager()
						.findFragmentById(R.id.details);
				if (details == null || details.getShownIndex() != position) {
					details = DetailsFragment.newInstance(position);

					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					ft.replace(R.id.details, details);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					ft.commit();
				}
			}

		} else {

			Intent intent = new Intent();
			intent.setClass(this, ContactListActivity.ArticleActivity.class);
			intent.putExtra("index", position);
			startActivity(intent);

		}

	}

	@Override
	public void onAddButtonClicked() {

		Intent addItemIntent = new Intent(this, AddItemActivity.class);
		startActivityForResult(addItemIntent, ADD_ITEM_CODE);

	}

	@Override
	public void onEditContextItemClicked(long position) {

		listItemPosition = position;
		Intent editItemIntent = new Intent(this, EditItemActivity.class);
		editItemIntent.putExtra("position", position);
		Log.d("mylogs", String.valueOf(position));
		startActivityForResult(editItemIntent, EDIT_ITEM_CODE);

	}

	public static class ArticleActivity extends Activity {

		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
					&& (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
				finish();
				return;
			}

			if (savedInstanceState == null) {
				DetailsFragment details = new DetailsFragment();
				details.setArguments(getIntent().getExtras());
				getFragmentManager().beginTransaction()
						.add(android.R.id.content, details).commit();
			}

		}

	}

}