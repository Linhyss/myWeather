package android.linhyss.myweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewActivity extends AppCompatActivity {
    List<Fruit> fruitArrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        for(int i=0;i<1000;i++) {
            Fruit fruit=new Fruit();
            fruit.setImgid(R.drawable.setting);
            fruit.setName("水果:"+i);
            fruitArrayList.add(fruit);
        }
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecycleAdapter recycleAdapter=new RecycleAdapter(fruitArrayList);
        recyclerView.setAdapter(recycleAdapter);

    }
}
