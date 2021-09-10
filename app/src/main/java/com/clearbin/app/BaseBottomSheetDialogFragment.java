package com.clearbin.app;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment {
    protected int getColor(int colorId) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ContextCompat.getColor(getActivity(), colorId);
        } else {
            return getResources().getColor(colorId);
        }
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            Log.d("BOTTOMSHEET-DIALOGFRAG", "Exception", e);
        }
    }
}
