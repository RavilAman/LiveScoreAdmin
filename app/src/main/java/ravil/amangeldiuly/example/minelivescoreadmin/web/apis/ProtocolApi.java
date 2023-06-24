package ravil.amangeldiuly.example.minelivescoreadmin.web.apis;

import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.ProtocolDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProtocolApi {

    @GET(UrlConstants.PROTOCOL_BY_ID)
    Call<ProtocolDTO> getProtocolById(@Path("id") long id);
}
