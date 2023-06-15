package ravil.amangeldiuly.example.minelivescoreuser;

public class UrlConstants {

    public static final String BACKEND_URL = "http://192.168.0.16:8080/";

    public static final String TOURNAMENTS = "tournament";
    public static final String TOURNAMENT_BY_NAME = "tournament/tournament_name";
    public static final String TOURNAMENTS_BY_USER = "tournament/user?userId=1"; //TODO change endpoint via token
    public static final String TOURNAMENTS_NOT_FINISHED_BY_USER = "tournament/user/no_finished?userId=1"; //TODO change endpoint via token
    public static final String TOURNAMENTS_CUP_BY_USER = "tournament/user/cup?userId=1"; //TODO change endpoint via token


    public static final String TOPIC_NAME = "notification/topic/tournament/{tournamentId}";
    public static final String CREATE_SUBSCRIPTION = "notification/subscriptions/{topic}";
    public static final String DELETE_SUBSCRIPTION = "notification/subscriptions/{topic}/{registrationToken}";

    public static final String GAME_BY_DATE = "game/new/date";
    public static final String GAME_LIVE = "game/live";

    public static final String PROTOCOL_BY_ID = "protocol/{id}";

    public static final String GROUP_STATISTIC = "group_info/group/points";
    public static final String STATISTICS_FOR_TOURNAMENT = "group_info/all_group/points";

    public static final String PLAYER_STATISTIC_ALL = "player_statistics/top_five/{tournament_id}";
    public static final String PLAYER_STATISTIC_ASSISTS = "player_statistics/assists";
    public static final String PLAYER_STATISTIC_GOALS = "player_statistics/goals";
    public static final String PLAYER_STATISTIC_RED_CARD = "player_statistics/red_card";
    public static final String PLAYER_STATISTIC_YELLOW_CARD = "player_statistics/yellow_card";

    public static final String TEAM_STATISTICS_ALL = "team_statistics/top_five/{tournament_id}";
    public static final String TEAM_STATISTICS_GOALS = "team_statistics/goals";
    public static final String TEAM_STATISTICS_RED_CARD = "team_statistics/red_cards/{tournament_id}";
    public static final String TEAM_STATISTICS_YELLOW_CARD = "team_statistics/yellow_cards/{tournament_id}";

    public static final String AUTH_LOGIN = "auth/login";

    public static final String UPLOAD_PLAYER_INFO = "info/upload/player_info/link";
    public static final String TEAMS_BY_TOURNAMENT = "team/group/{tournamentId}";
    public static final String PLAYER_BY_TEAM = "player/team/{team_id}";
    public static final String UPDATE_PLAYER_TEAM = "player/update/players";

    public static final String CREATE_IN_DRAW = "group_info/create_draw_in_cup";
    public static final String AFTER_DRAW = "group_info/tables_after_draw/{tournamentId}";
    public static final String ALL_GROUP_BY_POINT = "group_info/all_group/points";
    public static final String FINISH_LEAGUE = "group_info/finish_league/{tournament_id}";
    public static final String GROUP_TABS = "group/group_tabs";

}
