package android.rccar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class ConnectedMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_connected_menu);
        TextView tvStatus= (TextView)findViewById(R.id.tvStatus);
        tvStatus.setText("Wybrano urządzenie: "+ GlobalSettings.getInstance().name);
    }


    public void OnClickDisconnect(View v) {
        GlobalSettings.setStatus(false);
        GlobalSettings.setName("unnamed");
        GlobalSettings.setAddress("null");
        Intent i = new Intent(this, MainMenu.class);
        startActivity(i);
        finish();
    }
    public void OnClickDrive(View v){
        TextView tvStatus= (TextView)findViewById(R.id.tvStatus);
        tvStatus.setText("Łączenie..");
        Intent i = new Intent(this, Drive.class);
        startActivity(i);
        finish();
    }
    public void OnClickSettings(View v) {
        Intent i = new Intent(this, Settings.class);
        startActivity(i);
    }
    public void OnClickQuit(View v) {
        GlobalSettings.setStatus(false);
        finish();
    }

}
