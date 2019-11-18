package com.example.alspicks;


import android.app.Activity;
import android.widget.Switch;
import android.view.View;

public class themeChooser {

    public static void setOnCheckedChangeListener(Activity activity, Switch switch1) {
        if(switch1.isChecked())
            activity.setTheme(R.style.Theme_darkTheme);
        else
            activity.setTheme(R.style.Theme_light);
    }
}
