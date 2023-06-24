package ravil.amangeldiuly.example.minelivescoreadmin.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.SaveCupTournamentDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.SaveTournamentDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TournamentDto;
import retrofit2.Call;
import retrofit2.http.Body;
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

    @GET(UrlConstants.TOURNAMENTS_NOT_FINISHED_BY_USER)
    Call<List<TournamentDto>> findAllNotFinishedByUser();

    @GET(UrlConstants.TOURNAMENTS_CUP_BY_USER)
    Call<List<TournamentDto>> findAllCupByUser();

    @GET(UrlConstants.TOURNAMENT_BY_USER)
    Call<List<TournamentDto>> findTournamentsByUser();

    @POST(UrlConstants.CREATE_TOURNAMENT_LEAGUE)
    Call<TournamentDto> createTournamentLeague(@Body SaveTournamentDTO saveTournamentDTO);

    @POST(UrlConstants.CREATE_TOURNAMENT_CUP)
    Call<TournamentDto> createTournamentCup(@Body SaveCupTournamentDTO saveTournamentDTO);
}
