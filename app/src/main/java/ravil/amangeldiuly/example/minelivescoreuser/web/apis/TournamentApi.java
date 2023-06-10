package ravil.amangeldiuly.example.minelivescoreuser.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TournamentDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TournamentApi {

    @GET(UrlConstants.TOURNAMENT_BY_NAME)
    Call<List<TournamentDto>> findTournamentsByName(@Query("name") String name);

    @GET(UrlConstants.TOURNAMENTS)
    Call<List<TournamentDto>> findAllTournaments();

    @GET(UrlConstants.TOURNAMENTS_BY_USER)
    Call<List<TournamentDto>> findAllByUser();

    @POST(UrlConstants.UPLOAD_PLAYER_INFO)
    Call<String> uploadPlayerInfo(@Query("link") String link, @Query("tournamentId")String tournamentId);

}
