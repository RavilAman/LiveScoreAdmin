package ravil.amangeldiuly.example.minelivescoreuser.web.apis;

import ravil.amangeldiuly.example.minelivescoreuser.Constants;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.ProtocolDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProtocolApi {

    @GET(Constants.PROTOCOL_BY_ID)
    Call<ProtocolDTO> getProtocolById(@Path("id") long id);
}
