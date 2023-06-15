package ravil.amangeldiuly.example.minelivescoreuser.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.AfterDrawDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupInfoDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupInfoListDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.SaveGroupInfoDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TeamDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GroupInfoApi {
    @POST(UrlConstants.CREATE_IN_DRAW)
    Call<List<GroupInfoDTO>> createDrawInCup(@Body List<SaveGroupInfoDTO> drawList);

    @GET(UrlConstants.AFTER_DRAW)
    Call<List<AfterDrawDTO>> teamAfterDraw(@Path("tournamentId") Long tournamentId);

    @GET(UrlConstants.ALL_GROUP_BY_POINT)
    Call<List<GroupInfoListDTO>> allGroupsByPoint(@Query("tournamentId") Long tournamentId);

    @POST(UrlConstants.FINISH_LEAGUE)
    Call<TeamDTO> finishLeague(@Path("tournament_id") Long tournamentId);
}
