package ravil.amangeldiuly.example.minelivescoreuser.groups;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.games.GameAdapter;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.NewGameDTO;

public class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder> {

    private Context context;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private List<NewGameDTO> gamesInGroups;
    private GroupAdapter.OnItemListener onItemListener;

    public GroupAdapter(Context context, List<NewGameDTO> gamesInGroups, GroupAdapter.OnItemListener onItemListener) {
        recycledViewPool = new RecyclerView.RecycledViewPool();
        this.gamesInGroups = gamesInGroups;
        this.context = context;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.group_item, parent, false);
        return new GroupViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        NewGameDTO newGameDTO = gamesInGroups.get(position);
        Glide.with(context)
                .load(newGameDTO.getTournamentLogo())
                .into(holder.tournamentLogo);
        holder.tournamentName.setText(newGameDTO.getTournamentName());
        holder.groupName.setText(newGameDTO.getGroupName());

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

    public interface OnItemListener {
        void onItemClick(Bundle data);
    }
}
