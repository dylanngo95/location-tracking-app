package com.jundat95.locationtracking.Common;

import android.app.ProgressDialog;
import android.content.Context;

import com.jundat95.locationtracking.R;

/**
 * Created by tinhngo on 2/16/17.
 */

public class ProgressDialogLoading {

    private  ProgressDialog progressDialog;
    private  Context context;
    private  String  text;


    public ProgressDialogLoading(Context context, String text) {
        this.context = context;
        this.text = text;
    }

    public  void showProgressDialog(){
        progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(text);
        progressDialog.show();
    }

    public  void dismissProgressDialog(){
        progressDialog.dismiss();
    }

}
