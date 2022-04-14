package cn.rubintry.animatorutils;

import android.app.Application;

public class AnimateApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //注册全局的EditText点击外部收起键盘
        GlobalEdittextHooker.registerHooker(this);
    }
}
