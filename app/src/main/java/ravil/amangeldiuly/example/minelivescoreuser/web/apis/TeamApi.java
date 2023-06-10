package ravil.amangeldiuly.example.minelivescoreuser.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TeamDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TeamApi {
    @GET(UrlConstants.TEAMS_BY_TOURNAMENT)
    Call<List<TeamDTO>> findAllTeamsByTournament(@Path("tournament_id") Long tournamentId);
}
