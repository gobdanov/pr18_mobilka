package datas.baskets;

import com.google.gson.GsonBuilder;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

import domains.apis.MyAsyncTask;
import domains.callbacks.MyResponseCallback;
import domains.common.Settings;
import domains.models.BasketParams;


public class BasketUpdate extends MyAsyncTask {
    String token;
    BasketParams basketRequest;
    public BasketUpdate(BasketParams basketRequest, String token, MyResponseCallback callback){
        super(callback);
        this.token = token;
        this.basketRequest = basketRequest;
    }

    @Override
    protected String doInBackground(Void... voids){
        String rawData = new GsonBuilder().create().toJson(basketRequest);
        try{
            Connection.Response  response = Jsoup.connect(Settings.Url + "/api/basket/update")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.GET)
                    .header("Content-type", "application/json")
                    .header("token", token)
                    .requestBody(rawData)
                    .execute();
            return response.statusCode() == 200 ? response.body() : "Error: " + response.body();
        } catch (IOException e){
            return "Error " + e.getMessage();
        }
    }

}
