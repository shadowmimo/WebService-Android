package com.example.ulises.webservice;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button Enviar=(Button) findViewById(R.id.boton1);
        Enviar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
             case R.id.boton1:
                 EditText user= (EditText) findViewById(R.id.editText);
                 EditText contra= (EditText) findViewById(R.id.editText2);
                 enviausuario(user.getText().toString(),contra.getText().toString());
                 break;
        }
    }

    public void enviausuario(String user,String contra) {
        final String NAMESPACE = "http://WS/";
        final String METHOD_NAME = "login";
        final String SOAP_ACTION = "http://WS/SOAP/loginRequest";
        final String WSDL = "http://192.168.100.10:8080/WebService/SOAP";
        Map<String,String> parametros = new HashMap();
        parametros.put("usuario",user);
        parametros.put("contraseña",contra);
        TextView contenedor = new TextView(this);
        contenedor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().startsWith("ERROR")){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Ocurrió un error")
                            .setMessage(s.toString())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else{
                    Boolean resultado = new Boolean(s.toString());
                    if(resultado){
                        Intent intento = new Intent(MainActivity.this,MainActivity.class);
                        MainActivity.this.startActivity(intento);
                    }else{
                        Toast.makeText(MainActivity.this,"Datos incorrectos",Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SOAP soapws = new SOAP(contenedor,parametros);
        soapws.execute(NAMESPACE,METHOD_NAME,WSDL,SOAP_ACTION);
    }

}
