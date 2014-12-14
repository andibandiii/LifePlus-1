package edu.mobicom.lifeplus;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link EditToDoFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link EditToDoFragment#newInstance} factory method to create an instance of
 * this fragment.
 *
 */
public class EditToDoFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_ITEM_ID = "item_id";

	// TODO: Rename and change types of parameters
	private String mItemID;

	private DatabaseManager db;
	private EditText etName;
	private EditText etDesc;
	private EditText etTime;
	private EditText etDur;
	private EditText etDate;
	private TextView tvStatus;
	private TextView tvDifficulty;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 *
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment EditToDoFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static EditToDoFragment newInstance(String item_id) {
		EditToDoFragment fragment = new EditToDoFragment();
		Bundle args = new Bundle();
		args.putString(ARG_ITEM_ID, item_id);
		fragment.setArguments(args);
		return fragment;
	}

	public EditToDoFragment() {
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
		View v = inflater.inflate(R.layout.fragment_edit_to_do, container,
				false);
		etName = (EditText) v.findViewById(R.id.et_edit_todo_name);
		etDesc = (EditText) v.findViewById(R.id.et_edit_todo_desc);
		etTime = (EditText) v.findViewById(R.id.et_edit_todo_time);
		etDur = (EditText) v.findViewById(R.id.et_edit_todo_duration);
		etDate = (EditText) v.findViewById(R.id.et_edit_todo_date);
		tvStatus = (TextView) v.findViewById(R.id.tv_edit_todo_status);
		tvDifficulty = (TextView) v
				.findViewById(R.id.tv_edit_todo_DifficultyValue);

		Task temp = null;
		for (Task t : db.getTodoList())
			if (t.getID() == Integer.parseInt(mItemID))
				temp = t;

		tvDifficulty.setText(temp.getDifficulty());
		etName.setText(temp.getName());
		etDesc.setText(temp.getDesc());
		etTime.setText(temp.getTime());
		etDur.setText(temp.getDuration());
		etDate.setText(temp.getDate().toString());
		if (temp.getStatus() == true)
			tvStatus.setText("Finished");
		else
			tvStatus.setText("Active");

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

		etDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Calendar c = Calendar.getInstance();
				int mYear = c.get(Calendar.YEAR);
				int mMonth = c.get(Calendar.MONTH);
				int mDay = c.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog dpd = new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								etDate.setText(monthOfYear + 1 + " "
										+ dayOfMonth + " " + year);

							}
						}, mYear, mMonth, mDay);

				dpd.show();
			}
		});

		if (temp.getStatus() == true)
			tvStatus.setText("Finished");
		else
			tvStatus.setText("Active");

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
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.done) {
			String name = etName.getText().toString();
			String desc = etDesc.getText().toString();

			if (name.isEmpty())
				Toast.makeText(getActivity(),
						"Please enter a name for the task.", Toast.LENGTH_SHORT)
						.show();
			else if (desc.isEmpty())
				Toast.makeText(getActivity(),
						"Please enter a description for the task.",
						Toast.LENGTH_SHORT).show();
			else {

//				Task editedTask = new Task(name, desc, tvDifficulty.getText()
//						.toString(), etDur.getText().toString(), etTime
//						.getText().toString(), 2, false, false, false);

//				db.editTask(Integer.parseInt(mItemID), editedTask);

				Fragment todo_fragment = CustomListFragment.newInstance(1,
						db.getTodoList());
				todo_fragment.setHasOptionsMenu(true);

				getActivity().getFragmentManager().beginTransaction()
						.replace(R.id.container, todo_fragment).commit();
			}
		} else if (item.getItemId() == R.id.cancel) {
			Fragment todo_fragment = CustomListFragment.newInstance(1,
					db.getTodoList());
			todo_fragment.setHasOptionsMenu(true);

			getActivity().getFragmentManager().beginTransaction()
					.replace(R.id.container, todo_fragment).commit();
		}

		return super.onOptionsItemSelected(item);
	}

}
