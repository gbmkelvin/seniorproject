package cn.project.camt.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class UpdateImageViewAsync{
	 ImageView imageview;
     Bitmap imgBitmap = null;

   
	public UpdateImageViewAsync(String url, ImageView imageview) {
                     // TODO Auto-generated constructor stub
                     this.imageview = imageview;

                     new LoadImageAsync().execute(url);

     }

     class LoadImageAsync extends AsyncTask<Object, Object, Object> {

                     protected Long doInBackground(String... params) {
                                     // TODO Auto-generated method stub
                                     final String imageUrl = params[0];
                                     Bitmap bitmap = null;

                                     try {
                                                     bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
                                     } catch (MalformedURLException e) {
                                                     // TODO Auto-generated catch block
                                                     e.printStackTrace();
                                     } catch (IOException e) {
                                                     // TODO Auto-generated catch block
                                                     e.printStackTrace();
                                     }

                                     imgBitmap = bitmap;
                                     return null;
                     }
                     protected void onPostExecute(Long result) {
                                     // TODO Auto-generated method stub

                                     // set bitmap to imageView
                                     imageview.setImageBitmap(imgBitmap);
                                     super.onPostExecute(result);
                     }
					@Override
					protected Object doInBackground(Object... params) {
						// TODO Auto-generated method stub
						return null;
					}
     }
}
