package ravil.amangeldiuly.example.minelivescoreuser;

public class UrlConstants {

    public static final String BACKEND_URL = "http://192.168.0.14:8080/";

    public static final String TOURNAMENTS = "tournament";
    public static final String TOURNAMENT_BY_NAME = "tournament/tournament_name";

    public static final String TOPIC_NAME = "notification/topic/tournament/{tournamentId}";
    public static final String CREATE_SUBSCRIPTION = "notification/subscriptions/{topic}";
    public static final String DELETE_SUBSCRIPTION = "notification/subscriptions/{topic}/{registrationToken}";

    public static final String GAME_BY_DATE = "game/new/date";
    public static final String GAME_LIVE = "game/live";

    public static final String PROTOCOL_BY_ID = "protocol/{id}";

    public static final String GROUP_STATISTIC = "group_info/group/points";
    public static final String STATISTICS_FOR_TOURNAMENT = "group_info/all_group/points";

    public static final String PLAYER_STATISTIC_ASSISTS = "player_statistics/assists";
    public static final String PLAYER_STATISTIC_GOALS = "player_statistics/goals";
    public static final String PLAYER_STATISTIC_RED_CARD = "player_statistics/red_card";
    public static final String PLAYER_STATISTIC_YELLOW_CARD = "player_statistics/yellow_card";
}
