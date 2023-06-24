package ravil.amangeldiuly.example.minelivescoreadmin.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.GroupInfoListDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GroupStatisticsApi {

    @GET(UrlConstants.GROUP_STATISTIC)
    Call<List<GroupInfoListDTO>> getGroupStatistics(@Query("groupId") int groupId, @Query("tournamentId") int tournamentId);

    @GET(UrlConstants.STATISTICS_FOR_TOURNAMENT)
    Call<List<GroupInfoListDTO>> getStatisticsForTournament(@Query("tournamentId") int tournamentId);
}
