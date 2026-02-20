package com.example.interval;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class warning_dialog {

    public static void show(
            Context context,
            String title,
            String message,
            String positiveText,
            String negativeText,
            int iconResId,
            Runnable onPositiveClick
    ) {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        ImageView icon = dialog.findViewById(R.id.dialogIcon);
        TextView titleView = dialog.findViewById(R.id.dialogTitle);
        TextView messageView = dialog.findViewById(R.id.dialogMessage);
        Button positiveBtn = dialog.findViewById(R.id.dialogPositiveBtn);
        Button negativeBtn = dialog.findViewById(R.id.dialogNegativeBtn);

        icon.setImageResource(iconResId);
        titleView.setText(title);
        messageView.setText(message);
        positiveBtn.setText(positiveText);
        negativeBtn.setText(negativeText);

        positiveBtn.setOnClickListener(v -> {
            dialog.dismiss();
            if (onPositiveClick != null) {
                onPositiveClick.run();
            }
        });

        negativeBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
