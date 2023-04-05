package com.shaktipumplimited.shaktikusum.webservice.retrofit;




import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClientIMEI {

    public static String HEADER_CONTENT_TYPE = "Content-Type";
    public static String HEADER_KRY = "SHAKTI";

    /////////////////value of header
    public static String HEADER_CONTENT_TYPE_VALUE = "application/json";
    public static String HEADER_KRY_VALUE = "123456";
   // public static final String BASE_URL = "http://a313955b.ngrok.io/fitnessApp/";
  //  public static final String GET_URL = "http://192.168.4.1/";
  // public static final String BASE_URL = "http://192.168.2.23:3011/api/";

    private static Retrofit retrofit = null;


    public static Retrofit getClientIMEI() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://pmkapi.hareda.gov.in/api/")
                    .client(getRequestHeader())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static OkHttpClient okHttpClient = null;

    public static OkHttpClient getRequestHeader() {
        if (null == okHttpClient) {
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request.Builder ongoing = chain.request().newBuilder();
                            //ongoing.addHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE);
                       //     ongoing.addHeader("Authorization", NewSolarVFD.COMPELETE_ACCESS_TOKEN_NAME);
                            return chain.proceed(ongoing.build());
                        }
                    })
                    .readTimeout(45, TimeUnit.SECONDS)
                    .connectTimeout(45, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }


   /* public static OkHttpClient getRequestHeader() {
        if (null == okHttpClient) {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(45, TimeUnit.SECONDS)
                    .connectTimeout(45, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }*/


}
