package com.dan.ui.adapter.impl;

import android.text.Spannable;

/**
 * Created by Bo on 2019/2/21 13:33
 */
public interface SpinnerTextFormatter {

    Spannable format(String text);

    Spannable format(Object item);
}
