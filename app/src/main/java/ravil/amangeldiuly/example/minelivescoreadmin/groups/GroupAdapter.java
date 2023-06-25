package ravil.amangeldiuly.example.minelivescoreadmin.groups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.games.GameAdapter;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.NewGameDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TournamentDto;

public class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder> {

    private Context context;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private List<NewGameDTO> gamesInGroups;
    private FragmentManager fragmentManager;

    public GroupAdapter(Context context, List<NewGameDTO> gamesInGroups, FragmentManager fragmentManager) {
        recycledViewPool = new RecyclerView.RecycledViewPool();
        this.gamesInGroups = gamesInGroups;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.group_item, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.context = context;
        NewGameDTO newGameDTO = gamesInGroups.get(position);
        holder.tournament = new TournamentDto(newGameDTO.getTournamentId(),
                newGameDTO.getTournamentName(), newGameDTO.getTournamentLogo(),
                newGameDTO.getTournamentType(), newGameDTO.getTournamentLocation(),
                newGameDTO.getTournamentStatus());
        Glide.with(context)
                .load(newGameDTO.getTournamentLogo())
                .into(holder.tournamentLogo);
        holder.tournamentName.setText(newGameDTO.getTournamentName());
        holder.groupName.setText(newGameDTO.getGroupName());
        holder.fragmentManager = fragmentManager;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.gamesRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        linearLayoutManager.setInitialPrefetchItemCount(newGameDTO.getGames().size());

        GameAdapter gameAdapter = new GameAdapter(context, newGameDTO.getGames());
        holder.gamesRecyclerView.setLayoutManager(linearLayoutManager);
        holder.gamesRecyclerView.setAdapter(gameAdapter);
        holder.gamesRecyclerView.setRecycledViewPool(recycledViewPool);
        holder.tournamentId = newGameDTO.getTournamentId();
        holder.groupId = newGameDTO.getGroupId();
    }

    @Override
    public int getItemCount() {
        return gamesInGroups.size();
    }
}
