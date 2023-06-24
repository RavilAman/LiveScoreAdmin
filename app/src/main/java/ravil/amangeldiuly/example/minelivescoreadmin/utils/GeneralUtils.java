package ravil.amangeldiuly.example.minelivescoreadmin.utils;

import java.util.List;
import java.util.stream.Collectors;

import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TournamentDto;

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

    public static String beautifyScoreForNotification(String score, boolean first) {
        Integer team1Score = Integer.parseInt(score.substring(0, score.indexOf(':')));
        Integer team2Score = Integer.parseInt(score.substring(score.indexOf(':') + 1));

        if (first) {
            team1Score++;
            return "[" + team1Score + "]"
                    + ":" + team2Score;
        } else {
            team2Score++;
            return team1Score + ":" +
                    "[" + team2Score + "]";
        }
    }

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
