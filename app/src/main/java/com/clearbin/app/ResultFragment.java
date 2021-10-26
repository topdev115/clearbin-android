package com.clearbin.app;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends BaseBottomSheetDialogFragment {

    private static final String MATERIAL_PARAM = "MATERIAL_PARAM";
    private static final String RESULT_PARAM = "RESULT_PARAM";

    private String mMaterial;
    private boolean mResult;

    public ResultFragment() {
        // Required empty public constructor
    }

    public static ResultFragment newInstance(String material, boolean result) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(MATERIAL_PARAM, material);
        args.putBoolean(RESULT_PARAM, result);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMaterial = getArguments().getString(MATERIAL_PARAM, "");
            mResult = getArguments().getBoolean(RESULT_PARAM, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        // Get the views and attach the listener
        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Oswald-Regular.ttf");
        int textColor = getColor(mResult ? R.color.true_color : R.color.false_color);

        ((TextView) view.findViewById(R.id.materialLabel)).setTypeface(customFont);
        TextView materialText = (TextView) view.findViewById(R.id.materialText);
        materialText.setTypeface(customFont);
        materialText.setText(mMaterial);
        materialText.setTextColor(textColor);

        ((TextView) view.findViewById(R.id.recyclableLabel)).setTypeface(customFont);
        TextView recyclableText = (TextView) view.findViewById(R.id.recyclableText);
        recyclableText.setTypeface(customFont);
        recyclableText.setText(mResult ? R.string.yes : R.string.no);
        recyclableText.setTextColor(textColor);

        ((TextView) view.findViewById(R.id.qualityLabel)).setTypeface(customFont);
        TextView descriptionText = (TextView) view.findViewById(R.id.description);
        descriptionText.setTypeface(customFont);
        descriptionText.setTextColor(textColor);

        return view;
    }

    private View bottomSheet;
    @Override public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //     bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            // }
            View view = getView();
            view.post(() -> {
                View parent = (View) view.getParent();
                CoordinatorLayout.LayoutParams params =
                        (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
                CoordinatorLayout.Behavior behavior = params.getBehavior();
                BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
                bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());

                ((View) bottomSheet.getParent()).setBackgroundColor(Color.TRANSPARENT);
            });
        }
    }
}