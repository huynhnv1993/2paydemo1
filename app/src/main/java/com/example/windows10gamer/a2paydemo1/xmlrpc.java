package com.example.windows10gamer.a2paydemo1;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Windows 10 Gamer on 30/06/2017.
 */

public class xmlrpc extends AsyncTask<Void,Void,Void>{

        static String url = "http://micropay.vn";
        static String db = "2pay";
        static String username ="admin";
        static String password = "!qwertY777";


    @Override
    protected Void doInBackground(Void... params) {


        final XmlRpcClient authClient = new XmlRpcClient();
        final XmlRpcClientConfigImpl authStartConfig = new XmlRpcClientConfigImpl();
        try {
            authStartConfig.setServerURL(
                    new URL(String.format("%s/xmlrpc/2/common", url)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List configList = new ArrayList();
        Map paramMap = new HashMap();

        configList.add(db);
        configList.add(username);
        configList.add(password);
        configList.add(paramMap);

        int uid = 0;
        try {
            uid = (int)authClient.execute(
                    authStartConfig, "authenticate", configList);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }

        final XmlRpcClient objClient = new XmlRpcClient();
        final XmlRpcClientConfigImpl objStartConfig = new XmlRpcClientConfigImpl();
        try {
            objStartConfig.setServerURL(
                    new URL(String.format("%s/xmlrpc/2/object", url)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        objClient.setConfig(objStartConfig);


        List paramList = new ArrayList();

        configList.clear();
        paramMap.clear();
        paramList.clear();

        configList.add(db);
        configList.add(uid);
        configList.add(password);
        configList.add("sale.order");
        configList.add("create");

        paramMap.put("partner_id", 1);

        paramList.add(paramMap);
        configList.add(paramList);

        int cid = 0;
        try {
            cid = (int)objClient.execute("execute_kw", configList);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }

        System.out.println("Your Sales Order is : " + cid);
        Log.d("cid", String.valueOf(cid));
        return null;
    }
}
