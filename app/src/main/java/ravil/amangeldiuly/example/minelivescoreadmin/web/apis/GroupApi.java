package ravil.amangeldiuly.example.minelivescoreadmin.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.GroupDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GroupApi {

    @GET(UrlConstants.GROUP_BY_TOURNAMENT)
    Call<List<GroupDTO>> findGroupsByTournament(@Query("tournamentId") int tournamentId);

    @GET(UrlConstants.GROUP_TABS)
    Call<List<GroupDTO>> getGroupTabs(@Query("tournamentId") Long tournamentId);
}