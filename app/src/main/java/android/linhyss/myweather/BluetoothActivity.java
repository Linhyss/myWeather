package android.linhyss.myweather;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

public class BluetoothActivity extends AppCompatActivity   {
    private BluetoothAdapter  mBluetoothAdapter;
    final int REQUEST_CODE_PERMISSION_LOCATION=1;
    final int REQUEST_CODE_OPEN_GPS=2;
    final int REQUEST_CODE_OPEN_BLUETOOTH=3;
    private static boolean isScanning=false;//是否正在搜索
    ListView listView;
    private Handler mHandler;
    private BluetoothDevice chooseDevice;
    ArrayAdapter<String> arrayAdapter;
    //15秒搜索时间
    private static final long SCAN_PERIOD = 15000;
    private  List<BluetoothDevice> mBluetoothGattList=new ArrayList<>();
    Button openbtn;
    Button searchbtn;
    Button connect_btn;
    Button disconnect_btn;
    EditText editText;
    Button send_btn;
    BluetoothGattCharacteristic mCharacteristic;
    BluetoothGatt mBluetoothGatt;
    private List<String> device=new ArrayList<String>();
    private static final String TAG = "BluetoothActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        checkPermissions();
        mHandler=new Handler();
        openbtn=(Button)findViewById(R.id.open_btn) ;
        searchbtn=(Button)findViewById(R.id.search_btn) ;
        listView=(ListView)findViewById(R.id.bluetoothlist);
        onPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        connect_btn=(Button)findViewById(R.id.connect_btn) ;
        disconnect_btn=(Button)findViewById(R.id.disconnect_btn) ;
        editText= (EditText) findViewById(R.id.edt_sendbuf);
        send_btn= (Button) findViewById(R.id.send_btn);
        device.add("蓝牙列表:");
        arrayAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,device);
        listView.setAdapter(arrayAdapter);
        openbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                    //有蓝牙功能模块
                    Log.d(TAG, "onCreate: 有蓝牙功能模块");

                    if(isBlueEnable()){
                        openBlueSync(BluetoothActivity.this,REQUEST_CODE_OPEN_BLUETOOTH);
                    }else{
                        scanLeDevice(true);
                    }

                }else{
                    Toast.makeText(BluetoothActivity.this,"不支持蓝牙",Toast.LENGTH_SHORT).show();
                }
            }
        });
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBluetoothGattList.size()!=0){
                    for(int i=0;i<mBluetoothGattList.size();i++){
                        device.add(mBluetoothGattList.get(i).getName());

                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chooseDevice=mBluetoothGattList.get(i);
            }
        });

        connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chooseDevice!=null) {
                    mBluetoothGatt = chooseDevice.connectGatt(BluetoothActivity.this, true, mBluetoothGattCallback);
                }
            }
        });

        disconnect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBluetoothGatt!=null){
                    mBluetoothGatt.disconnect();
                }
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBluetoothGatt != null) {
                    BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb"));
                    mCharacteristic = service.getCharacteristic(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
                    byte[] send;
                    send = editText.getText().toString().getBytes();
                    byte[] sendData = new byte[send.length + 2];
                    sendData[0] = (byte) 0xaa;
                    sendData[sendData.length - 1] = (byte) 0xff;
                    for (int i = 1; i < sendData.length - 1; i++) {
                        sendData[i] = send[i - 1];
                    }
                    Log.e("dataSend", sendData.toString());
                    mCharacteristic.setValue(sendData);
                    if (mBluetoothGatt != null) {
                        boolean status = mBluetoothGatt.writeCharacteristic(mCharacteristic);
                        Log.e("dataSend", status + "");
                    } 


                }
            }
        });




    }


    /**
     * 蓝牙是否打开   true为打开
     * @return
     */
    public boolean isBlueEnable(){
        return isSupportBlue() && mBluetoothAdapter.isEnabled();
    }

    /**
     * 设备是否支持蓝牙  true为支持
     * @return
     */
    public boolean isSupportBlue(){
        return mBluetoothAdapter != null;
    }
    /**
     * 自动打开蓝牙（异步：蓝牙不会立刻就处于开启状态）
     * 这个方法打开蓝牙不会弹出提示
     */
    public void openBlueAsyn() {
        if (isSupportBlue()) {
            mBluetoothAdapter.enable();
        }
    }


    /**
     * 自动打开蓝牙（同步）
     * 这个方法打开蓝牙会弹出提示
     * 需要在onActivityResult 方法中判断resultCode == RESULT_OK  true为成功
     */
    public void openBlueSync(Activity activity, int requestCode){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_OPEN_BLUETOOTH&&resultCode==RESULT_OK){
            Log.d(TAG, "onActivityResult: 蓝牙打开");
            scanLeDevice(true);

        }
    }

    /**
     * 检查权限
     */
    private void checkPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    /**
     * 权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public final void onRequestPermissionsResult(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i]);
                        }
                    }
                }
                break;
        }
    }

    /**
     * 开启GPS
     * @param permission
     */
    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("当前手机扫描蓝牙需要打开定位功能。")
                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                            .setPositiveButton("前往设置",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                        }
                                    })

                            .setCancelable(false)
                            .show();
                } else {
                    //GPS已经开启了
                }
                break;
        }
    }
    /**
     * 检查GPS是否打开
     * @return
     */
    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }




    private void scanLeDevice(final boolean enable) {
        if (enable) {//true
            //15秒后停止搜索
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            isScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback); //开始搜索
        } else {//false
            isScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);//停止搜索
        }
    }
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            //这里是个子线程，下面把它转换成主线程处理
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBluetoothGattList.add(device);
                    //在这里可以把搜索到的设备保存起来
                    //device.getName();获取蓝牙设备名字
                    //device.getAddress();获取蓝牙设备mac地址
                    //这里的rssi即信号强度，即手机与设备之间的信号强度。
                }
            });
        }

    };
    BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyRead(gatt, txPhy, rxPhy, status);
        }

        //当连接状态发生改变
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
        }

        //发现新服务，即调用了mBluetoothGatt.discoverServices()后，返回的数据
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
        }

        //调用mBluetoothGatt.readCharacteristic(characteristic)读取数据回调，在这里面接收数据
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.d(TAG, "onCharacteristicRead: "+ characteristic.getValue().toString());

        }

        //发送数据后的回调
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {//descriptor读
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {//descriptor写
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        //调用mBluetoothGatt.readRemoteRssi()时的回调，rssi即信号强度
        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {//读Rssi
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
        }
    };

}
