package android.rccar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.Toast;
import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

import static android.rccar.GlobalSettings.accSensitivity;
import static android.rccar.GlobalSettings.address;
import static android.rccar.GlobalSettings.buttonsSensitivity;

public class Drive extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor gyro;
    private SensorEventListener gyroListener;
    private boolean turning = false;
    private boolean speeding = false;

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private ConnectedThread mConnectedThread;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (gyro == null) {
            Toast.makeText(this, "Nie obsługuje acc", Toast.LENGTH_LONG).show();
        }
        setContentView(R.layout.activity_drive);
        if (!GlobalSettings.getInstance().status) {
            TextView tvStatus = (TextView) findViewById(R.id.tvStatus);
            tvStatus.setText("Status: Nie połączono");
        } else {
            TextView tvStatus = (TextView) findViewById(R.id.tvStatus);
            tvStatus.setText("Połączono z urządzeniem: " + GlobalSettings.getInstance().name);
        }

        final TextView tvForward = (TextView) findViewById(R.id.tvForward);
        final TextView tvTurn = (TextView) findViewById(R.id.tvTurn);


        // listeners
        ImageButton btLeft = findViewById(R.id.btLeft);
        ImageButton btRight = findViewById(R.id.btRight);
        ImageButton btUp = findViewById(R.id.btUp);
        ImageButton btDown = findViewById(R.id.btDown);
        if(!GlobalSettings.driveButtons) {
            btLeft.setVisibility(View.INVISIBLE);
            btRight.setVisibility(View.INVISIBLE);
            btUp.setVisibility(View.INVISIBLE);
            btDown.setVisibility(View.INVISIBLE);
        }else{
        if (!GlobalSettings.turnButtons) {
            btLeft.setVisibility(View.INVISIBLE);
            btRight.setVisibility(View.INVISIBLE);

        } else {
            btLeft.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        tvTurn.setText("W LEWO");
                        turning = true;

                        mConnectedThread.write("<T-" + Integer.toString(buttonsSensitivity) + ">");
                        return true;
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        tvTurn.setText(" ");
                        turning = false;

                        mConnectedThread.write("<T0>");
                    }
                    return false;
                }
            });
            btRight.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        tvTurn.setText("W PRAWO");
                        turning = true;

                        mConnectedThread.write("<T" + Integer.toString(buttonsSensitivity) + ">");

                        return true;
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        tvTurn.setText(" ");
                        turning = false;
                        mConnectedThread.write("<T0>");
                    }
                    return false;
                }

            });
        }

            if (!GlobalSettings.velocityButton) {
                btUp.setVisibility(View.INVISIBLE);
                btDown.setVisibility(View.INVISIBLE);
            } else {
                btUp.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            tvForward.setText("DO PRZODU");
                            speeding = true;

                            mConnectedThread.write("<F" + Integer.toString(buttonsSensitivity) + ">");
                            return true;
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            speeding = false;
                            tvForward.setText(" ");

                            mConnectedThread.write("<F0>");
                        }
                        return false;
                    }
                });
                btDown.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            tvForward.setText("DO TYŁU");
                            speeding = true;
                            mConnectedThread.write("<F-" + Integer.toString(buttonsSensitivity) + ">");
                            return true;
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            tvForward.setText(" ");
                            speeding = false;
                            mConnectedThread.write("<F0>");
                        }
                        return false;
                    }
                });
            }
        }
            if (GlobalSettings.spinButtons) {
                gyroListener = new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        if (GlobalSettings.accTurnButton) {
                            if (event.values[1] > 2.0) {
                                if(!turning) {
                                    tvTurn.setText("W PRAWO");

                                    mConnectedThread.write("<T"+ Integer.toString(accSensitivity)+">");
                                }
                            }
                            else if (event.values[1] < -2.0) {
                                if(!turning) {
                                    tvTurn.setText("W LEWO");
                                    mConnectedThread.write("<T-"+ Integer.toString(accSensitivity)+">");

                                }
                            }
                            else
                            {
                                if(!turning) {
                                    mConnectedThread.write("<T0>");
                                    tvTurn.setText(" ");
                                }
                            }
                            }
                        if (GlobalSettings.accVelocityButton) {
                            if (event.values[2] > 3.0) {
                                if(!speeding) {
                                    tvForward.setText("W PRZOD");
                                    mConnectedThread.write("<F"+ Integer.toString(accSensitivity)+">");

                                }

                            }
                            if (event.values[2] < -1.0) {
                                if(!speeding) {
                                    tvForward.setText("W TYŁ");
                                    mConnectedThread.write("<F-"+ Integer.toString(accSensitivity)+">");

                                }

                            }
                            if(event.values[2]>-1 && event.values[2]<3){

                                if(!speeding) {
                                    tvForward.setText(" ");
                                    mConnectedThread.write("<F0>");

                                }

                            }
                        }




                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {

                    }
                };

            }
        }



    public void OnClickQuit(View v) throws IOException {
        mConnectedThread.write("<F0>");
        mConnectedThread.write("<T0>");
        Intent i = new Intent(Drive.this, ConnectedMenu.class);
        startActivity(i);
        finish();

    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
       return device.createRfcommSocketToServiceRecord(MY_UUID);
    }
    private void checkBTstate(){
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter==null){
            Toast.makeText(getBaseContext(),"Nie obsługuje bt",Toast.LENGTH_SHORT);
        } else {
            if(!btAdapter.isEnabled()){
                Intent enableBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBt,1);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
            try{
            btSocket = createBluetoothSocket(device);
            }catch (IOException e){
                Toast.makeText(getBaseContext(),"Socket nie zostal stworzony",Toast.LENGTH_LONG).show();
            }

            try{
                TextView tvStatus = (TextView) findViewById(R.id.tvStatus);
                tvStatus.setText("Status: Łączenie");
                btSocket.connect();
                tvStatus.setText("Połączono z " + GlobalSettings.name);
            }catch(IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();




        sensorManager.registerListener(gyroListener, gyro, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConnectedThread.write("<F0>");
        mConnectedThread.write("<T0>");
        try {
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sensorManager.unregisterListener(gyroListener);

    }

private class ConnectedThread extends Thread {

        boolean running = true;
        private final OutputStream mmOutputStream;
        public ConnectedThread(BluetoothSocket socket){

            OutputStream tmpOut = null;
            try {
                           tmpOut=socket.getOutputStream();
            }catch(IOException e){
            }
            mmOutputStream =tmpOut;
        }

        public void run(){
            while(running){

            }
        }

        public void write (String input){
            byte[] msg = input.getBytes();
            try {
                mmOutputStream.write(msg);
            }catch(IOException e){
               running = false;
            }
        }
}
}
