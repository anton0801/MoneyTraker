package priler.com.api;

import android.app.Application;
import android.content.SharedPreferences;
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

    public static final String BASE_URL = "https://moneytracker-server.ru/api/";
    public static final String DATE_FORMAT = "dd:HH:mm";
    public static final String PREFS_NAME = "money_tracker";
    public static final String AUTH_TOKEN_KEY = "auth_token";
    public static final String IS_AUTH_KEY = "is_auth";

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

    private static App instance;

    public Api getApi() {
        return api;
    }

    public void saveAuthToken(String token) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(AUTH_TOKEN_KEY, token);
        editor.apply();
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(key, "");
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getBoolean(key, false);
    }

}
