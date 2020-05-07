package com.mobile.caro.TwoPlayersOnlineActivity.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mobile.caro.R;

public class WarningDialog extends DialogFragment {

    private String warning;
    private DialogInterface.OnDismissListener listener;

    public WarningDialog(String warning, DialogInterface.OnDismissListener listener) {
        this.warning = warning;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setCancelable(false);
        return new AlertDialog.Builder(getActivity())
                .setMessage(warning)
                .setTitle(R.string.warning)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onDismiss(dialog);
        }
    }
}
