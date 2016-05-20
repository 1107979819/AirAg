package cn.airag.base64.airag;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {
    private OkHttpClient client;
//    String  result;
    private MyHandler myhandler;
    private ImageView iv;
    private Button btn;
    private boolean isRequseting = false;
    private Bitmap bitmap;
    private TextView tv;
    private TextView tv_mes;
    private String repMes;
    private String picMes;
    private  int count = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        iv = new ImageView(getApplicationContext());
//        setContentView(iv);
        iv = (ImageView)findViewById(R.id.imageView);
        tv = (TextView)findViewById(R.id.textView);
        tv_mes = (TextView)findViewById(R.id.tv_mes);
        btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( isRequseting ==false)
                {   repMes = "请求中";
                    myhandler.sendEmptyMessage(1);
                    count  = 60;
                    download();
                    Toast.makeText(getApplicationContext(),"请求数据",Toast.LENGTH_SHORT).show();

                    isRequseting =true;
                }

            }
        });
        myhandler = new MyHandler();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(100, TimeUnit.SECONDS);
        builder.writeTimeout(100, TimeUnit.SECONDS);
        builder.readTimeout(100, TimeUnit.SECONDS);
        client = builder.build();

        download();
        repMes = "请求中";
        myhandler.sendEmptyMessage(1);
        isRequseting = true;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
             if(isRequseting==false&&count == 0)
             {
                 count  = 60;
                 repMes = "请求中";
                 download();
                 myhandler.sendEmptyMessage(1);
                 isRequseting =true;
             }
                if(isRequseting==false) {
                    count--;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn.setText("刷新(" + count + ")");
                        }
                    });
                }
            }
        },1000,1000);

    }

    public void download()
    {
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               tv_mes.setText("");
               iv.setImageDrawable(getResources ().getDrawable(R.mipmap.ic_launcher));
           }
       });
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {


                    Request request = new Request.Builder()
                            .url("http://112.74.56.215/ae/ae/spec/pic/00000013?gpsX=23.25246568830887&gpsY=111.54324531555174")
                            .get()
                            .build()
                            ;

                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Headers  headers = response.headers();

                        repMes = headers.toString();
                         Log.i("Test","Head:"+ repMes);
                        String json =response.body().string();
                        Gson gson = new Gson();
                        PicJson picJson = gson.fromJson(json,PicJson.class);
                        bitmap = stringtoBitmap( picJson.getPicture() );
                        picMes = "I   D :"+picJson.getAe_id()+"\n地区："+picJson.getAe_name()+"\n农场："+picJson.getFarm_name()+" \n时间："+picJson.getCollect_time();

                        myhandler.sendEmptyMessage(0);

                    } else {
                        Log.i("Test", "respone:" + response.code());
                        repMes = "error:"+ response.code();
                        myhandler.sendEmptyMessage(1);
                        count  = 15;

                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.i("Test", "error:"+e.getMessage());
                    repMes = e.getMessage();
                    myhandler.sendEmptyMessage(1);
                    count  = 15;
                    e.printStackTrace();
                }

/*

                Log.i("Test","Test");
                t = new Test();

//				String  result= t.httpGetReturn("http://112.74.56.215/ae/ae/spec/pic/log/00000013?gpsX=23.25246568830887&gpsY=111.54324531555174&startTime=2016-05-19&endTime=2016-05-20");
                result= t.httpGetReturn("http://112.74.56.215/ae/ae/spec/pic/00000013?gpsX=23.25246568830887&gpsY=111.54324531555174");
                if(result!=null)
                {
//                    Log.i("Test",">>"+result);
                    String ar[] = result.split("\":\"");
                    bitmap = stringtoBitmap( ar[1] );
                    myhandler.sendEmptyMessage(0);


//                    for(int i = 0 ; i<ar.length;i++)
//                    {
//                        Log.i("Test",""+ar[i]);
//                    }
//                    String spic = ar[1];
//					Log.i("Test"," spic "+ spic );


//                    Message m = new Message();
//                    m.what = 0;
//
//                    Bundle  b =new Bundle();
//                    b.putString("data", spic);
//
//                    m.setData(b);
//                    myhandler.dispatchMessage(m);
                }else
                {
                    isRequseting =false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"请求异常",Toast.LENGTH_SHORT).show();
                        }
                    });

                }*/



            }
        }).start();
    }
    class MyHandler extends Handler
    {



        @Override
        public void handleMessage(final Message msg) {
            if(msg.what==0)
            {
                iv.setImageBitmap(bitmap);
                tv.setText(repMes);
                tv_mes.setText(picMes);
                isRequseting =false;

            }
            else
            {
                tv.setText(repMes);
                isRequseting=false;
            }

            super.handleMessage(msg);
        }



    }

    public Bitmap stringtoBitmap(String string){

        Bitmap bitmap=null;
        try {
            byte[]bitmapArray;
            bitmapArray= Base64.decode(string, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            repMes +=e.getMessage();
            myhandler.sendEmptyMessage(1);
            e.printStackTrace();
        }

        return bitmap;
    }


}
