//package ravil.amangeldiuly.example.minelivescoreuser.notification;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//import ravil.amangeldiuly.example.minelivescoreuser.utils.SharedPreferencesUtil;
//
//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//
//    @Override
//    public void onNewToken(String token) {
//        super.onNewToken(token);
//        getSharedPreferences("LiveScoreUser", MODE_PRIVATE).edit().putString("fcm_token", token).apply();
//    }
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Log.d("INFO: ", "Received message: " + remoteMessage.getData().get("body"));
//        super.onMessageReceived(remoteMessage);
//    }
//
//    public static String getToken(Context context) {
//        return SharedPreferencesUtil.getValue(context, "fcm_token");
//    }
//}


// todo: не работает он нью токен, чекнуть почему