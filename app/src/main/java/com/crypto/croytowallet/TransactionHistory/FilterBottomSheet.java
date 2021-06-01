package com.crypto.croytowallet.TransactionHistory;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FilterBottomSheet extends BottomSheetDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        //inflating layout
        View view = View.inflate(getContext(), R.layout.filter_bottomsheet_layout, null);

        //setting layout with bottom sheet
        bottomSheet.setContentView(view);


        return bottomSheet;
    }


}
