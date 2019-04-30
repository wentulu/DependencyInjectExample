// 生成代码，请勿修改
package com.stu.cusview.ruiz;

import android.app.Activity;
import cn.stu.cusview.ruiz.inject.R;
import java.lang.Exception;
import java.lang.reflect.Field;

public final class MainActivity_FindView {
  public static void findViewByIdAnnotation(Activity activity) {
    if(activity!=null) {
      Activity a = activity;
    }
    try {
         Field field = activity.getClass().getDeclaredField("tv");
        field.setAccessible(true);
        field.set(activity,activity.findViewById(R.id.tv));
        } catch (Exception e) {
         e.printStackTrace();
        };
    try {
         Field field = activity.getClass().getDeclaredField("btn");
        field.setAccessible(true);
        field.set(activity,activity.findViewById(R.id.btn));
        } catch (Exception e) {
         e.printStackTrace();
        };
  }
}
