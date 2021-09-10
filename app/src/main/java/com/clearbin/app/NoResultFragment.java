package com.clearbin.app;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoResultFragment extends BaseBottomSheetDialogFragment {

    public NoResultFragment() {
        // Required empty public constructor
    }

    public static NoResultFragment newInstance() {
        NoResultFragment fragment = new NoResultFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_no_result, container, false);

        // Get the views and attach the listener
        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Oswald-Regular.ttf");

        ((TextView) view.findViewById(R.id.materialLabel)).setTypeface(customFont);
        ((TextView) view.findViewById(R.id.materialText)).setTypeface(customFont);

        TextView searchButton = (TextView) view.findViewById(R.id.searchButton);
        searchButton.setTypeface(customFont);
        searchButton.setTextColor(getColor(R.color.no_color));
        searchButton.setBackgroundResource(R.drawable.button_border);

        searchButton.setOnClickListener(view1 -> {
            // Navigate to Search Activity
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);

            // getActivity().finish();
        });

        return view;
    }
}