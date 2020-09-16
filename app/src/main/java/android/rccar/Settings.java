package android.rccar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
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
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RadioButton;

public class Settings extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
        if (!GlobalSettings.getInstance().status) {
            TextView tvStatus = (TextView) findViewById(R.id.tvStatus);
            tvStatus.setText("Status: Nie połączono");
        } else {
            TextView tvStatus = (TextView) findViewById(R.id.tvStatus);
            tvStatus.setText("Wybrano urządzenie: " + GlobalSettings.getInstance().name);
        }
        load();
    }
    public void load(){
        CheckBox checkbox1 =findViewById(R.id.checkBox1);
        CheckBox checkbox2 =findViewById(R.id.checkBox2);
        CheckBox checkbox3 =findViewById(R.id.checkBox3);
        CheckBox checkbox4 =findViewById(R.id.checkBox4);
        CheckBox checkbox5 =findViewById(R.id.checkBox5);
        CheckBox checkbox6 =findViewById(R.id.checkBox6);

        RadioButton radioButton1 = findViewById(R.id.radioButton1);
        RadioButton radioButton2 = findViewById(R.id.radioButton2);
        RadioButton radioButton3 = findViewById(R.id.radioButton3);
        RadioButton radioButton4 = findViewById(R.id.radioButton4);
        RadioButton radioButton5 = findViewById(R.id.radioButton5);
        RadioButton radioButton6 = findViewById(R.id.radioButton6);



        if(GlobalSettings.driveButtons){
            checkbox1.setChecked(true);
        }else{
            checkbox1.setChecked(false);
        }
        if(GlobalSettings.spinButtons){
            checkbox2.setChecked(true);
        }else{
            checkbox2.setChecked(false);
        }
        if(GlobalSettings.turnButtons){
            checkbox3.setChecked(true);
        }else{
            checkbox3.setChecked(false);
        }
        if(GlobalSettings.velocityButton){
            checkbox4.setChecked(true);
        }else{
            checkbox4.setChecked(false);
        }
        if(GlobalSettings.accTurnButton){
            checkbox5.setChecked(true);
        }else{
            checkbox5.setChecked(false);
        }
        if(GlobalSettings.accVelocityButton){
            checkbox6.setChecked(true);
        }else{
            checkbox6.setChecked(false);
        }

        if(GlobalSettings.buttonsSensitivity==255){
            radioButton1.setChecked(true);
        }
        else if(GlobalSettings.buttonsSensitivity==220){
            radioButton2.setChecked(true);

        }
        else if(GlobalSettings.buttonsSensitivity==200){
            radioButton3.setChecked(true);
        }

        if (GlobalSettings.accSensitivity==255){
            radioButton4.setChecked(true);
        }
        else if (GlobalSettings.accSensitivity==220){
            radioButton5.setChecked(true);
        }
        else if (GlobalSettings.accSensitivity==200){
            radioButton6.setChecked(true);
        }



    }


    public void save(){
        CheckBox checkbox1 =findViewById(R.id.checkBox1);
        CheckBox checkbox2 =findViewById(R.id.checkBox2);
        CheckBox checkbox3 =findViewById(R.id.checkBox3);
        CheckBox checkbox4 =findViewById(R.id.checkBox4);
        CheckBox checkbox5 =findViewById(R.id.checkBox5);
        CheckBox checkbox6 =findViewById(R.id.checkBox6);

        RadioButton radioButton1 = findViewById(R.id.radioButton1);
        RadioButton radioButton2 = findViewById(R.id.radioButton2);
        RadioButton radioButton3 = findViewById(R.id.radioButton3);
        RadioButton radioButton4 = findViewById(R.id.radioButton4);
        RadioButton radioButton5 = findViewById(R.id.radioButton5);
        RadioButton radioButton6 = findViewById(R.id.radioButton6);


        if(checkbox1.isChecked()) {
            GlobalSettings.setdriveButtons(true);
        }else{
            GlobalSettings.setdriveButtons(false);
        }

        if(checkbox2.isChecked()){
            GlobalSettings.setSpinButtons(true);
        }else{
            GlobalSettings.setSpinButtons(false);
        }
        if(checkbox3.isChecked()){
            GlobalSettings.setturnButtons(true);
        }else{
            GlobalSettings.setturnButtons(false);
        }

        if(checkbox4.isChecked()){
            GlobalSettings.setvelocityButton(true);
        }else{
            GlobalSettings.setvelocityButton(false);
        }
        if(checkbox5.isChecked()){
            GlobalSettings.setaccTurnButton(true);
        }else{
            GlobalSettings.setaccTurnButton(false);
        }
        if(checkbox6.isChecked()){
            GlobalSettings.setaccVelocityButton(true);
        }else{
            GlobalSettings.setaccVelocityButton(false);
        }


        if(radioButton1.isChecked()){
            GlobalSettings.setbuttonsSensitivity(255);
        }
        if(radioButton2.isChecked()){
            GlobalSettings.setbuttonsSensitivity(220);
        }
        if(radioButton3.isChecked()){
            GlobalSettings.setbuttonsSensitivity(200);
        }
        if(radioButton4.isChecked()){
            GlobalSettings.setaccSensitivity(255);
        }
        if(radioButton5.isChecked()){
            GlobalSettings.setaccSensitivity(220);
        }
        if(radioButton6.isChecked()){
            GlobalSettings.setaccSensitivity(200);
        }





    }



    public void OnClickSave(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Zapisać zmiany?");
        builder.setCancelable(false);
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
                Toast.makeText(getApplicationContext(), "Zapisano zmiany", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Nie zapisano zmian", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        builder.show();

    }

    public void OnClickQuit(View v) {
        finish();
    }


}
