package ravil.amangeldiuly.example.minelivescoreadmin.web.apis;

import okhttp3.ResponseBody;
import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UploadTeamsApi {

    @POST(UrlConstants.UPLOAD_PLAYER_INFO)
    Call<ResponseBody> uploadPlayerInfo(@Query("link") String link, @Query("tournamentId") Long tournamentId);

}
