package datas.orders;


import com.google.gson.GsonBuilder;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

import domains.apis.MyAsyncTask;
import domains.callbacks.MyResponseCallback;
import domains.common.Settings;

public class OrderCreate  extends MyAsyncTask{
    String token;
    public OrderCreate(String token, MyResponseCallback callback){
        super(callback);
        this.token = token;
    }
    @Override
    protected String doInBackground(Void... voids){
        try{
            Connection.Response  response = Jsoup.connect(Settings.Url + "/api/order/create")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.POST)
                    .header("Content-type", "application/json")
                    .header("token", token)
                    .execute();
            return response.statusCode() == 200 ? response.body() : "Error: " + response.body();
        } catch (IOException e){
            return "Error " + e.getMessage();
        }
    }
}
