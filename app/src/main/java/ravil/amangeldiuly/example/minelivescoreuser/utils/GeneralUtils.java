package ravil.amangeldiuly.example.minelivescoreuser.utils;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
}
