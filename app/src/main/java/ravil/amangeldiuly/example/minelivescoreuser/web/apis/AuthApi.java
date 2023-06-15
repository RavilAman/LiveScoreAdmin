package ravil.amangeldiuly.example.minelivescoreuser.web.apis;

import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.AuthRequest;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.AuthResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST(UrlConstants.AUTH_LOGIN)
    Call<AuthResponse> login(@Body AuthRequest authRequest);
}
