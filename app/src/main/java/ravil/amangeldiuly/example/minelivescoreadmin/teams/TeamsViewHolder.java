package ravil.amangeldiuly.example.minelivescoreadmin.teams;

import static ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants.BACKEND_URL;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.TeamDTO;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TeamsViewHolder extends RecyclerView.ViewHolder {

    ImageView teamLogo;
    TextView teamName;
    TeamsAdapter.OnItemListener onItemListener;
    Context context;
    TeamDTO teamDTO;
    Retrofit retrofit;

    public TeamsViewHolder(@NonNull View itemView, TeamsAdapter.OnItemListener onItemListener, Context context) {
        super(itemView);
        this.teamLogo = itemView.findViewById(R.id.card_team_logo);
        this.teamName = itemView.findViewById(R.id.card_team_name);
        this.onItemListener = onItemListener;
        this.context = context;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        itemView.setOnClickListener(v -> onItemListener.onItemClick(teamDTO));
    }

}
