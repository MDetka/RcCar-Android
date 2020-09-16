package android.rccar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import static android.rccar.GlobalSettings.setAddress;

public class DevicesList extends AppCompatActivity {

    ListView lvList;
    TextView tvStatus;

    private BluetoothAdapter adapterBT;
    private ArrayAdapter<String> devicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (!GlobalSettings.getInstance().status) {
            tvStatus = (TextView) findViewById(R.id.tvStatus);
            tvStatus.setText("Status: Nie połączono");
        } else {
            tvStatus = (TextView) findViewById(R.id.tvStatus);
            tvStatus.setText("Połączono z urządzeniem: " + GlobalSettings.getInstance().name);
        }
    }

    public void OnClickQuit(View v) {
        Intent i = new Intent(this, MainMenu.class);
        setAddress("null");
        startActivity(i);
        finish();
    }

    private AdapterView.OnItemClickListener mClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            tvStatus.setText("Łączenie...");
            String info = ((TextView)v).getText().toString();
            setAddress(info.substring(info.length()-17));

            GlobalSettings.setStatus(true);
            GlobalSettings.setName(info.substring(0,info.length()-18));
            Intent i = new Intent(DevicesList.this, ConnectedMenu.class);
            startActivity(i);
            finish();
        }
    };

    private void checkBTstate(){
        adapterBT = BluetoothAdapter.getDefaultAdapter();
        if(adapterBT ==null){
            Toast.makeText(getBaseContext(),"Nie obsługuje bt",Toast.LENGTH_SHORT);
        } else {
            if(!adapterBT.isEnabled()){
                Intent enableBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBt,1);
            }
        }
    }
    @Override
    public void onResume(){
        super.onResume();

        checkBTstate();

        devicesArrayAdapter = new ArrayAdapter<String>(this,R.layout.paired_devices);
        lvList = findViewById(R.id.btList);
        lvList.setAdapter(devicesArrayAdapter);
        lvList.setOnItemClickListener(mClickListener);
        devicesArrayAdapter.clear();
        adapterBT = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = adapterBT.getBondedDevices();

        if(pairedDevices.size()>0){
            for (BluetoothDevice device : pairedDevices){
                devicesArrayAdapter.add(device.getName()+"" +"\n" + device.getAddress());
            }
        }else
        {
            devicesArrayAdapter.add("Brak sparwoanych urządzeń");
        }


    }


}
