package ravil.amangeldiuly.example.minelivescoreadmin.web.apis;

import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.AuthRequest;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.AuthResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST(UrlConstants.AUTH_LOGIN)
    Call<AuthResponse> login(@Body AuthRequest authRequest);
}
