package com.Johanna.test;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{
    private final String TAG = "Rate";
    String data[] ={"one","two","three","four"};
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Intent list = getIntent();

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);
        final List<String> list1 = new ArrayList<String>();
        for(int i=1;i<100;i++){
            list1.add("item"+i);
        }
        ListView listView = (ListView)findViewById(R.id.list);


       ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list1);
       setListAdapter(adapter);

       Thread t = new Thread(this);
       t.start();


       handler = new Handler(){
           @Override
           public void handleMessage(@NonNull Message msg) {
               if(msg.what==5){
                   List<String> list2 = (List<String>) msg.obj;
                   ListAdapter adapter = new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list2);
                   setListAdapter(adapter);
               }
               super.handleMessage(msg);
           }
       };
//         listView.setAdapter(adapter);

       // finish();
    }

    @Override
    public void run() {
        List<String> retList = new ArrayList<String>();


        Document doc = null;
        try {
            Thread.sleep(3000);
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
                Log.i(TAG,str1+"=>"+val);
                retList.add(str1+"=>"+val);


            }



        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(5);
//        msg.what = 5;
//        msg.obj = "Hello from run()";
        msg.obj = retList;
        handler.sendMessage(msg);



    }
}
