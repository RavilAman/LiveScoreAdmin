package ravil.amangeldiuly.example.minelivescoreadmin.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.DistinctPlayerStatisticsDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.PlayerStatisticsAllDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlayerStatisticsApi {

    @GET(UrlConstants.PLAYER_STATISTIC_ALL)
    Call<List<PlayerStatisticsAllDTO>> getAll(@Query("tournament_id") int tournamentId);

    @GET(UrlConstants.PLAYER_STATISTIC_ASSISTS)
    Call<List<DistinctPlayerStatisticsDTO>> getAssists(@Query("tournament_id") int tournamentId);

    @GET(UrlConstants.PLAYER_STATISTIC_GOALS)
    Call<List<DistinctPlayerStatisticsDTO>> getGoals(@Query("tournament_id") int tournamentId);

    @GET(UrlConstants.PLAYER_STATISTIC_RED_CARD)
    Call<List<DistinctPlayerStatisticsDTO>> getRedCards(@Query("tournament_id") int tournamentId);

    @GET(UrlConstants.PLAYER_STATISTIC_YELLOW_CARD)
    Call<List<DistinctPlayerStatisticsDTO>> getYellowCards(@Query("tournament_id") int tournamentId);
}
