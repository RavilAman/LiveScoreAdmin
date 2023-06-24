package ravil.amangeldiuly.example.minelivescoreadmin.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.AfterDrawDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.FinishStageDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.GroupInfoDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.GroupInfoListDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.SaveGroupInfoDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TeamDTO;
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

    @GET(UrlConstants.GROUP_INFO_BY_GROUP)
    Call<List<GroupInfoListDTO>> groupInfoByGroup(@Query("groupId") Long groupId, @Query("tournamentId") Long tournamentId);

    @POST(UrlConstants.FINISH_LEAGUE)
    Call<TeamDTO> finishLeague(@Path("tournament_id") Long tournamentId);

    @POST(UrlConstants.FINISH_GROUP_STAGE)
    Call<List<TeamDTO>> finishGroupStage(@Path("tournament_id") Long tournamentId);

    @POST(UrlConstants.FINISH_STAGE)
    Call<List<TeamDTO>> finishStage(@Path("tournament_id") Long tournamentId, @Body FinishStageDTO finishStageDTO);
}
