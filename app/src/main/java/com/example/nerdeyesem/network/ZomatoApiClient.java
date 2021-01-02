package com.example.nerdeyesem.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZomatoApiClient {

    //For now, I am sharing the API key here, for you to test the application.
    // And that's why I had to hide my repo.
    // Then I will keep this api key as environment variable and make the repo public.
    public static final String API_KEY = "17361d6eadaae515bd66272977b5e85f";

    private static Retrofit retrofit;

    //Singleton pattern to access Api service
    public static Retrofit getRetrofit() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://developers.zomato.com/api/v2.1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
