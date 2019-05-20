package android.linhyss.myweather;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class FragmentTabActivity extends AppCompatActivity {
    private FragmentTabHost mFragmentTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_tab);
        mFragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mFragmentTabHost.setup(getApplicationContext(), getSupportFragmentManager(), R.id.realtabcontent);
        Indicator[] indicators = Indicator.values();
        //初始化Tab
        for (int i = 0; i < indicators.length; i++) {
            TabHost.TabSpec tabSpec = mFragmentTabHost.newTabSpec(getString(indicators[i].getResName()));
            tabSpec.setIndicator(getIndicatorView(indicators[i]));
            mFragmentTabHost.addTab(tabSpec, indicators[i].getClz(), null);
        }

        //去除底部按钮之间的分割线
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mFragmentTabHost.getTabWidget().setShowDividers(0);

            mFragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {
//                    if(tabId.equals(getString(Indicator.TEST.getResName()))){
//                        mCount.setVisibility(View.VISIBLE);
//                    }else{
//                        mCount.setVisibility(View.GONE);
//                    }
                }
            });


        }
    }

    /** 返回设置好的底部按钮
     * @param indicator1
     * @return
     */
    private View getIndicatorView(Indicator indicator1) {

        View view = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
        TextView indicator = (TextView) view.findViewById(R.id.tab_title);

        indicator.setText(getString(indicator1.getResName()));

        indicator.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        Drawable icon = this.getResources().getDrawable(indicator1.getResIcon());
//        自定义ICON大小
//        icon.setBounds(0, 0, 75, 75);
//        indicator.setCompoundDrawables(null,icon,null,null);
        indicator.setCompoundDrawablePadding(3);
        indicator.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        indicator.setPadding(0,8,0,5);


        return view;
    }




















}
