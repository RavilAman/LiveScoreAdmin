package ravil.amangeldiuly.example.minelivescoreadmin.activities;

import static ravil.amangeldiuly.example.minelivescoreadmin.SharedPreferencesConstants.AUTH_TOKEN;
import static ravil.amangeldiuly.example.minelivescoreadmin.SharedPreferencesConstants.BEARER_PREFIX;
import static ravil.amangeldiuly.example.minelivescoreadmin.SharedPreferencesConstants.LOGGED_IN;
import static ravil.amangeldiuly.example.minelivescoreadmin.SharedPreferencesConstants.TRUE;
import static ravil.amangeldiuly.example.minelivescoreadmin.utils.SharedPreferencesUtil.getValue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;

import ravil.amangeldiuly.example.minelivescoreadmin.MainActivity;
import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreadmin.utils.SharedPreferencesUtil;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.AuthApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.AuthRequest;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.AuthResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthActivity extends AppCompatActivity {

    private Context context;

    private EditText loginField;
    private EditText passwordField;
    private Button loginButton;
    private ImageButton passwordToggle;
    private TextView invalidCredentials;

    private Retrofit retrofit;
    private AuthApi authApi;

    private AuthRequest authRequest;
    private boolean viewPasswordClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initializeViews();
        initializeRetrofit();
        initializeObjects();
        setOnClickListeners();

        checkIfLoggedIn();
    }

    private void initializeViews() {
        context = getApplicationContext();
        loginField = findViewById(R.id.activity_auth_login_field);
        passwordField = findViewById(R.id.activity_auth_password_field);
        loginButton = findViewById(R.id.activity_auth_login_button);
        passwordToggle = findViewById(R.id.activity_auth_password_toggle);
        invalidCredentials = findViewById(R.id.activity_auth_wrong_credentials_label);
    }

    private void initializeRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(UrlConstants.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        authApi = retrofit.create(AuthApi.class);
    }

    private void initializeObjects() {
        authRequest = new AuthRequest();
    }

    private void setOnClickListeners() {
        passwordToggle.setOnClickListener(passwordToggleListener());
        loginButton.setOnClickListener(loginButtonListener());
    }

    private View.OnClickListener passwordToggleListener() {
        return view -> {
            viewPasswordClicked = !viewPasswordClicked;
            int passwordToggleDrawable = viewPasswordClicked
                    ? R.drawable.ic_baseline_visibility_off_24
                    : R.drawable.ic_baseline_remove_red_eye_24;
            if (viewPasswordClicked) {
                passwordField.setTransformationMethod(null);
            } else {
                passwordField.setTransformationMethod(new PasswordTransformationMethod());
            }
            passwordToggle.setImageResource(passwordToggleDrawable);
            passwordField.setSelection(passwordField.getText().length());
        };
    }

    private View.OnClickListener loginButtonListener() {
        return view -> login();
    }

    private void login() {
        invalidCredentials.setVisibility(View.GONE);
        authRequest.setUsername(loginField.getText().toString());
        authRequest.setPassword(passwordField.getText().toString());
        authApi.login(authRequest).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SharedPreferencesUtil.putValue(context,
                            AUTH_TOKEN,
                            BEARER_PREFIX + response.body().getToken());
                    SharedPreferencesUtil.putValue(context,
                            LOGGED_IN,
                            TRUE);
                    goToMainEvent();
                } else if (response.code() == 403) {
                    invalidCredentials.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void checkIfLoggedIn() {
        if (getValue(context, LOGGED_IN).equals(TRUE)) {
            goToMainEvent();
        }
    }

    private void goToMainEvent() {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }
}