package priler.com.api;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import priler.com.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static final String TAG = "App";

    public static final String BASE_URL = "https://c3f83173aa7a.ngrok.io/api/";
    public static final String DATE_FORMAT = "dd:HH:mm";

    private Api api;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(
                BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE
        ); // BODY это запрос с телом, а NONE это ничего

        OkHttpClient client = new OkHttpClient.Builder() // это отвечает за соединение сервера
                .addInterceptor(interceptor) // перехватчик он делает перед тек как мы отправим кокой-то запрос этот перехватчик возбмёт его как-то обрабодает и отправит его на сервер
                // и потом можно также получить ответ от сервера перехватить и как-то его обработать! это можно использовать к примеру при передачи чего-либо на сервер мы можем просто этому объекту присвайвать заголовок или проверить если в этом объекте то чего неля пересылать или выпускать ну и т.д
                .connectTimeout(1200000, TimeUnit.SECONDS)
                .readTimeout(120000, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat(DATE_FORMAT) // когда приходит дата и в коком формате её преобразовать
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)) // для обработки json ответов
                .client(client)
                .build();

        api = retrofit.create(Api.class);
    }

    public Api getApi() {
        return api;
    }

}
