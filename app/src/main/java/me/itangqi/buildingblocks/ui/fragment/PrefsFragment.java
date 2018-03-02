package me.itangqi.buildingblocks.ui.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import me.itangqi.buildingblocks.R;


public class PrefsFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener {

    String versionName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        try {
//            String versionName = this.getActivity().getApplicationContext().getPackageManager()
//                    .getPackageInfo(this.getActivity().getApplicationContext().getPackageName(), 0).versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
        addPreferencesFromResource(R.xml.prefs);
    }

    //选中的设置
    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

}
