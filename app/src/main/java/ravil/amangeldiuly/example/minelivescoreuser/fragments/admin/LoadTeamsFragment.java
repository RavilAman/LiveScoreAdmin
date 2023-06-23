package ravil.amangeldiuly.example.minelivescoreuser.fragments.admin;

import static ravil.amangeldiuly.example.minelivescoreuser.UrlConstants.BACKEND_URL;
import static ravil.amangeldiuly.example.minelivescoreuser.UrlConstants.DELETE_SUBSCRIPTION;
import static ravil.amangeldiuly.example.minelivescoreuser.UrlConstants.STATISTICS_FOR_TOURNAMENT;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.web.RequestHandler;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.UploadTeamsApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TournamentDto;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadTeamsFragment extends Fragment {

    private View currentView;
    private TournamentDto tournamentDto;
    private ShapeableImageView tournamentLogo;
    private TextView tournamentName;
    private TextView tournamentGroup;
    private ImageButton imageButton;
    private FragmentManager fragmentManager;
    private Button uploadTeamsButton;
    private EditText linkEditText;
    private ProgressDialog progressDialog;
    private UploadTeamsApi tournamentApi;
    private Retrofit retrofit;

    public static LoadTeamsFragment newInstance(TournamentDto tournamentDto, FragmentManager fragmentManager) {
        LoadTeamsFragment fragment = new LoadTeamsFragment(fragmentManager, tournamentDto);
        Bundle args = new Bundle();
        args.putString("tournament_logo", tournamentDto.getTournamentLogo());
        args.putString("tournament_name", tournamentDto.getTournamentName());
        args.putString("tournament_group", tournamentDto.getTournamentLocation());
        fragment.setArguments(args);
        return fragment;
    }

    public LoadTeamsFragment(FragmentManager fragmentManager, TournamentDto tournamentDto) {
        this.fragmentManager = fragmentManager;
        this.tournamentDto = tournamentDto;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_load_teams, container, false);
        tournamentLogo = currentView.findViewById(R.id.fragment_tournament_logo);
        tournamentName = currentView.findViewById(R.id.fragment_tournament_name);
        tournamentGroup = currentView.findViewById(R.id.fragment_tournament_group);
        imageButton = currentView.findViewById(R.id.fragment_back_button);
        uploadTeamsButton = currentView.findViewById(R.id.upload_button);
        linkEditText = currentView.findViewById(R.id.link_edittext);


        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // Set the connection timeout to 30 seconds
                .readTimeout(60, TimeUnit.SECONDS) // Set the read timeout to 30 seconds
                .writeTimeout(60, TimeUnit.SECONDS); // Set the write timeout to 30 seconds

        RequestHandler requestHandler = new RequestHandler(getContext());
        retrofit = requestHandler.getRetrofit();
        tournamentApi = retrofit.create(UploadTeamsApi.class);

        Bundle args = getArguments();

        if (args != null) {
            tournamentName.setText(args.getString("tournament_name"));
            tournamentGroup.setText(args.getString("tournament_group"));
            Glide.with(this)
                    .load(args.getString("tournament_logo"))
                    .into(tournamentLogo);
        }
        imageButton.setOnClickListener(backButtonListener());

        linkEditText.addTextChangedListener(getWatcher());
        uploadTeamsButton.setOnClickListener(uploadTeamsButtonListener());

        return currentView;
    }

    private View.OnClickListener uploadTeamsButtonListener() {
        return view -> {
            String link = linkEditText.getText().toString().trim();
            if (!link.isEmpty()) {
                // Show progress dialog
                showProgressDialog("Uploading Teams...");
                Callback<ResponseBody> callback = new Callback<>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        hideProgressDialog();
                        assert response.body() != null;
                        ResponseBody body = response.body();
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Teams successfully added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                        }
                        linkEditText.setText("");
                        uploadTeamsButton.setEnabled(false);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideProgressDialog();
                        Toast.makeText(getContext(), "Vse Ploho", Toast.LENGTH_SHORT).show();

                    }
                };
                tournamentApi.uploadPlayerInfo(link, tournamentDto.getTournamentId()).enqueue(callback);
            }
        };
    }

    private void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @NonNull
    private TextWatcher getWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if the linkEditText is empty or contains only whitespace
                boolean isEmpty = s.toString().trim().isEmpty();
                boolean isLink = isTextValidLink(s.toString().trim());

                // Enable the button only if it's not empty and a valid link
                uploadTeamsButton.setEnabled(!isEmpty && isLink);
            }

            private boolean isTextValidLink(String text) {
                // Define a regular expression to validate the link format
                String linkPattern = "^(https?|ftp)://.*$";
                // Match the text against the regular expression
                return text.matches(linkPattern);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        };
    }

    private View.OnClickListener backButtonListener() {
        return view -> {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.popBackStack();
            fragmentTransaction.commit();
        };
    }

}
