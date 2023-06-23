package ravil.amangeldiuly.example.minelivescoreadmin;

public class UrlConstants {

    public static final String BACKEND_URL = "http://192.168.139.76:8080/";

    public static final String TOURNAMENTS = "tournament";
    public static final String TOURNAMENT_BY_NAME = "tournament/tournament_name";
    public static final String TOURNAMENT_BY_USER = "tournament/user/no_finished";
    public static final String TOURNAMENTS_BY_USER = "tournament/user";
    public static final String TOURNAMENTS_NOT_FINISHED_BY_USER = "tournament/user/no_finished";
    public static final String TOURNAMENTS_CUP_BY_USER = "tournament/user/cup";
    public static final String CREATE_TOURNAMENT_LEAGUE = "tournament/league";
    public static final String CREATE_TOURNAMENT_CUP = "tournament/cup";

    public static final String TOPIC_NAME = "notification/topic/tournament/{tournamentId}";
    public static final String CREATE_SUBSCRIPTION = "notification/subscriptions/{topic}";
    public static final String DELETE_SUBSCRIPTION = "notification/subscriptions/{topic}/{registrationToken}";
    public static final String POST_TO_TOPIC = "notification/topic/{topic}";
    public static final String TOPIC_NAME_BY_PROTOCOL = "notification/topic/{protocolId}";

    public static final String GAME_BY_DATE = "game/new/date";
    public static final String GAME_LIVE = "game/live";
    public static final String POST_GAME = "game";
    public static final String START_GAME = "game/start/{id}";
    public static final String END_GAME = "game/end/{id}";

    public static final String GROUP_BY_TOURNAMENT = "group/tournament";
    public static final String GROUP_TABS = "group/group_tabs";

    public static final String TEAM_BY_GROUP_AND_TOURNAMENT = "team/group";
    public static final String TEAMS_BY_TOURNAMENT = "team/group/{tournamentId}";

    public static final String PROTOCOL_BY_ID = "protocol/{id}";

    public static final String GROUP_STATISTIC = "group_info/group/points";
    public static final String STATISTICS_FOR_TOURNAMENT = "group_info/all_group/points";
    public static final String CREATE_IN_DRAW = "group_info/create_draw_in_cup";
    public static final String AFTER_DRAW = "group_info/tables_after_draw/{tournamentId}";
    public static final String ALL_GROUP_BY_POINT = "group_info/all_group/points";
    public static final String FINISH_LEAGUE = "group_info/finish_league/{tournament_id}";
    public static final String GROUP_INFO_BY_GROUP = "group_info/group/points";
    public static final String FINISH_GROUP_STAGE = "group_info/finish_group_stage/{tournament_id}";
    public static final String FINISH_STAGE = "group_info/finish_stage/{tournament_id}";

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

    public static final String PLAYER_BY_TEAM = "player/team/{team_id}";
    public static final String UPDATE_PLAYER_TEAM = "player/update/players";

    public static final String POST_EVENT = "event";
    public static final String POST_GOAL = "event/save_goal";
    public static final String POST_PENALTY = "event/save_penalty";
    public static final String PUT_EVENT = "event/{id}";
    public static final String PUT_GOAL = "event/update_goal/{id}";
    public static final String PUT_PENALTY = "event/update_penalty/{id}";
}
