package com.deepoove.restclient.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deepoove.restclient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sayi
 */

public class RadioBoxView extends FrameLayout {

    int mSelectPosition = 0;
    int mSelectBackgroundColor;

    String[] radioStr;
    List<LinearLayout> mLayouts = new ArrayList<>();
    List<TextView> mTextViews = new ArrayList<>();
    List<Integer> mSelectColorList = new ArrayList<>();

    OnSelectListener mListener;

    public RadioBoxView(Context context) {
        super(context);
        initialise(null);
    }

    public RadioBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise(attrs);
    }

    public RadioBoxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialise(attrs);
    }

    private void initialise(AttributeSet attrs) {
        String radioTextArray = null;
        int selectColor = 0;
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.RadioBox);

        try {
            if (a != null) {
                radioTextArray = a.getString(R.styleable.RadioBox_textArray);
                radioTextArray = (radioTextArray == null) ? "" : radioTextArray;
                selectColor = a.getColor(R.styleable.RadioBox_selectColor, Color.parseColor("#ffffff"));

                int resourceId = a.getResourceId(R.styleable.RadioBox_selectColorList, 0);
                if (resourceId != 0){
                    int[] intArray = getResources().getIntArray(resourceId);
                    for (int i = 0; i < intArray.length; i++) {
                        mSelectColorList.add(intArray[i]);

                    }
                }
            }
        } finally {
            if (a != null) {
                a.recycle();
            }
        }


        radioStr = radioTextArray.split(";");
        mSelectBackgroundColor = selectColor;

        LayoutInflater inflater = LayoutInflater.from(getContext());


        LinearLayout v = (LinearLayout)inflater.inflate(R.layout.radio_box_view, this, false);

        for (int i = 0; i < radioStr.length; i++) {
            LinearLayout layout = new LinearLayout(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1f;
            layout.setLayoutParams(layoutParams);
            layout.setGravity(Gravity.CENTER);

            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(radioStr[i]);
            layout.addView(textView);
            v.addView(layout);
            if (i != radioStr.length - 1){
                View view = new View(getContext());
                view.setLayoutParams(new ViewGroup.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT));
                view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                v.addView(view);
            }
            mLayouts.add(layout);
            mTextViews.add(textView);
            final int position = i;
            final String text = radioStr[i];
            layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(position, text);
                }
            });

        }
        this.addView(v);
    }

    private void click(int position, String text) {
        mSelectPosition = position;
        this.requestFocus();
        for (LinearLayout layout : mLayouts){
            layout.setBackground(null);
        }
        for (TextView textView : mTextViews){
            textView.setTextColor(Color.parseColor("#666666"));
        }

        View v = mLayouts.get(position);
        int currentSelectColor = 0;
        if (position < mSelectColorList.size()){
            currentSelectColor = mSelectColorList.get(position);
        }else{
            currentSelectColor = mSelectBackgroundColor;
        }

        int radius = dp2px(10, getContext().getResources().getDisplayMetrics());

        if (0 == position){
            RoundRectShape shape = new RoundRectShape(new float[]{radius, radius, 0, 0, 0, 0, radius, radius}, null, null);
            ShapeDrawable drawable = new ShapeDrawable(shape);
            drawable.getPaint().setColor(currentSelectColor);
            v.setBackground(drawable);
        }else if (radioStr.length - 1 == position){
            RoundRectShape shape = new RoundRectShape(new float[]{0, 0, radius, radius, radius, radius, 0, 0}, null, null);
            ShapeDrawable drawable = new ShapeDrawable(shape);
            drawable.getPaint().setColor(currentSelectColor);
            v.setBackground(drawable);
        }else {
            v.setBackgroundColor(currentSelectColor);
        }
        mTextViews.get(position).setTextColor(Color.parseColor("#ffffff"));

        if (null != mListener){
            mListener.onSelect(position, text);
        }

    }

    public static int dp2px(int dp, DisplayMetrics displayMetrics) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }
    public void setOnSelectListener(OnSelectListener listener){
        mListener = listener;
    }

    public void setSelectPosition(int position, String text) {
        click(position, text);
    }

    public interface OnSelectListener{
        void onSelect(int position, String text);
    }

    public int getmSelectPosition() {
        return mSelectPosition;
    }
}
