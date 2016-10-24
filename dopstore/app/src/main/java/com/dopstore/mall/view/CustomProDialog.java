package com.dopstore.mall.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.dopstore.mall.R;


public class CustomProDialog extends Dialog {

    public View view;
    private Context c;


    public CustomProDialog(Context paramContext) {
        super(paramContext, paramContext.getResources().getIdentifier("dialog", "style", paramContext.getPackageName()));
        view = View.inflate(paramContext, R.layout.customprodialog1, null);
        this.setContentView(view);
        this.c = paramContext;
    }

    public void cancel() {
        if (!isShowing())
            return;
        super.cancel();
    }

    public void show() {
        if (isShowing())
            return;
        super.show();
    }


}
