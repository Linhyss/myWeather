package android.linhyss.myweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ListviewActivity extends AppCompatActivity {
    ListView listView;
    String[] data={"1","2","3"};
    List<Fruit> fruitArrayList=new ArrayList<>();
    private static final String TAG = "ListviewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        listView=(ListView)findViewById(R.id.list_view);

        for(int i=0;i<1000;i++) {
            Fruit fruit=new Fruit();
            fruit.setImgid(R.drawable.setting);
            fruit.setName("水果:"+i);
            fruitArrayList.add(fruit);
        }
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,data);
        FruiteAdapter adapter2=new FruiteAdapter(this,R.layout.listview_item,fruitArrayList);
        listView.setAdapter(adapter2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.d(TAG, "onItemClick: "+fruitArrayList.get(i).getName());
                Toast.makeText(getApplicationContext(),"选择"+fruitArrayList.get(i).getName(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
