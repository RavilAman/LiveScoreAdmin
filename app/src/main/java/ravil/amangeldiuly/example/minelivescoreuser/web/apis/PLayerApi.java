package ravil.amangeldiuly.example.minelivescoreuser.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.PlayerDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TeamDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PLayerApi {
    @GET(UrlConstants.PLAYER_BY_TEAM)
    Call<List<PlayerDTO>> findAllPlayerByTeamId(@Path("team_id")Long teamId);
}
