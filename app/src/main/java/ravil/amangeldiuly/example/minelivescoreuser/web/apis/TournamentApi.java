package ravil.amangeldiuly.example.minelivescoreuser.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.Constants;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TournamentDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TournamentApi {

    @GET(Constants.TOURNAMENT_BY_NAME)
    Call<List<TournamentDto>> findTournamentsByName(@Query("name") String name);

    @GET(Constants.TOURNAMENTS)
    Call<List<TournamentDto>> findAllTournaments();

    @GET(Constants.TOURNAMENTS_BY_USER)
    Call<List<TournamentDto>> findAllByUser();
}
