package ravil.amangeldiuly.example.minelivescoreuser.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GroupApi {

    @GET(UrlConstants.GROUP_BY_TOURNAMENT)
    Call<List<GroupDTO>> findGroupsByTournament(@Query("tournamentId") int tournamentId);
}