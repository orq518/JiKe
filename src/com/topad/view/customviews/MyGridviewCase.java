package com.topad.view.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * The author 欧瑞强 on 2015/12/22.
 * todo
 */
public class MyGridviewCase extends GridView {

    public MyGridviewCase(Context context) {
        super(context);
    }
    public MyGridviewCase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyGridviewCase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec= MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

