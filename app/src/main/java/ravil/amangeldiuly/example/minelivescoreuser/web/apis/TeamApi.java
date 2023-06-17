package ravil.amangeldiuly.example.minelivescoreuser.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TeamDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TeamApi {
    @GET(UrlConstants.TEAMS_BY_TOURNAMENT)
    Call<List<TeamDTO>> findAllTeamsByTournament(@Path("tournamentId") Long tournamentId);

    @GET(UrlConstants.TEAM_BY_GROUP_AND_TOURNAMENT)
    Call<List<TeamDTO>> findTeamsByTournamentAndGroups(@Query("tournamentId") int tournamentId, @Query("groupId") int groupId);
}
