package com.wang.wheel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.wang.wheel.adapters.NumericWheelAdapter;
import com.wang.wheel.listener.OnWheelChangedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import kankan.wheel.R;

/**
 * Author: wangxiaojie6
 * Date: 2017/10/13.
 */

public class IOSTimePickerView extends LinearLayout implements OnWheelChangedListener {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final int DEFAULT_START_YEAR = 1900;
    private static final int DEFAULT_END_YEAR = 2100;

    private IOSWheelView mYearWheel;
    private IOSWheelView mMonthWheel;
    private IOSWheelView mDayWheel;

    private NumericWheelAdapter mYearAdapter;
    private NumericWheelAdapter mMonthAdapter;
    private NumericWheelAdapter mDayAdapter;

    private final DateFormat mDateFormat = new SimpleDateFormat(DATE_FORMAT);

    private Calendar mMinDate;
    private Calendar mMaxDate;
    private Calendar mCurrentDate;

    public IOSTimePickerView(Context context) {
        super(context);
        init(context, null);
    }

    public IOSTimePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public IOSTimePickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IOSTimePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.time_picker_ios, this, true);
        mYearWheel = findViewById(R.id.year_wheel);
        mMonthWheel = findViewById(R.id.month_wheel);
        mDayWheel = findViewById(R.id.day_wheel);

        mYearWheel.setOnWheelChangedListener(this);
        mMonthWheel.setOnWheelChangedListener(this);
        mDayWheel.setOnWheelChangedListener(this);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IOSTimePickerView, 0, 0);
        int startYear = typedArray.getInt(R.styleable.IOSTimePickerView_itp_startYear, DEFAULT_START_YEAR);
        int endYear = typedArray.getInt(R.styleable.IOSTimePickerView_itp_endYear, DEFAULT_END_YEAR);
        String minDate = typedArray.getString(R.styleable.IOSTimePickerView_itp_minDate);
        String maxDate = typedArray.getString(R.styleable.IOSTimePickerView_itp_maxDate);
        typedArray.recycle();

        initCalendar(Locale.getDefault(), startYear, endYear, minDate, maxDate);
        mYearAdapter = new NumericWheelAdapter(context, mMinDate.get(Calendar.YEAR), mMaxDate.get(Calendar.YEAR), "%d年");
        mYearWheel.setAdapter(mYearAdapter);
        mMonthAdapter = new NumericWheelAdapter(context, 0, 0, "%d月");
        mDayAdapter = new NumericWheelAdapter(context, 0, 0, "%d日");
        updateWheel();
    }


    protected void initCalendar(Locale locale, int startYear, int endYear, String minDate, String maxDate) {
        mMinDate = Calendar.getInstance(locale);
        mMaxDate = Calendar.getInstance(locale);
        mCurrentDate = Calendar.getInstance(locale);

        mCurrentDate.setTimeInMillis(System.currentTimeMillis());

        if (!TextUtils.isEmpty(minDate)) {
            if (!parseDate(minDate, mMinDate)) {
                mMinDate.set(startYear, 0, 1);
            }
        } else {
            mMinDate.set(startYear, 0, 1);
        }
        if (mCurrentDate.before(mMinDate)) {
            mCurrentDate.setTimeInMillis(mMinDate.getTimeInMillis());
        }

        if (!TextUtils.isEmpty(maxDate)) {
            if (!parseDate(maxDate, mMaxDate)) {
                mMaxDate.set(endYear, 11, 31);
            }
        } else {
            mMaxDate.set(endYear, 11, 31);
        }
        if (mCurrentDate.after(mMaxDate)) {
            mCurrentDate.setTimeInMillis(mMaxDate.getTimeInMillis());
        }

    }


    private void judgeDate() {
        if (mCurrentDate.before(mMinDate)) {
            mCurrentDate.setTimeInMillis(mMinDate.getTimeInMillis());
        } else if (mCurrentDate.after(mMaxDate)) {
            mCurrentDate.setTimeInMillis(mMaxDate.getTimeInMillis());
        }
    }

    private boolean parseDate(String date, Calendar outDate) {
        try {
            outDate.setTime(mDateFormat.parse(date));
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateYear() {
        mYearWheel.setCurrentItem(mCurrentDate.get(Calendar.YEAR) - mMinDate.get(Calendar.YEAR));
    }

    public void updateMonth() {
        if (mCurrentDate.equals(mMinDate)) {
            mMonthAdapter.setMinValue(mCurrentDate.get(Calendar.MONTH) + 1);
            mMonthAdapter.setMaxValue(mCurrentDate.getActualMaximum(Calendar.MONTH) + 1);
            mMonthWheel.setAdapter(mMonthAdapter);
            mMonthWheel.setCurrentItem(0);
        } else if (mCurrentDate.equals(mMaxDate)) {
            int min = mCurrentDate.getActualMinimum(Calendar.MONTH);
            int current = mCurrentDate.get(Calendar.MONTH);
            mMonthAdapter.setMinValue(min + 1);
            mMonthAdapter.setMaxValue(current + 1);
            mMonthWheel.setAdapter(mMonthAdapter);
            mMonthWheel.setCurrentItem(current - min);
        } else {
            mMonthAdapter.setMinValue(1);
            mMonthAdapter.setMaxValue(12);
            mMonthWheel.setAdapter(mMonthAdapter);
            mMonthWheel.setCurrentItem(mCurrentDate.get(Calendar.MONTH));
        }
    }

    public void updateDay() {
        if (mCurrentDate.equals(mMinDate)) {
            mDayAdapter.setMinValue(mCurrentDate.get(Calendar.DAY_OF_MONTH));
            mDayAdapter.setMaxValue(mCurrentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            mDayWheel.setAdapter(mDayAdapter);
            mDayWheel.setCurrentItem(0);
        } else if (mCurrentDate.equals(mMaxDate)) {
            int min = mCurrentDate.getActualMinimum(Calendar.DAY_OF_MONTH);
            int current = mCurrentDate.get(Calendar.DAY_OF_MONTH);
            mDayAdapter.setMinValue(min);
            mDayAdapter.setMaxValue(current);
            mDayWheel.setAdapter(mDayAdapter);
            mDayWheel.setCurrentItem(current - min);
        } else {
            mDayAdapter.setMinValue(1);
            mDayAdapter.setMaxValue(mCurrentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            mDayWheel.setAdapter(mDayAdapter);
            mDayWheel.setCurrentItem(mCurrentDate.get(Calendar.DAY_OF_MONTH) - 1);
        }
    }

    private void updateWheel() {
        mYearWheel.setCurrentItem(mCurrentDate.get(Calendar.YEAR) - mMinDate.get(Calendar.YEAR));
        if (mCurrentDate.equals(mMinDate)) {
            mMonthAdapter.setMinValue(mCurrentDate.get(Calendar.MONTH) + 1);
            mMonthAdapter.setMaxValue(mCurrentDate.getActualMaximum(Calendar.MONTH) + 1);
            mMonthWheel.setAdapter(mMonthAdapter);
            mMonthWheel.setCurrentItem(0);
            mDayAdapter.setMinValue(mCurrentDate.get(Calendar.DAY_OF_MONTH));
            mDayAdapter.setMaxValue(mCurrentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            mDayWheel.setAdapter(mDayAdapter);
            mDayWheel.setCurrentItem(0);
        } else if (mCurrentDate.equals(mMaxDate)) {
            int min = mCurrentDate.getActualMinimum(Calendar.MONTH);
            int current = mCurrentDate.get(Calendar.MONTH);
            mMonthAdapter.setMinValue(min + 1);
            mMonthAdapter.setMaxValue(current + 1);
            mMonthWheel.setAdapter(mMonthAdapter);
            mMonthWheel.setCurrentItem(current - min);
            min = mCurrentDate.getActualMinimum(Calendar.DAY_OF_MONTH);
            current = mCurrentDate.get(Calendar.DAY_OF_MONTH);
            mDayAdapter.setMinValue(min);
            mDayAdapter.setMaxValue(current);
            mDayWheel.setAdapter(mDayAdapter);
            mDayWheel.setCurrentItem(current - min);
        } else {
            mMonthAdapter.setMinValue(1);
            mMonthAdapter.setMaxValue(12);
            mMonthWheel.setAdapter(mMonthAdapter);
            mMonthWheel.setCurrentItem(mCurrentDate.get(Calendar.MONTH));
            mDayAdapter.setMinValue(1);
            mDayAdapter.setMaxValue(mCurrentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            mDayWheel.setAdapter(mDayAdapter);
            mDayWheel.setCurrentItem(mCurrentDate.get(Calendar.DAY_OF_MONTH) - 1);
        }

    }

    @Override
    public void onChanged(View wheel, int oldValue, int newValue) {
        if (wheel == mDayWheel) {
            Log.e("fuck", "name day" + " old " + oldValue + " new " + newValue);
            mCurrentDate.add(Calendar.DAY_OF_MONTH, newValue - oldValue);
        } else if (wheel == mMonthWheel) {
            Log.e("fuck", "name month" + " old " + oldValue + " new " + newValue);
            mCurrentDate.add(Calendar.MONTH, newValue - oldValue);
            judgeDate();
            updateDay();
        } else if (wheel == mYearWheel) {
            Log.e("fuck", "name year" + " old " + oldValue + " new " + newValue);
            mCurrentDate.add(Calendar.YEAR, newValue - oldValue);
            judgeDate();
            updateDay();
        }
    }

    public Calendar getCurrentDate() {
        return mCurrentDate;
    }
}
