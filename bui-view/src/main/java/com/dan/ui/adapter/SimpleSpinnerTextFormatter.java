package com.dan.ui.adapter;

import android.text.Spannable;
import android.text.SpannableString;

import com.dan.ui.adapter.impl.SpinnerTextFormatter;

/**
 * Created by Bo on 2019/2/21 13:38
 */
public class SimpleSpinnerTextFormatter implements SpinnerTextFormatter {

    @Override
    public Spannable format(String text) {
        return new SpannableString(text);
    }

    @Override
    public Spannable format(Object item) {
        return new SpannableString(item.toString());
    }
}
