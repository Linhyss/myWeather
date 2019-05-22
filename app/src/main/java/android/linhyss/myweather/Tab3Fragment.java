package android.linhyss.myweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Tab3Fragment extends Fragment {

    private static final String TAG = "ChooseAreaFragment";

    private TextView titleText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_layout, container, false);
        titleText = (TextView) view.findViewById(R.id.tab1_text);
        titleText.setTextSize(30);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleText.setText("碎片3");
    }

}
