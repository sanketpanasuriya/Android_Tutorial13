package com.example.tutorial13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText edtPhone,edtMSG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtPhone=findViewById(R.id.editPhone);
        edtMSG=findViewById(R.id.editMSG);
    }

    public void callToNumber(View view) {
        if(isCallPermissionAllowed()) {
            call();
        }
    }

    private boolean isCallPermissionAllowed() {
        if(Build.VERSION.SDK_INT>=23){
            if(checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                Log.v("TAG","Permission is Granted");
                return true;
            }else{
                Log.v("TAG","Permission is Revoked");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
                return false;
            }
        }else
            return true;
    }

    public void sendTextMessage(View view) {
        if(isMSGPermissionAllowed()) {
            sms();
        }
    }

    private boolean isMSGPermissionAllowed() {
        if(Build.VERSION.SDK_INT>=23){
            if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                Log.v("TAG","Permission is Granted");
                return true;
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},2);
                Log.v("TAG","Permission is Revoked");
                return false;
            }
        }else
            Log.v("TAG","Permission is Granted");
            return true;
    }

    private void sms() {
        String phoneNumber = edtPhone.getText().toString();
        String smsText = edtMSG.getText().toString();
        if(phoneNumber.length()!=10){
            Toast.makeText(this,"Number must be 10 digits",Toast.LENGTH_SHORT).show();
        }else if(smsText.isEmpty()) {
            Toast.makeText(this, "Please enter same message", Toast.LENGTH_SHORT).show();
        }else {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, smsText, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
        }
    }

    private void call() {
        String phoneNumber = edtPhone.getText().toString();
        if(phoneNumber.length()==10){
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }else{
            Toast.makeText(this,"Number must be 10 digits",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    call();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    sms();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}