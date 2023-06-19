package ravil.amangeldiuly.example.minelivescoreuser.web.apis;

import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.EventDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.SaveEventDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.SaveGoalEventDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EventApi {

    @POST(UrlConstants.POST_EVENT)
    Call<EventDTO> postEvent(@Body SaveEventDTO saveEventDTO);

    @POST(UrlConstants.POST_GOAL)
    Call<EventDTO> postGoalEvent(@Body SaveGoalEventDTO saveEventDTO);

    @POST(UrlConstants.POST_PENALTY)
    Call<EventDTO> postPenaltyEvent(@Body SaveEventDTO saveEventDTO);
}
