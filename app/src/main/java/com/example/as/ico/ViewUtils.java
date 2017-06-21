package com.example.as.ico;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by as on 2017/6/21.
 */

public class ViewUtils {
    public static void inject(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }

    public static void inject(View view) {
        inject(new ViewFinder(view), view);
    }

    public static void inject(View view, Object object) {
        inject(new ViewFinder(view), object);
    }

    //object 需要反射的类
    public static void inject(ViewFinder finder, Object object) {
        injectView(finder, object);
        injectEvent(finder,object);
    }

    private static void injectEvent(ViewFinder finder, Object object) {
        //获取类
        Class<?> clazz = object.getClass();
        //获取所用方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if(onClick !=null){
                int[] viewIds = onClick.value();
                if(viewIds !=null) {
                    for (int viewId : viewIds) {
                        View view = finder.findViewById(viewId);
                        CheckNet checkNet = method.getAnnotation(CheckNet.class);
                        boolean isCheckNet =checkNet !=null;
                        String noNetHint =null;
                        if(checkNet !=null){
                             noNetHint =checkNet.value();
                        }
                        Log.d("test","isCheckNet"+isCheckNet);
                        if(view !=null){
                             view.setOnClickListener(new DeclareOnClickListener(method,object,isCheckNet,noNetHint));
                        }
                    }
                }
            }
        }

    }

    private static class  DeclareOnClickListener implements View.OnClickListener{
        private Object mObject;
        private Method mMethod;
        private boolean isCheckNet;
        private String noNetHint;

        public DeclareOnClickListener(Method mMethod, Object mObject,boolean isCheckNet,String noNetHint) {
            this.mMethod = mMethod;
            this.mObject = mObject;
            this.isCheckNet = isCheckNet;
            this.noNetHint =noNetHint;
        }

        @Override
        public void onClick(View v) {
            mMethod.setAccessible(true);
            if(isCheckNet){
                Log.d("test","aaaaaaa");
                if(!networkAvailable(v.getContext())){
                    if(TextUtils.isEmpty(noNetHint)) {
                        Toast.makeText(v.getContext(), "请检测网络", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(v.getContext(), noNetHint, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }
            try {
                mMethod.invoke(mObject,v);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMethod.invoke(mObject,null);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

    /**
     * 组件注解
     * @param finder 帮助类
     * @param object 需要反射的类
     */
    private static void injectView(ViewFinder finder, Object object) {
        //通过反射获取到类
        Class<?> clazz = object.getClass();
        //获取所有属性包括公有和私有
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            //获取到注解的类
            ViewById viewById = field.getAnnotation(ViewById.class);
            //获取注解里面的属性
            int viewId = viewById.value();
            View view = finder.findViewById(viewId);
            field.setAccessible(true);
            if(view !=null){
                try {
                    field.set(object,view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 判断网络是否连接
     * @param context 上下文
     * @return 网络是否连接 true表示连接上  false表示没有连接
     */
    private static boolean networkAvailable(Context context){
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if(activeNetworkInfo !=null && activeNetworkInfo.isConnected()){
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
