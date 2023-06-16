package ravil.amangeldiuly.example.minelivescoreuser.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GameDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.NewGameDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.SaveGameDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GameApi {

    @GET(UrlConstants.GAME_BY_DATE)
    Call<List<NewGameDTO>> getGamesByDate(@Query("date") String date);

    @GET(UrlConstants.GAME_LIVE)
    Call<List<NewGameDTO>> getLiveGames();

    @POST(UrlConstants.POST_GAME)
    Call<GameDTO> postGame(@Body SaveGameDTO saveGameDTO);
}
