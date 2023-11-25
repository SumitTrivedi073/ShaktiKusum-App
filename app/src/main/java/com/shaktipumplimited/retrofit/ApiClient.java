package com.shaktipumplimited.retrofit;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import debugapp.GlobalValue.Constant;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utility.CustomUtility;
import webservice.WebURL;


public class ApiClient {
    private static Retrofit retrofit = null;
    private static Retrofit retrofit2 = null;
    public static Retrofit getClient(Context context) {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(CustomUtility.getSharedPreferences(context, Constant.RmsBaseUrl))// close by vikas
                    .client(getRequestHeader())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientDebugApp() {

        if (retrofit2 == null) {

            retrofit2 = new Retrofit.Builder()
                    .baseUrl(WebURL.BASE_URL_VK)// close by vikas
                     .client(getRequestHeader())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }


    public static OkHttpClient okHttpClient = null;

    public static OkHttpClient getRequestHeader() {
        if (null == okHttpClient) {
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @NonNull
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                           /* Request.Builder ongoing = chain.request().newBuilder();
                            //ongoing.addHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE);
                          //  ongoing.addHeader("Authorization", NewSolarVFD.COMPELETE_ACCESS_TOKEN_NAME);
                            return chain.proceed(ongoing.build());*/

                            Request request = chain.request();

                            // try the request
                            Response response = chain.proceed(request);

                            int tryCount = 0;
                            while (!response.isSuccessful() && tryCount < 3) {

                                Log.d("intercept", "Request is not successful - " + tryCount);

                                tryCount++;

                                // retry the request
                                response.close();
                                response = chain.proceed(request);
                            }

                            // otherwise just pass the original response on
                            return response;
                        }
                    })
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }


}
