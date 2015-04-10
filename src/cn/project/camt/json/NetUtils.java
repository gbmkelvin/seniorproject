package cn.project.camt.json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

public class NetUtils {
	public static final String HTTP_REQUEST_METHOD_GET="GET";
	
	public static final String HTTP_REQUEST_METHOD_POST="POST";
	
	public static String getCharacterEncode(HttpURLConnection conn)
	{
		if(conn==null)
			return null;
		
		String ct=conn.getHeaderField("Content-Type");
		
		if(ct==null||ct.length()<=0)
			return null;
		
		return ct.substring(ct.indexOf("=")+1,ct.length());
	}
	
	public static String request(String request)
	{
		try {
			URL url=new URL(request);
			
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			if(conn.getResponseCode()==HttpURLConnection.HTTP_OK)
			{
				InputStream in = conn.getInputStream();
				String charset=NetUtils.getCharacterEncode(conn);
				BufferedReader reader=new BufferedReader(new InputStreamReader(in, charset));
				StringBuffer buf=new StringBuffer();
				String line=null;
				while((line=reader.readLine())!=null)
				{
					buf.append(line);
				}
				reader.close();
				in.close();
				conn.disconnect();
				
				return buf.toString();
			}
			else
			{
				Log.e("NetUtils", "Rquesting the URL["+request+"] is failure, Please check the network setting!");
				conn.disconnect();
				return null;
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("NetUtils", e.getMessage());
			return null;
		}
	}
	
	public static String request(String path,Map<String,Object> params,String method)
	{
		if(path==null||path.length()<=0)
			return null;
		try{
			URL url=null;
			HttpURLConnection conn=null;
			if(method!=null&&method.equalsIgnoreCase("post"))
			{
				url=new URL(path);
				
				conn=(HttpURLConnection) url.openConnection();
				conn.setRequestMethod(HTTP_REQUEST_METHOD_POST);
				conn.setDoInput(true);
				if(params!=null&&!params.isEmpty())
				{
					String query="";
					int c=0;
					for(Entry<String, Object> param:params.entrySet())
					{
						String key=param.getKey();
						String value=param.getValue().toString();
						if(c+1==params.size())
						{
							query+=key+"="+value;
						}
						else
						{
							query+=key+"="+value+"&";
						}
						c++;
					}
					conn.setDoOutput(true);
					OutputStream out = conn.getOutputStream();
					out.write(query.getBytes());
					out.close();
				}
				
			}
			else
			{
				
				if(params!=null&&!params.isEmpty())
				{
					String query="?";
					int c=0;
					for(Entry<String, Object> param:params.entrySet())
					{
						String key=param.getKey();
						String value=param.getValue().toString();
						if(c+1==params.size())
						{
							query+=key+"="+value;
						}
						else
						{
							query+=key+"="+value+"&";
						}
						c++;
					}
					path+=query;
				}
				
				url=new URL(path);
				conn=(HttpURLConnection) url.openConnection();
				conn.setRequestMethod(HTTP_REQUEST_METHOD_GET);
				conn.setDoInput(true);
			}
			
			conn.connect();
			if(conn.getResponseCode()==HttpURLConnection.HTTP_OK)
			{
				InputStream in = conn.getInputStream();
				String charset=NetUtils.getCharacterEncode(conn);
				BufferedReader reader=new BufferedReader(new InputStreamReader(in, charset));
				StringBuffer buf=new StringBuffer();
				String line=null;
				while((line=reader.readLine())!=null)
				{
					buf.append(line);
				}
				reader.close();
				in.close();
				conn.disconnect();
				
				return buf.toString();
			}
			else
			{
				Log.e("NetUtils", "Rquesting the URL["+path+"] is failure, Please check the network setting!");
				conn.disconnect();
				return null;
			}
			
			
		}catch(Exception e){
			//Log.e("NetUtils", e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
