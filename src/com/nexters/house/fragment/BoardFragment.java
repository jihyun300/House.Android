package com.nexters.house.fragment;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;

import com.nexters.house.*;


public class BoardFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_board, container, false);

		return v;
	}


}