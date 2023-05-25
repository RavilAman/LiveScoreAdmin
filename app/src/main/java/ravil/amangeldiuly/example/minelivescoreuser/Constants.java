package ravil.amangeldiuly.example.minelivescoreuser;

public class Constants {

    public static final String BACKEND_URL = "http://192.168.0.14:8080/";

    public static final String TOURNAMENTS = "tournament";
    public static final String TOURNAMENT_BY_NAME = "tournament/tournament_name";

    public static final String TOPIC_NAME = "notification/topic/tournament/{tournamentId}";
    public static final String CREATE_SUBSCRIPTION = "notification/subscriptions/{topic}";
    public static final String DELETE_SUBSCRIPTION = "notification/subscriptions/{topic}/{registrationToken}";

    public static final String GAME_BY_DATE = "game/new/date";
}
