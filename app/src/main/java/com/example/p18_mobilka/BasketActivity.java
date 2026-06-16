package com.example.p18_mobilka;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import datas.baskets.BasketDelete;
import datas.baskets.BasketGet;
import datas.baskets.BasketUpdate;
import datas.orders.OrderCreate;
import domains.PermissionManager;
import domains.callbacks.MyResponseCallback;
import domains.models.BasketParams;
import domains.models.Order;
import domains.models.ProductBasket;
import infrastructure.OrderService;

public class BasketActivity extends AppCompatActivity {

    public static String TOKEN = "d3de116e-4ac1-4d5f-ad51-8c00aa682ee";

    ArrayList<ProductBasket> ProductsBasket = new ArrayList<>();

    LinearLayout AllItems;
    TextView tvAllSum;
    View btnBasketDelete;
    View btnOrderCreate;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_activity);

        AllItems = findViewById(R.id.AllItems);
        tvAllSum = findViewById(R.id.tvAllSum);
        btnBasketDelete = findViewById(R.id.btn_basket_delete);
        btnOrderCreate = findViewById(R.id.btn_Order_Create);

        context = this;

        PermissionManager.PermissionNotification(context, this);

        btnBasketDelete.setOnClickListener(v ->{
            BasketDelete RequestBasketDelete = new BasketDelete(
                    TOKEN,
                    new MyResponseCallback() {
                        @Override
                        public void onCompile(String result) {
                            Log.d("BASKET DELETE", result);
                            onBasketGet();
                        }

                        @Override
                        public void onError(String error) {
                            Log.d("BASKET DELETE", error);
                        }
                    });
            RequestBasketDelete.execute();
        });
        btnOrderCreate.setOnClickListener(v->{
            OrderCreate RequestOrderCreate = new OrderCreate(
                    TOKEN,
                    new MyResponseCallback() {
                        @Override
                        public void onCompile(String result) {
                            Log.d("BASKET DELETE", result);
                            Order order = new GsonBuilder().create().fromJson(result, Order.class);
                            onBasketGet();

                            Toast.makeText(context,"заказ успешно оформлен. при изменении статуса вам придёт уведомление", Toast.LENGTH_SHORT).show();

                            Intent OrderService = new Intent(context, OrderService.class);
                            OrderService.putExtra("id", order.id);
                            startService(OrderService);
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("BASKET DELETE", error);
                        }
                    }
            );
            RequestOrderCreate.execute();
        });
        onBasketGet();

    }

    public void onBasketGet(){
        BasketGet RequestBasketGet = new BasketGet(
                TOKEN,
                new MyResponseCallback() {
                    @Override
                    public void onCompile(String result) {
                        Log.d("BASKET GET", result);
                        ProductsBasket = new GsonBuilder().create().fromJson(result, new TypeToken<ArrayList<ProductBasket>>(){}.getType());

                        CreateItemBasket();
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("BASKET GET", error);
                    }
                }
        );
        RequestBasketGet.execute();
    }

    public void onBasketUpdate(ProductBasket productBasket){
        BasketParams Data = new BasketParams(productBasket.count, productBasket.product.id);
        BasketUpdate RequestBasketUpdate = new BasketUpdate(
                Data,
                TOKEN,
                new MyResponseCallback() {
                    @Override
                    public void onCompile(String result) {
                        Log.d("BASKET UPDATE", result);
                        onBasketGet();
                    }

                    @Override
                    public void onError(String error) {
                        Log.d("BASKET UPDATE", error);
                    }
                }
        );
        RequestBasketUpdate.execute();
    }
    public void CreateItemBasket(){
        AllItems.removeAllViews();
        Integer AllSum = 0;
        for (int i = 0; i < ProductsBasket.size(); i++){
            ProductBasket ProductBasket = ProductsBasket.get(i);

            View itemOrder = LayoutInflater.from(this).inflate(R.layout.item_basket, AllItems,false);

            TextView tvName = itemOrder.findViewById(R.id.tvName);
            TextView tvPrice = itemOrder.findViewById(R.id.tvPrice);
            TextView tvCount = itemOrder.findViewById(R.id.tvCount);
            View btnMinus = itemOrder.findViewById(R.id.btnMinus);
            View btnPlus = itemOrder.findViewById(R.id.btnPlus);
            View btnItemClear = itemOrder.findViewById(R.id.btn_Item_Clear);

            tvName.setText(ProductBasket.product.name);
            tvPrice.setText(ProductBasket.product.price + "р");
            tvCount.setText(ProductBasket.count + "штук");

            btnMinus.setOnClickListener(v ->{
                ProductBasket.count--;
                onBasketUpdate(ProductBasket);
            });

            btnPlus.setOnClickListener(v ->{
                ProductBasket.count++;
                onBasketUpdate(ProductBasket);
            });

            btnItemClear.setOnClickListener(v ->{
                ProductBasket.count=0;
                onBasketUpdate(ProductBasket);
            });
            AllSum += ProductBasket.product.price+ProductBasket.count;
            AllItems.addView(itemOrder);
        }
        tvAllSum.setText(AllSum + "р");

    }

}
