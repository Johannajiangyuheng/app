package com.Johanna.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

public class dollars extends AppCompatActivity implements Runnable {

    private final String TAG = "Rate";
    private float dollarRate;
    private float euroRate;
    private float wonRate;

    EditText rmb;
    TextView show;

    Handler handler;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dollars);

        rmb = (EditText) findViewById(R.id.ipt1);
        show = (TextView) findViewById(R.id.view1);

        SharedPreferences sharedPreferences = getSharedPreferences("rate", Activity.MODE_PRIVATE);
//把数据拿出来
        dollarRate = sharedPreferences.getFloat("dollar_rate", 0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate", 0.0f);
        wonRate = sharedPreferences.getFloat("won_rate", 0.0f);

        Log.i(TAG, "on create sp dollarRate = " + dollarRate);
        Log.i(TAG, "on create sp euroRate = " + euroRate);
        Log.i(TAG, "on create sp wonRate = " + wonRate);

        //开启子线程
        Thread t = new Thread(this);
        t.start();


//匿名类定义对象
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 5) {

                    Bundle bdl = (Bundle) msg.obj;
                    dollarRate = bdl.getFloat("dollar-rate");
                    euroRate = bdl.getFloat("euro-rate");
                    wonRate = bdl.getFloat("won-rate");

                    Log.i(TAG, "handlemessage:dollarRate " + dollarRate);
                    Log.i(TAG, "handlemessage:euroRate " + euroRate);
                    Log.i(TAG, "handlemessage:wonRate " + wonRate);

                    Toast.makeText(dollars.this, "汇率已更新", Toast.LENGTH_SHORT).show();

                }
                super.handleMessage(msg);

            }
        };

    }


    public void onClick(View btn) {
        String str = rmb.getText().toString();
        float r = 0;
        if (str.length() > 0) {
            r = Float.parseFloat(str);
        }

        if (btn.getId() == R.id.DOLLAR) {
            float val = r * dollarRate;
            show.setText(String.valueOf(val));
        } else if (btn.getId() == R.id.EURO) {
            float val = r * euroRate;
            show.setText(String.valueOf(val));
        } else {
            float val = r * wonRate;
            show.setText(String.valueOf(val));
        }


    }

    public void openOne(View btn) {
        //Log.i("open","openOne:");
        openConfig();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_set) {
            openConfig();
        }else if (item.getItemId()==R.id.open_list){
            Intent list = new Intent(this, RateListActivity.class);

            startActivity(list);

        }

        return super.onOptionsItemSelected(item);
    }

    private void openConfig() {
        Intent config = new Intent(this, openNew.class);
        config.putExtra("dollar_rate_key", dollarRate);
        config.putExtra("euro_rate_key", euroRate);
        config.putExtra("won_rate_key", wonRate);

        Log.i(TAG, "openOne:dollarRate=" + dollarRate);
        Log.i(TAG, "openOne:euroRate=" + euroRate);
        Log.i(TAG, "openOne:wonRate=" + wonRate);
        //startActivity(config);
        startActivityForResult(config, 1);


    }


//    public void openList(View btn) {
//            Intent list = new Intent(this,RateListActivity.class);
//            startActivity(list);
//
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == 2) {
//            bdl.putFloat("key_dollar",newDollar);
//            bdl.putFloat("key_euro",newEuro);
//            bdl.putFloat("key_won",newWon);
            Bundle bundle = data.getExtras();//返回的数据是一个Bundle对象
            dollarRate = bundle.getFloat("key_dollar", 0.1f);
            euroRate = bundle.getFloat("key_euro", 0.1f);
            wonRate = bundle.getFloat("key_won", 0.1f);
            Log.i(TAG, "onActivityResult:dollarRate=" + dollarRate);
            Log.i(TAG, "onActivityResult:euroRate=" + euroRate);
            Log.i(TAG, "onActivityResult:wonRate=" + wonRate);


            //将获取的汇率写入sp
            SharedPreferences sharedPreferences = getSharedPreferences("rate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar_rate", dollarRate);
            editor.putFloat("euro_rate", euroRate);
            editor.putFloat("won_rate", wonRate);
            editor.commit();

            Log.i(TAG, "数据已保存到sharedPreferences中");
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void run() {


            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        Bundle bundle = new Bundle();
//
//
//
//        //获取网络数据
        URL url = null;
//
        Document doc = null;
        Message msg = handler.obtainMessage(5);
//        msg.what = 5;
//        msg.obj = "Hello from run()";
        msg.obj = bundle;
        handler.sendMessage(msg);
//
        try {
            doc = Jsoup.connect("https://www.usd-cny.com/bankofchina.htm").get();
            Elements tables = doc.getElementsByTag("table");
            Element table6 = tables.get(0);
            Elements tds = table6.getElementsByTag("td");
            for (int i = 0; i < tds.size(); i += 6) {
                Element td1 = tds.get(i);
                Element td2 = tds.get(i + 5);
                String str1 = td1.text();
                String val = td2.text();
                float v = 100f / Float.parseFloat(val);
//                HashMap<String,String> map= new HashMap<String,String>();
//                map.put("ItemTitle",str1);   //标题文字
//                map.put("ItemDetail",String.valueOf(v));  //详情描述
//                listItems.add(map);
                Log.i(TAG,"run:text="+td1.text()+"=>val="+td2.text());

                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",v);
                }else if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",v);
                }else if("韩元".equals(str1)){
                    bundle.putFloat("won-rate",v);
                }
            }

           // Elements elements = doc.select("span.short");
//
//            for (Element element : elements) {
//                Log.i(TAG, "run:span = " + element);
//            }

//            Element table6 = tables.get(5);
//            Elements tds = table6.getElementsByTag("td");
//            for(int i=0;i<tds.size();i++){
//                Element td1 = tds.get(i);
//                Element td2 = tds.get(i+5);


        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

        private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(inputStream, "GB2312");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }


}
