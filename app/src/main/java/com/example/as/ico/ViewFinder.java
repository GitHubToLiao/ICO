package com.example.as.ico;

import android.app.Activity;
import android.view.View;

/**
 * Created by as on 2017/6/21.
 */

public class ViewFinder {
    private Activity mActivity;
    private View mView;
    public ViewFinder(Activity activity) {
        this.mActivity =activity;
    }

    public ViewFinder(View view) {
        this.mView =mView;
    }

    public View findViewById(int viewID){
        return mActivity !=null?mActivity.findViewById(viewID):mView.findViewById(viewID);
    }
}
