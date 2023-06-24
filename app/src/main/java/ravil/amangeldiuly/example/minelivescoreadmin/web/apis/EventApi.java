package ravil.amangeldiuly.example.minelivescoreadmin.web.apis;

import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.EventDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.SaveEventDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.SaveGoalEventDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EventApi {

    @POST(UrlConstants.POST_EVENT)
    Call<EventDTO> postEvent(@Body SaveEventDTO saveEventDTO);

    @POST(UrlConstants.POST_GOAL)
    Call<EventDTO> postGoalEvent(@Body SaveGoalEventDTO saveEventDTO);

    @POST(UrlConstants.POST_PENALTY)
    Call<EventDTO> postPenaltyEvent(@Body SaveEventDTO saveEventDTO);

    @PUT(UrlConstants.PUT_EVENT)
    Call<EventDTO> putEvent(@Path("id") int id, @Body SaveEventDTO saveEventDTO);

    @PUT(UrlConstants.PUT_GOAL)
    Call<EventDTO> putGoalEvent(@Path("id") int id, @Body SaveGoalEventDTO saveEventDTO);

    @PUT(UrlConstants.PUT_PENALTY)
    Call<EventDTO> putPenaltyEvent(@Path("id") int id, @Body SaveEventDTO saveEventDTO);
}
