package edu.mobicom.lifeplus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link AddToDoFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link AddToDoFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class AddToDoFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private OnFragmentInteractionListener mListener;

	private DatabaseManager db;
	private EditText etName;
	private EditText etDesc;
	private EditText etTime;
	private EditText etDur;
	private EditText etDate;
	private Spinner spDifficulty;
	private ImageButton ibNew;
	private ImageButton ibExisting;
	private ImageView ivImage;
	private static int RESULT_LOAD_IMAGE = 1;
	private static final int CAMERA_REQUEST = 1888;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment AddToDoFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AddToDoFragment newInstance() {
		AddToDoFragment fragment = new AddToDoFragment();
		return fragment;
	}

	public AddToDoFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DatabaseManager(getActivity(), Task.DATABASE_NAME, null, 1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater
				.inflate(R.layout.fragment_add_to_do, container, false);
		etName = (EditText) v.findViewById(R.id.et_add_todo_name);
		etDesc = (EditText) v.findViewById(R.id.et_add_todo_desc);
		etTime = (EditText) v.findViewById(R.id.et_add_todo_time);
		etDur = (EditText) v.findViewById(R.id.et_add_todo_duration);
		etDate = (EditText) v.findViewById(R.id.et_add_todo_date);
		spDifficulty = (Spinner) v.findViewById(R.id.sp_add_todo);
		ibNew = (ImageButton) v.findViewById(R.id.ib_add_todo_capture);
		ibExisting = (ImageButton) v.findViewById(R.id.ib_add_todo_browse);
		ivImage = (ImageView) v.findViewById(R.id.iv_add_todo);

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
								etTime.setText(hourOfDay + ":"
										+ String.format("%02d%n", minute));
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
				int mMonth = c.get(Calendar.MONTH);
				int mYear = c.get(Calendar.YEAR);
				int mDay = c.get(Calendar.DAY_OF_MONTH);

				// Launch Time Picker Dialog
				DatePickerDialog tpd = new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								etDate.setText(monthOfYear + 1 + " "
										+ dayOfMonth + " " + year);

							}
						}, mYear, mMonth, mDay);
				tpd.show();

			}
		});

		ibExisting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});

		ibNew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
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

				Task newTodo = new Task(name, desc, spDifficulty.getSelectedItemPosition(), etDur.getText()
						.toString(), etTime.getText().toString(), 2, false,
						false, false);
				Log.i("Difficulty", spDifficulty.getSelectedItem().toString()
						+ " REMOVE THIS LINE; AddDailyQuestFragment.java:191");

				db.addTask(newTodo);

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
