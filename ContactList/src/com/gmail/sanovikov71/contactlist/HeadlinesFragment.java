package com.gmail.sanovikov71.contactlist;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HeadlinesFragment extends ListFragment {

	OnHeadlineSelectedListener mCallback;

	private boolean mDualPane;
	private int mCurCheckPosition = 0;

	public interface OnHeadlineSelectedListener {
		public void onArticleSelected(int position);

		public void onAddButtonClicked();

		public void onEditContextItemClicked(long position);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (OnHeadlineSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		DBHelper dbh = DBHelper.getInstance(getActivity().getApplication());
		SQLiteDatabase db = dbh.getWritableDatabase();

		Cursor c = db.query(DBHelper.TABLE_CONTACT_LIST, null, null, null,
				null, null, null);

		String[] records = new String[c.getCount()];
		c.moveToFirst();
		while (!c.isAfterLast()) {
			records[c.getPosition()] = c.getString(1) + " " + c.getString(2);
			c.moveToNext();
		}

		c.close();

		registerForContextMenu(getListView());

		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_activated_1, records));

		View detailsFrame = getActivity().findViewById(R.id.details);
		mDualPane = detailsFrame != null
				&& detailsFrame.getVisibility() == View.VISIBLE;

		if (savedInstanceState != null) {
			mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
		}

		if (mDualPane) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curChoice", mCurCheckPosition);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (position == 0) {
			mCallback.onAddButtonClicked();
		} else {
			mCallback.onArticleSelected(position);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		if (item.getItemId() == R.id.context_menu_edit) {
			mCallback.onEditContextItemClicked(info.position);
			return true;
		} else {
			return super.onContextItemSelected(item);
		}
	}
}