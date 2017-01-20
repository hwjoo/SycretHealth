package com.muhanbit.sycrethealth.view;

import android.content.Context;

/**
 * Created by hwjoo on 2017-01-18.
 */

public interface RecordFragView {
    Context getViewContext();
    void showSnackBar(String message);
    void progressOnOff(boolean on);
    void showDeleteDialog(int position);
}
