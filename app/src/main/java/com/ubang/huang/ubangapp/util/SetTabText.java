package com.ubang.huang.ubangapp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import android.widget.TextView;

import com.ubang.huang.ubangapp.common.CP;

/**
 * Created by huang on 2019/1/29.
 *
 * @author = huangyouxin
 */

public class SetTabText {

    public static TextView setTextVIew(TextView convertView, int position, int type, Activity activity){

        TextView textView = convertView;
        switch (type){
            case CP.SEEKHELP:
                textView.setText(CP.SEEK_HELP_TAB[position]);
                break;
            case CP.HELPOTHERS:
                textView.setText(CP.Help_Others_Tab[position]);
                break;
            case CP.MyHelpList:
                textView.setText(CP.Help_Status[position]);
                break;
//            case "SEKKHELP_LAF_TAB":
//                textView.setText(CP.SEKKHELP_LAF_TAB[position]);
//                break;
//            case "CONTAIN_ROUND_TAB":
//                textView.setText(CP.CONTAIN_ROUND_TAB[position]);
//                break;
//            case "CONTAIN_LEARN_TAB":
//                textView.setText(CP.CONTAIN_LEARN_TAB[position]);
//                break;
            default:
                break;
        }
        int padding = dipToPix(activity);
        int width = getTextWidth(textView);
        //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
        //1.3f是根据上面字体大小变化的倍数1.3f设置
        textView.setWidth((int) (width * 1.3f) + padding);
        return textView;
    }

    private static int getTextWidth(TextView textView) {
        if (textView == null) {
            return 0;
        }
        Rect bounds = new Rect();
        String text = textView.getText().toString();
        Paint paint = textView.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.left + bounds.width();
    }

    /**
     * 根据dip值转化成px值
     * @param context
     * @return
     */
    private static int dipToPix(Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CP.DIP, context.getResources().getDisplayMetrics());
    }
}
