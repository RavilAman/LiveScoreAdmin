package ravil.amangeldiuly.example.minelivescoreadmin.tournaments;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.activities.StatisticsActivity;
import ravil.amangeldiuly.example.minelivescoreadmin.db.SQLiteManager;
import ravil.amangeldiuly.example.minelivescoreadmin.utils.SharedPreferencesUtil;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.NotificationApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.TournamentDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TournamentViewHolder extends RecyclerView.ViewHolder {

    LinearLayout mainLayout;
    ShapeableImageView tournamentLogo;
    TextView tournamentName;
    TextView tournamentLocation;
    ImageButton imageButton;
    boolean imageButtonPressed;
    long tournamentId;

    Context context;
    TournamentDto tournamentDto;

    private NotificationApi notificationApi;
    private SQLiteManager sqLiteManager;
    private Retrofit retrofit;

    public TournamentViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;

        mainLayout = itemView.findViewById(R.id.tournament_item_layout);
        tournamentLogo = itemView.findViewById(R.id.card_tournament_logo);
        tournamentName = itemView.findViewById(R.id.card_tournament_name);
        tournamentLocation = itemView.findViewById(R.id.card_tournament_location);
        imageButton = itemView.findViewById(R.id.toggle_favourites_button);

        sqLiteManager = SQLiteManager.getInstance(context);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(UrlConstants.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        notificationApi = retrofit.create(NotificationApi.class);

        imageButton.setOnClickListener(imageButtonListener());
        setOnClickListeners();
    }

    public void redrawImageButtons() {
        if (sqLiteManager.tournamentPresents(tournamentDto.getTournamentId().intValue())) {
            imageButtonPressed = true;
            imageButton.setImageResource(R.drawable.ic_baseline_star_24);
        }
    }

    private void setOnClickListeners() {
        mainLayout.setOnClickListener(layoutListener());
    }

    private View.OnClickListener layoutListener() {
        return view -> {
            if (imageButtonPressed) {
                Intent statisticsIntent = new Intent(context, StatisticsActivity.class);
                statisticsIntent.putExtra("tournamentId", tournamentId);
                statisticsIntent.putExtra("groupId", -1L);
                context.startActivity(statisticsIntent);
            }
        };
    }

    private View.OnClickListener imageButtonListener() {
        return view -> {
            imageButtonPressed = !imageButtonPressed;
            Executor mainExecutor = Executors.newSingleThreadExecutor();
            notificationApi.getTopicName(tournamentDto.getTournamentId()).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.body() != null) {
                        String topicName = response.body();
                        if (imageButtonPressed) {
                            imageButton.setImageResource(R.drawable.ic_baseline_star_24);
                            mainExecutor.execute(() -> {
                                try {
                                    Log.d("token from prefs", SharedPreferencesUtil.getValue(context, "fcm_token"));
                                    notificationApi.createSubscription(topicName, List.of(SharedPreferencesUtil.getValue(context, "fcm_token"))).execute();
                                } catch (IOException e) {
                                    Log.e("Error while executing notification api", Arrays.toString(e.getStackTrace()));
                                }
                            });
                            sqLiteManager.addTournamentToFavourites(tournamentDto);
                            sqLiteManager.addTopic(topicName);
                        } else {
                            imageButton.setImageResource(R.drawable.ic_baseline_star_border_24);
                            mainExecutor.execute(() -> {
                                try {
                                    Log.d("token from prefs", SharedPreferencesUtil.getValue(context, "fcm_token"));
                                    notificationApi.deleteSubscription(topicName, SharedPreferencesUtil.getValue(context, "fcm_token")).execute();
                                } catch (IOException e) {
                                    Log.e("Error while executing notification api", Arrays.toString(e.getStackTrace()));
                                }
                            });
                            sqLiteManager.deleteTournamentFromFavourites(tournamentDto.getTournamentId());
                            sqLiteManager.deleteTopic(topicName);
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        };
    }
}
