package android.linhyss.myweather;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DownloadActivity extends AppCompatActivity {
    private static final String TAG = "DownloadActivity";
    DownloadManager.Request request;
    Button downbtn;
    Button downcencel;
    DownloadManager downManager=null;
    long id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        downbtn =(Button)findViewById(R.id.down_btn);
        downcencel =(Button)findViewById(R.id.down_cancel);
        downbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download("http://gdown.baidu.com/data/wisegame/55dc62995fe9ba82/jinritoutiao_448.apk", Environment.DIRECTORY_DOWNLOADS);
            }
        });
        downcencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(downManager!=null&&id!=0) {
                    downManager.remove(id);
                }else{
                    Toast.makeText(getApplicationContext(),"未知错误",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void download(String url,String path){

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //设置在什么网络情况下进行下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|(DownloadManager.Request.NETWORK_WIFI));
        //设置通知栏标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("下载");
        request.setDescription("今日头条正在下载");
        request.setAllowedOverRoaming(false);
        //设置文件存放目录
        request.setDestinationInExternalFilesDir(this, path, "mydown");

        downManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        id= downManager.enqueue(request);
        Log.d(TAG, "download: 下载id="+id);

    }
}
