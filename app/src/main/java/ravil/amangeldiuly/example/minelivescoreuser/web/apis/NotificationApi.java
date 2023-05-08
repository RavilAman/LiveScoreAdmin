package ravil.amangeldiuly.example.minelivescoreuser.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.Constants;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotificationApi {

    @GET(Constants.TOPIC_NAME)
    Call<String> getTopicName(@Path("tournamentId") Long tournamentId);

    @POST(Constants.CREATE_SUBSCRIPTION)
    Call<Void> createSubscription(@Path("topic") String topic, @Body List<String> registrationTokens);

    @DELETE(Constants.DELETE_SUBSCRIPTION)
    Call<Void> deleteSubscription(@Path("topic") String topic, @Path("registrationToken") String registrationToken);
}
