package edu.mobicom.lifeplus;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.app.Fragment;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the
 * ListView with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ListFragment extends Fragment implements
		AbsListView.OnItemClickListener {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_SECTION_NUMBER = "section_number";
	private static final String ARG_VALUES = "values";

	// TODO: Rename and change types of parameters
	private int mSectionNumber;
	private ArrayList<Task> mValues;
	private SwipeDetector swipe = new SwipeDetector();
	
	private static enum Action {
	    LR, // Left to Right
	    RL, // Right to Left
	    TB, // Top to bottom
	    BT, // Bottom to Top
	    None // when no action was detected
	}

	private OnFragmentInteractionListener mListener;

	/**
	 * The fragment's ListView/GridView.
	 */
	private AbsListView mListView;

	/**
	 * The Adapter which will be used to populate the ListView/GridView with
	 * Views.
	 */
	private ListAdapter mAdapter;

	// TODO: Rename and change types of parameters
	public static ListFragment newInstance(int sectionNumber, ArrayList<Task> values) {
		ListFragment fragment = new ListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		args.putSerializable(ARG_VALUES, values);
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ListFragment() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
			mValues = (ArrayList<Task>) getArguments().getSerializable(ARG_VALUES);
		}

		mAdapter = new CustomAdapter(getActivity(), mValues);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dailyquest, container,
				false);

		// Set the adapter
		mListView = (AbsListView) view.findViewById(android.R.id.list);
		mListView.setAdapter(mAdapter);

		// Set OnItemClickListener so we can be notified on item clicks
		mListView.setOnItemClickListener(this);
		mListView.setLongClickable(true);
		mListView.setOnTouchListener(swipe);
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int pos, long id) {
                // TODO Auto-generated method stub
            	RelativeLayout rl = (RelativeLayout)arg1;
            	TextView name = (TextView)rl.findViewById(R.id.itemName);
            	TextView desc = (TextView)rl.findViewById(R.id.itemDesc);
            	CheckBox cb = (CheckBox)rl.findViewById(R.id.itemCheck);
            	Toast.makeText(getActivity(),  "Edit " + name.getText().toString() + ".", Toast.LENGTH_SHORT).show();
                return true;
            }
        }); 
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = new OnFragmentInteractionListener() {
				
				@Override
				public void onFragmentInteraction(String id) {
					// TODO Auto-generated method stub
					
				}
			};
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (null != mListener) {
			// Notify the active callbacks interface (the activity, if the
			// fragment is attached to one) that an item has been selected.
			//mListener
			//		.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
			RelativeLayout rl = (RelativeLayout)view;
        	final TextView name = (TextView)rl.findViewById(R.id.itemName);
        	final TextView desc = (TextView)rl.findViewById(R.id.itemDesc);
        	final CheckBox cb = (CheckBox)rl.findViewById(R.id.itemCheck);
        	
        	if(swipe.swipeDetected()) {
                if(swipe.getAction() == Action.RL) {
                	Builder builder = new Builder(getActivity());
                	builder.setTitle("Delete task");
            		builder.setMessage("Are you sure you want to delete \"" + name.getText().toString() + "\"?");
            		
            		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            			@Override
            			public void onClick(DialogInterface dialog, int which) {
            					Toast.makeText(getActivity(), "Delete " + name.getText().toString() + ".", Toast.LENGTH_SHORT).show();
        				}
            				
        			});
            		
            		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            			
            			@Override
            			public void onClick(DialogInterface dialog, int which) {
            			}
            			
            		});
            		
            		builder.create().show();
                } 
            }     
		}
	}

	/**
	 * The default content for this Fragment has a TextView that is shown when
	 * the list is empty. If you would like to change the text, call this method
	 * to supply the text it should use.
	 */
	public void setEmptyText(CharSequence emptyText) {
		View emptyView = mListView.getEmptyView();

		if (emptyText instanceof TextView) {
			((TextView) emptyView).setText(emptyText);
		}
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
		public void onFragmentInteraction(String id);
	}
	
	
	
	public class SwipeDetector implements View.OnTouchListener {

	    private static final String logTag = "SwipeDetector";
	    private static final int MIN_DISTANCE = 100;
	    private float downX, downY, upX, upY;
	    private Action mSwipeDetected = Action.None;

	    public boolean swipeDetected() {
	        return mSwipeDetected != Action.None;
	    }

	    public Action getAction() {
	        return mSwipeDetected;
	    }

	    public boolean onTouch(View v, MotionEvent event) {
	        switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN: {
	            downX = event.getX();
	            downY = event.getY();
	            mSwipeDetected = Action.None;
	            return false; // allow other events like Click to be processed
	        }
	        case MotionEvent.ACTION_MOVE: {
	            upX = event.getX();
	            upY = event.getY();

	            float deltaX = downX - upX;
	            float deltaY = downY - upY;

	            // horizontal swipe detection
	            if (Math.abs(deltaX) > MIN_DISTANCE) {
	                // left or right
	                if (deltaX < 0) {
	                    mSwipeDetected = Action.LR;
	                    return true;
	                }
	                if (deltaX > 0) {
	                    mSwipeDetected = Action.RL;
	                    return true;
	                }
	            } else 

	                // vertical swipe detection
	                if (Math.abs(deltaY) > MIN_DISTANCE) {
	                    // top or down
	                    if (deltaY < 0) {
	                        mSwipeDetected = Action.TB;
	                        return false;
	                    }
	                    if (deltaY > 0) {
	                        mSwipeDetected = Action.BT;
	                        return false;
	                    }
	                } 
	            return true;
	        }
	        }
	        return false;
	    }
	}

}
