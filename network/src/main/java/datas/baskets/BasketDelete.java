package datas.baskets;


import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

import domains.apis.MyAsyncTask;
import domains.callbacks.MyResponseCallback;
import domains.common.Settings;



public class BasketDelete  extends MyAsyncTask {

    String token;
    public BasketDelete(String token, MyResponseCallback callback){
        super(callback);
        this.token = token;
    }
    @Override
    protected String doInBackground(Void... voids){
        try{
            Connection.Response  response = Jsoup.connect(Settings.Url + "/api/basket/delete")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.DELETE)
                    .header("Content-type", "application/json")
                    .header("token", token)
                    .execute();
            return response.statusCode() == 200 ? response.body() : "Error: " + response.body();
        } catch (IOException e){
            return "Error " + e.getMessage();
        }
    }
}
