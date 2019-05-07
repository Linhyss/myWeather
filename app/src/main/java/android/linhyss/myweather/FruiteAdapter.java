package android.linhyss.myweather;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2019/5/7.
 */

public class FruiteAdapter extends ArrayAdapter<Fruit> {
     private int resourceid;

    public FruiteAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Fruit> objects) {
        super(context, resource, objects);
        resourceid=resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Fruit fruit=getItem(position);
        View view;
        ViewHolder viewholder;
        if(convertView!=null){
            view=convertView;
            viewholder=(ViewHolder)view.getTag();
        }else {
            view = LayoutInflater.from(getContext()).inflate(resourceid, parent, false);
            viewholder=new ViewHolder();
            viewholder.imageview=view.findViewById(R.id.listview_img);
            viewholder.textview=view.findViewById(R.id.listview_text);
            view.setTag(viewholder);
        }

        viewholder.textview.setText(fruit.getName());
        viewholder.imageview.setImageResource(fruit.getImgid());
        return view;
    }
    class ViewHolder{
        ImageView imageview;
        TextView textview;

    }
}
