package edu.mobicom.lifeplus;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the
 * {@link EditDailyQuestFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link EditDailyQuestFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class EditDailyQuestFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_ITEM_ID = "item_id";

	// TODO: Rename and change types of parameters
	private String mItemID;

	private DatabaseManager db;
	private EditText etName;
	private EditText etDesc;
	private EditText etTime;
	private EditText etDuration;
	private int diffPos;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment EditItemFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static EditDailyQuestFragment newInstance(String item_id) {
		EditDailyQuestFragment fragment = new EditDailyQuestFragment();
		Bundle args = new Bundle();
		args.putString(ARG_ITEM_ID, item_id);
		fragment.setArguments(args);
		return fragment;
	}

	public EditDailyQuestFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mItemID = getArguments().getString(ARG_ITEM_ID);
		}
		db = new DatabaseManager(getActivity(), Task.DATABASE_NAME, null, 1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_edit_daily_quest,
				container, false);

		etName = (EditText) v.findViewById(R.id.et_edit_daily_name);
		etDesc = (EditText) v.findViewById(R.id.et_edit_daily_desc);
		etTime = (EditText) v.findViewById(R.id.et_edit_daily_time);
		etDuration = (EditText) v.findViewById(R.id.et_edit_quest_duration);
		TextView tvStatus = (TextView) v
				.findViewById(R.id.tv_edit_daily_StatusUpdate);
		TextView tvDifficulty = (TextView) v
				.findViewById(R.id.tv_edit_quest_DifficultyValue);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.spinner_difficulty,
				android.R.layout.simple_spinner_item);

		Task temp = null;
		for (Task t : db.getDailyQuests())
			if (t.getID() == Integer.parseInt(mItemID))
				temp = t;

		etName.setText(temp.getName());
		etDesc.setText(temp.getDesc());
		etTime.setText(temp.getTime());
		etDuration.setText(temp.getDuration());

		if (temp.getStatus() == true)
			tvStatus.setText("Finished");
		else
			tvStatus.setText("Active");

		diffPos = temp.getDifficulty();
		tvDifficulty.setText(adapter.getItem(temp.getDifficulty()));

		etTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Calendar c = Calendar.getInstance();
				int mHour = c.get(Calendar.HOUR_OF_DAY);
				int mMinute = c.get(Calendar.MINUTE);

				// Launch Time Picker Dialog
				TimePickerDialog tpd = new TimePickerDialog(getActivity(),
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								// Display Selected time in textbox
								etTime.setText(hourOfDay
										+ ":"
										+ String.format("%02d%n", minute)
												.trim());
							}
						}, mHour, mMinute, false);
				tpd.show();
			}
		});

		return v;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = new OnFragmentInteractionListener() {

				@Override
				public void onFragmentInteraction(Uri uri) {
					// TODO Auto-generated method stub

				}
			};
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.list_done, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.done) {
			String name = etName.getText().toString();
			String desc = etDesc.getText().toString();

			if (name.isEmpty())
				Toast.makeText(getActivity(),
						"Please enter a name for the daily quest.",
						Toast.LENGTH_SHORT).show();
			else if (desc.isEmpty())
				Toast.makeText(getActivity(),
						"Please enter a description for the daily quest.",
						Toast.LENGTH_SHORT).show();
			else {

				Task editedQuest = new Task(name, desc, diffPos, etDuration
						.getText().toString(), etTime.getText().toString(), 1,
						false, false, false);

				db.editTask(Integer.parseInt(mItemID), editedQuest);

				Fragment daily_quest_fragment = CustomListFragment.newInstance(
						1, db.getDailyQuests());
				daily_quest_fragment.setHasOptionsMenu(true);

				getActivity().getFragmentManager().beginTransaction()
						.replace(R.id.container, daily_quest_fragment).commit();
			}
		} else if (item.getItemId() == R.id.cancel) {
			Fragment daily_quest_fragment = CustomListFragment.newInstance(1,
					db.getDailyQuests());
			daily_quest_fragment.setHasOptionsMenu(true);

			getActivity().getFragmentManager().beginTransaction()
					.replace(R.id.container, daily_quest_fragment).commit();
		}

		return super.onOptionsItemSelected(item);
	}

}
