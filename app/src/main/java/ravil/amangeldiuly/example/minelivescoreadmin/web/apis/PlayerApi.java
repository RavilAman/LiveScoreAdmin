package ravil.amangeldiuly.example.minelivescoreadmin.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.PlayerDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.UpdatePlayerRequestDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PlayerApi {

    @GET(UrlConstants.PLAYER_BY_TEAM)
    Call<List<PlayerDTO>> findAllPlayerByTeamId(@Path("team_id")Long teamId);

    @PUT(UrlConstants.UPDATE_PLAYER_TEAM)
    Call<List<PlayerDTO>> updatePlayers(@Body List<UpdatePlayerRequestDTO> updateRequests);
}
