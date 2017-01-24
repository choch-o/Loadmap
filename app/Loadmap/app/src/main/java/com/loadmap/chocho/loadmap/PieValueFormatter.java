package com.loadmap.chocho.loadmap;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by q on 2017-01-24.
 */

public class PieValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public PieValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        float millis = entry.getY();
        long minute = (long) (millis / (1000 * 60)) % 60;
        long hour = (long) (millis / (1000 * 60 * 60)) % 24;
        long day = (long) (millis / (1000 * 60 * 60 * 24)) % 365;
        if (day < 1)
            return String.format("%d시간 %d분 (%.1f%%)", hour, minute, value) ;
        else
            return String.format("%d일 %d시간 %d분 (%.1f%%)", day, hour, minute, value);
    }
}