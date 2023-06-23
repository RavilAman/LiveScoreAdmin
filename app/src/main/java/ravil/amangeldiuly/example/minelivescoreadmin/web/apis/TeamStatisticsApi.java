package ravil.amangeldiuly.example.minelivescoreadmin.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.DistinctTeamStatisticsDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.TeamStatisticsAllDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TeamStatisticsApi {

    @GET(UrlConstants.TEAM_STATISTICS_ALL)
    Call<List<TeamStatisticsAllDTO>> getAll(@Path("tournament_id") int tournamentId);

    @GET(UrlConstants.TEAM_STATISTICS_GOALS)
    Call<List<DistinctTeamStatisticsDTO>> getGoals(@Query("tournament_id") int tournamentId);

    @GET(UrlConstants.TEAM_STATISTICS_RED_CARD)
    Call<List<DistinctTeamStatisticsDTO>> getRedCards(@Path("tournament_id") int tournamentId);

    @GET(UrlConstants.TEAM_STATISTICS_YELLOW_CARD)
    Call<List<DistinctTeamStatisticsDTO>> getYellowCards(@Path("tournament_id") int tournamentId);
}
