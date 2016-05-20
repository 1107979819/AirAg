package cn.airag.base64.airag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.util.Log;

public class Test {



	private OkHttpClient client;

	public Test() {
//		client = new OkHttpClient();
//		client.cookieJar(new MyCookieJar());
		
		//��������cookie
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.cookieJar(new MyCookieJar());
		builder.connectTimeout(100, TimeUnit.SECONDS);
		builder.writeTimeout(100, TimeUnit.SECONDS);
		builder.readTimeout(100, TimeUnit.SECONDS);
		client = builder.build();
	}

	public void httpGet(String url) {
		try {

			
			Request request = new Request.Builder()
					.url(url)
					.get()
					.build()
					;

			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				Log.i("Test", "" + response.body().string());
			} else {
				Log.i("Test", "respone:" + response.code());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("Test", "error:"+e.getMessage());
			e.printStackTrace();
		}
	}

	public String httpGetReturn(String url) {
		try {

			
			Request request = new Request.Builder()
					.url(url)
					.get()
					.build()
					;

			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				String result = response.body().string();
				Log.i("Test", "" +result  );
				return result;
			} else {
				Log.i("Test", "respone:" + response.code());
				return null;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("Test", "error:"+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	
	public void httpGet() {
		try {

			
			Request request = new Request.Builder()
					.url("http://wenyl.top/HttpTest.php?name=111")
					.get()
					.build();

			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				Log.i("Test", "" + response.body().string());
			} else {
				Log.i("Test", "" + response.code());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("Test", "error");
			e.printStackTrace();
		}
	}

	public void httpPost() {
		try {

			FormBody body = new FormBody.Builder().add("name", "1111").build();
			Request request = new Request.Builder()
					.url("http://wenyl.top/HttpTest.php")
					.post(body)
					.build();

			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				Log.i("Test", "" + response.body().string());
			} else {
				Log.i("Test", "" + response.code());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("Test", "error");
			e.printStackTrace();
		}
	}
	
	
	public void httpPut() {
		try {

			FormBody body = new FormBody.Builder().add("name", "1111").build();
			Request request = new Request.Builder()
					.url("http://wenyl.top/HttpTest.php")
					.put(body)
					.build();

			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				Log.i("Test", "" + response.body().string());
			} else {
				Log.i("Test", "" + response.code());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("Test", "error");
			e.printStackTrace();
		}
	}
	
	public void httpDelete() {
		try {

			FormBody body = new FormBody.Builder().add("name", "1111").build();
			Request request = new Request.Builder()
					.url("http://wenyl.top/HttpTest.php")
					.delete(body)
					.build();

			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				Log.i("Test", "" + response.body().string());
			} else {
				Log.i("Test", "" + response.code());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("Test", "error");
			e.printStackTrace();
		}
	}
	
	public void httpCookie(String url) {
		try {

		
			
			
			Request request = new Request.Builder()
					.url(url)
					.get()
					.build()
					;
			Response response = client.newCall(request).execute();
			
			   
			  
			  
			    
			if (response.isSuccessful()) {
				Log.i("Test", "" + response.body().string());
				
				
			} else {
				Log.i("Test", "" + response.code());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("Test", "error");
			e.printStackTrace();
		}
	}
	public class MyCookieJar implements CookieJar {

	    private List<Cookie> cookies;

	    @Override
	    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
	        this.cookies =  cookies;
	        Log.i("Test","save cookie");
	    }

	    @Override
	    public List<Cookie> loadForRequest(HttpUrl url) {
	        if (cookies != null)
	            return cookies;
	        return new ArrayList<Cookie>();

	    } 
	}

}
