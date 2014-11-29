package com.example.carlton.ca;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ListActivity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;


/**
 * Created by Carlton on 29/11/2014.
 */
public class PopularPageActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HttpGetTask().execute();
    }
    private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {
        private static final String CLIENT_ID = "c8fa3ba0c07b41ad989cc027abf7c804";
        private static final String ACCESS_TOKEN = "39479938.c8fa3ba.819ecb525cfc4486804e781902a500b2";
        private static final String URL = "https://api.instagram.com/v1/media/popular?access_token=" + ACCESS_TOKEN;

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected List<String> doInBackground(Void... params){
            HttpGet request = new HttpGet(URL);
            JSONResponseHandler responseHandler = new JSONResponseHandler();
            try {
                return mClient.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (null != mClient)
                mClient.close();
            setListAdapter(new ArrayAdapter<String>(
                    PopularPageActivity.this,
                    R.layout.popular_page_item, result));
        }

    }
    private class JSONResponseHandler implements ResponseHandler<List<String>> {
        private static final String DATA_TAG = "data";
        private static final String TYPE_TAG = "type";
        private static final String CAPTION_TAG = "caption";
        private static final String TEXT_TAG = "text";
        private static final String LINK_TAG = "link";
        private static final String USER_TAG = "user";
        private static final String USERNAME_TAG = "username";
        private static final String PROFILE_PICTURE_TAG = "profile_picture";
        private static final String ID_TAG = "id";
        private static final String IMAGES_TAG = "images";
        private static final String THUMBNAIL_TAG = "thumbnail";
        private static final String URL_TAG = "url";
        private static final String HEIGHT_TAG  ="height";
        private static final String WIDTH_TAG = "width";
        private static final String STANDARD_RESOLUTION_TAG = "standard_resolution";

        public List<String> handleResponse(HttpResponse response) throws ClientProtocolException, IOException{
            List<String> result = new ArrayList<String>();
            String JSONResponse = new BasicResponseHandler().handleResponse(response);

            try{
                JSONObject responseObject = (JSONObject) new JSONTokener(JSONResponse).nextValue();
                JSONArray data = responseObject.getJSONArray(DATA_TAG);
                for (int idx = 0; idx < data.length(); idx++){
                    JSONObject imgObj = (JSONObject) data.get(idx);
                    /**
                    *Might have to run a for loop on "caption", "user", "images"
                    *to get the data out of then because of the structure
                    *http://instagram.com/developer/endpoints/media/
                    *
                    */
                    result.add(TYPE_TAG +":"+ imgObj.get(TYPE_TAG) + ","+CAPTION_TAG +":"+ imgObj.get(CAPTION_TAG) + ","+TEXT_TAG +":"+ imgObj.get(TEXT_TAG) + ","
                            +LINK_TAG +":"+ imgObj.get(LINK_TAG) + ","+USER_TAG +":"+ imgObj.get(USER_TAG) + ","+USERNAME_TAG +":"+ imgObj.get(USERNAME_TAG) + ","
                            +PROFILE_PICTURE_TAG +":"+ imgObj.get(PROFILE_PICTURE_TAG) + ","+ID_TAG +":"+ imgObj.get(ID_TAG) + ","+IMAGES_TAG +":"+ imgObj.get(IMAGES_TAG) + ","
                            +THUMBNAIL_TAG +":"+ imgObj.get(THUMBNAIL_TAG) + ","+URL_TAG +":"+ imgObj.get(URL_TAG) + ","+HEIGHT_TAG +":"+ imgObj.get(HEIGHT_TAG) + ","
                            +WIDTH_TAG +":"+ imgObj.get(WIDTH_TAG) + ","+STANDARD_RESOLUTION_TAG +":"+ imgObj.get(STANDARD_RESOLUTION_TAG));
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            return result;
        }
    }
}
