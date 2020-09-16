package android.rccar;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        if(!GlobalSettings.getInstance().status) {
            TextView tvStatus = (TextView) findViewById(R.id.tvStatus);
            tvStatus.setText("Status: Nie połączono");
        }
    }

    public void OnClickConnect(View v) {
        Intent i = new Intent(this, DevicesList.class);
        startActivity(i);
        finish();
    }

    public void OnClickSettings(View v) {
        Intent i = new Intent(this, Settings.class);
        startActivity(i);
    }
    public void OnClickQuit(View v) {
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
        if (!GlobalSettings.getInstance().status) {
            TextView tvStatus = (TextView) findViewById(R.id.tvStatus);
            tvStatus.setText("Status: Nie połączono");
            GlobalSettings.setAddress("null");
        }
    }
}
