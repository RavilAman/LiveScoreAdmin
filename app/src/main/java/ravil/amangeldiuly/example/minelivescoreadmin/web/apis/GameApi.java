package ravil.amangeldiuly.example.minelivescoreadmin.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.GameDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.NewGameDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.SaveGameDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GameApi {

    @GET(UrlConstants.GAME_BY_DATE)
    Call<List<NewGameDTO>> getGamesByDate(@Query("date") String date);

    @GET(UrlConstants.GAME_LIVE)
    Call<List<NewGameDTO>> getLiveGames();

    @POST(UrlConstants.POST_GAME)
    Call<GameDTO> postGame(@Body SaveGameDTO saveGameDTO);

    @POST(UrlConstants.START_GAME)
    Call<GameDTO> startGame(@Path("id") int id);

    @POST(UrlConstants.END_GAME)
    Call<GameDTO> endGame(@Path("id") int id);
}
