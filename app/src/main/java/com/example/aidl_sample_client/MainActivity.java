package com.example.aidl_sample_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aidl_sample_server.Calculator;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText op1, op2;
    TextView tv;
    Button add, sub, mul, div;
    Calculator cal;
    boolean bound = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = findViewById(R.id.add);
        sub = findViewById(R.id.sub);
        mul = findViewById(R.id.mul);
        div = findViewById(R.id.div);
        op1 = findViewById(R.id.op1);
        op2 = findViewById(R.id.op2);
        tv = findViewById(R.id.res);
        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        mul.setOnClickListener(this);
        div.setOnClickListener(this);
        Intent ob = new Intent("com.android.demo.aidlserver");
        bindService(convert(ob, getApplicationContext()), con, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            cal = Calculator.Stub.asInterface(service);
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Override
    public void onClick(View v) {
        int res = 0;
        char op = ' ';
        int a = Integer.parseInt(op1.getText().toString());
        int b = Integer.parseInt(op2.getText().toString());
        try {
            if(bound) {
                switch (v.getId()) {
                    case R.id.add:
                        res = cal.add(a, b);
                        op = '+';
                        break;
                    case R.id.sub:
                        res = cal.sub(a, b);
                        op = '-';
                        break;
                    case R.id.mul:
                        res = cal.mul(a, b);
                        op = '*';
                        break;
                    case R.id.div:
                        res = cal.div(a, b);
                        op = '/';
                        break;
                }
            }
            tv.setText(""+a+" "+op+" "+b+" = "+res);
        }catch(RemoteException re){
            re.printStackTrace();
        }
    }

    private Intent convert(Intent ob, Context applicationContext) {
        PackageManager pm = applicationContext.getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentServices(ob, 0);
        if(resolveInfoList == null || resolveInfoList.size() != 1){
            return null;
        }
        ResolveInfo serviceInfo = resolveInfoList.get(0);
        ComponentName componentName = new ComponentName(serviceInfo.serviceInfo.packageName, serviceInfo.serviceInfo.name);
        Intent explicitIntent = new Intent(ob);
        explicitIntent.setComponent(componentName);
        return explicitIntent;
    }
}
