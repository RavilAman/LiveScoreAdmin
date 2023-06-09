package ravil.amangeldiuly.example.minelivescoreuser.tournaments;

import static ravil.amangeldiuly.example.minelivescoreuser.UrlConstants.BACKEND_URL;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TournamentDto;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TournamentListViewHolder extends RecyclerView.ViewHolder {

    ShapeableImageView tournamentLogo;
    TextView tournamentName;
    TextView tournamentLocation;
    TournamentListAdapter.OnItemListener onItemListener;
    ImageButton imageButton;
    Context context;
    TournamentDto tournamentDto;
    Retrofit retrofit;

    public TournamentListViewHolder(@NonNull View itemView,TournamentListAdapter.OnItemListener onItemListener, Context context) {
        super(itemView);
        this.tournamentLogo = itemView.findViewById(R.id.card_tournament_logo);
        this.tournamentName = itemView.findViewById(R.id.card_tournament_name);
        this.tournamentLocation = itemView.findViewById(R.id.card_tournament_location);
        this.onItemListener = onItemListener;
        this.context = context;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
