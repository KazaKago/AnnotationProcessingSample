package com.kazakago.annotationprocessing;

import android.content.Context;
import android.widget.Toast;

public abstract class ToastProvider {

    private Context context;

    public ToastProvider(Context context) {
        this.context = context;
    }

    protected void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
