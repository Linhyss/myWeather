package android.linhyss.myweather.BReceiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.linhyss.myweather.MainActivity;
import android.linhyss.myweather.WeatherActivity;
import android.linhyss.myweather.gson.Weather;
import android.util.Log;
import android.widget.Toast;

public class DownLoadCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "DownLoadCompleteReceive";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.d(TAG, "onReceive:下载完成 ");
        }else if(intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)){
            Log.d(TAG, "onReceive：触碰 ");
        }
    }
}
