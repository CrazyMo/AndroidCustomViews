package com.crazymo.views.util;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * auther: MO
 * Date: 2016/12/23
 * Time:13:30
 * Des:为了保证各种屏幕分辨率和屏幕密度下都能保持相同的比例
 */

public final class DisplayUtil {
    /**
     * px转为dip值保证尺寸大小不变
     * @param context
     * @param pxValue px像素值
     * @return
     */
    public static int pxTodip(@NonNull Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5F);
    }

    /**
     * dip转为px值保证尺寸大小不变
     * @param context
     * @param dipValue
     * @return
     */
    public static int dipTopx(@NonNull Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5F);
    }

    public static int pxTosp(@NonNull Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5F);
    }

    public static int spTopx(@NonNull Context context, float spValue){
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5F);
    }

}
