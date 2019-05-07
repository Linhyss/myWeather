package android.linhyss.myweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

@SuppressWarnings("deprecation")
public class SettingActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener {
    private Preference pref_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);
        //通过key得到界面上的Preference

        pref_share=(Preference) findPreference("share");
        pref_share.setOnPreferenceClickListener(this);        // 输入选择的值显示出来


        SharedPreferences sharedPreferences =PreferenceManager.getDefaultSharedPreferences(this);
        String refleshtime =sharedPreferences.getString("refleshtime","8");
        // 把输入的值显示出来
        Preference pref = findPreference("refleshtime");
        pref.setSummary(refleshtime+"小时");
        pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            @Override
            public boolean onPreferenceChange(Preference pref, Object arg1) {
                pref.setSummary(arg1.toString()+"小时");
                return true;
            }
        });

}

    private static final String TAG = "SettingActivity";
/**
 * 点击后触发分享
 */
@Override
public boolean onPreferenceClick(Preference preference) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "分享app，链接还没有..");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getTitle()));

    return true;
}

}
