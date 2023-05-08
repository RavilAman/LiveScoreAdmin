package ravil.amangeldiuly.example.minelivescoreuser.web.apis;

import ravil.amangeldiuly.example.minelivescoreuser.Constants;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NotificationApi {

    @GET(Constants.TOPIC_NAME)
    Call<String> getTopicName(@Path("tournamentId") Long tournamentId);
}
