package com.abusement.park.acneed.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLException;

public class FacePPClient {

    private static final String TAG = "FacePPClient";

    private static final String API_KEY = "xYs-B3gemuwpCEef0CLf6zcRzeqCYHjB";
    private static final String API_SECRET = "jDAbS0omtK112QQ9fcp6JUrGcoV2GbIN";
    private static final String endpoint = "https://api-us.faceplusplus.com/facepp/v3/detect";
    private final static int CONNECT_TIME_OUT = 30000;
    private final static int READ_OUT_TIME = 50000;
    private static String boundaryString = getBoundary();

    public static String detectFaces(File imageFile) {
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", API_KEY);
        map.put("api_secret", API_SECRET);
        byteMap.put("image_file", getBytesFromFile(imageFile));
        try{
            byte[] bacd = post(endpoint, map, byteMap);
            String result = new String(bacd);
            Log.d(TAG, "Face result: " + result);
            return result;
        }catch (Exception e) {
            Log.d(TAG, "Exception while submitting request: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static byte[] post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {
        HttpURLConnection connection;
        URL url1 = new URL(url);
        connection = (HttpURLConnection) url1.openConnection();
        connection.setDoOutput(true);
        connection.setUseCaches(true);
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(CONNECT_TIME_OUT);
        connection.setReadTimeout(READ_OUT_TIME);
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            dataOutputStream.writeBytes("--" + boundaryString + "\r\n");
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"\r\n");
            dataOutputStream.writeBytes("\r\n");
            dataOutputStream.writeBytes(value + "\r\n");
        }
        if(fileMap != null && fileMap.size() > 0){
            for (Map.Entry<String, byte[]> fileEntry : fileMap.entrySet()) {
                dataOutputStream.writeBytes("--" + boundaryString + "\r\n");
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                        + "\"; filename=\"" + encode(" ") + "\"\r\n");
                dataOutputStream.writeBytes("\r\n");
                dataOutputStream.write(fileEntry.getValue());
                dataOutputStream.writeBytes("\r\n");
            }
        }
        dataOutputStream.writeBytes("--" + boundaryString + "--" + "\r\n");
        dataOutputStream.writeBytes("\r\n");
        dataOutputStream.flush();
        dataOutputStream.close();
        InputStream ins = null;
        int code = connection.getResponseCode();
        try{
            if(code == 200){
                ins = connection.getInputStream();
            }else{
                ins = connection.getErrorStream();
            }
        }catch (SSLException e){
            e.printStackTrace();
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len;
        while((len = ins.read(buff)) != -1){
            baos.write(buff, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        ins.close();
        return bytes;
    }
    private static String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 32; i++) {
            char ascii = (char) (rand.nextInt(26) + 97); // a - z
            sb.append(ascii);
        }
        return sb.toString();
    }
    private static String encode(String value) throws Exception{
        return URLEncoder.encode(value, "UTF-8");
    }

    private static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            Log.d(TAG, "I/O EXCEPTION WHILE READING FILE BYTES", e);
        }
        return null;
    }
}
