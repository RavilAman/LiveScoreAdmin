package ravil.amangeldiuly.example.minelivescoreuser.utils;

import java.util.List;
import java.util.stream.Collectors;

import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TournamentDto;

public class GeneralUtils {

    private GeneralUtils() {
    }

    public static List<TournamentDto> getTournamentsByName(List<TournamentDto> tournaments, String subName) {
        return tournaments.stream()
                .filter(t -> t.getTournamentName().toLowerCase()
                        .contains(subName.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static String titleCaseWord(String word) {
        char[] wordChars = word.toCharArray();
        wordChars[0] = Character.toUpperCase(wordChars[0]);
        for (int n = wordChars.length, i = 1; i < n; i++) {
            wordChars[i] = Character.toLowerCase(wordChars[i]);
        }
        return String.valueOf(wordChars);
    }

    public static String getGameScoreForTeam(String gameScore, int teamNumber) {
        if (teamNumber == 1) {
            return gameScore.substring(0, gameScore.indexOf(":"));
        } else if (teamNumber == 2) {
            return gameScore.substring(gameScore.indexOf(":") + 1);
        }
        return "";
    }

    public static String gameScoreIntoDashFormat(String gameScore) {
        return gameScore.substring(0, gameScore.indexOf(":")) + " - "
                + gameScore.substring(gameScore.indexOf(":") + 1);
    }

//    public static<T> initializeRetrofit() {
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
//                .create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(UrlConstants.BACKEND_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//        groupApi = retrofit.create(GroupApi.class);
//    }

    // todo: починить, не работает
//    public static boolean isConnected(FragmentActivity fragmentActivity) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) fragmentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivityManager != null) {
//            NetworkInfo connection = connectivityManager.getActiveNetworkInfo();
//            return connection != null && connection.isConnected();
//        }
//        return true;
//    }
}
