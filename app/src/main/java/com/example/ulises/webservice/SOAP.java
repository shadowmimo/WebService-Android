package com.example.ulises.webservice;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by Ulises on 15/04/2017.
 */

public class SOAP extends AsyncTask<String,Void,String> {

    private Map<String,String> parametros;
    private TextView contenedor;

    public SOAP(TextView contenedor, Map<String,String> parametros){
        this.parametros = parametros;
        this.contenedor = contenedor;
    }


    @Override
    protected String doInBackground(String... params) {
        SoapObject soapobject = new SoapObject(params[0],params[1]);
        for(String key:parametros.keySet()){
            PropertyInfo pi = new PropertyInfo();
            pi.setName(key);
            pi.setValue(parametros.get(key));
            pi.setType(String.class);
            soapobject.addProperty(pi);
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(soapobject);

        HttpTransportSE ht = new HttpTransportSE(params[2],600000);
        ht.debug = true;
        String respuesta;
        try{
            ht.call(params[3],envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            respuesta = result.getProperty(0).toString();
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            respuesta = "ERROR\n" +  sw.toString();
        }
        return respuesta;
    }

    @Override
    public void onPostExecute(String resultado){
        contenedor.setText(resultado);
    }
}
