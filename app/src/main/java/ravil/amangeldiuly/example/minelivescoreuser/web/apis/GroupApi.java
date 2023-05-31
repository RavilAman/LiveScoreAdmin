package ravil.amangeldiuly.example.minelivescoreuser.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.Constants;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupInfoListDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GroupApi {

    @GET(Constants.GROUP_STATISTIC)
    Call<List<GroupInfoListDTO>> getGroupStatistics(@Query("groupId") int groupId, @Query("tournamentId") int tournamentId);

    @GET(Constants.STATISTICS_FOR_TOURNAMENT)
    Call<List<GroupInfoListDTO>> getStatisticsForTournament(@Query("tournamentId") int tournamentId);
}
