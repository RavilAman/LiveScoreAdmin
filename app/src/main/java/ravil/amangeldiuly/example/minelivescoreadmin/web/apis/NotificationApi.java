package ravil.amangeldiuly.example.minelivescoreadmin.web.apis;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.CustomNotificationDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotificationApi {

    @GET(UrlConstants.TOPIC_NAME)
    Call<String> getTopicName(@Path("tournamentId") Long tournamentId);

    @POST(UrlConstants.CREATE_SUBSCRIPTION)
    Call<Void> createSubscription(@Path("topic") String topic, @Body List<String> registrationTokens);

    @DELETE(UrlConstants.DELETE_SUBSCRIPTION)
    Call<Void> deleteSubscription(@Path("topic") String topic, @Path("registrationToken") String registrationToken);

    @POST(UrlConstants.POST_TO_TOPIC)
    Call<String> postToTopic(@Path("topic") String topicName, @Body CustomNotificationDto customNotificationDto);

    @GET(UrlConstants.TOPIC_NAME_BY_PROTOCOL)
    Call<String> getTopicNameByProtocolId(@Path("protocolId") int protocolId);
}
