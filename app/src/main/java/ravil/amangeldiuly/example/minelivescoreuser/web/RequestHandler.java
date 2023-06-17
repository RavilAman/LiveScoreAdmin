package ravil.amangeldiuly.example.minelivescoreuser.web;

import static ravil.amangeldiuly.example.minelivescoreuser.SharedPreferencesConstants.AUTH_TOKEN;
import static ravil.amangeldiuly.example.minelivescoreuser.SharedPreferencesConstants.FALSE;
import static ravil.amangeldiuly.example.minelivescoreuser.SharedPreferencesConstants.LOGGED_IN;
import static ravil.amangeldiuly.example.minelivescoreuser.utils.SharedPreferencesUtil.getValue;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.activities.AuthActivity;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.utils.SharedPreferencesUtil;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestHandler {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private Retrofit retrofit;
    private Context context;

    public RequestHandler(Context context) {
        this.context = context;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request originalRequest = chain.request();
            Request modifiedRequest = originalRequest.newBuilder()
                    .header(HEADER_AUTHORIZATION, getValue(context, AUTH_TOKEN))
                    .build();
            Response response = chain.proceed(modifiedRequest);
            if (response.code() == 403) {
                SharedPreferencesUtil.putValue(context, LOGGED_IN, FALSE);
                Intent authIntent = new Intent(context, AuthActivity.class);
                authIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(authIntent);
            }
            return response;
        });
        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(UrlConstants.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
