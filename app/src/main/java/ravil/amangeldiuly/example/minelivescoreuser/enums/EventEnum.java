package ravil.amangeldiuly.example.minelivescoreuser.enums;

public enum EventEnum {

    // event enum ID = 1
    GOAL("GOAL"),

    // 2
    ASSIST("ASSIST"),

    // 3
    YELLOW_CARD("YELLOW_CARD"),

    // 4
    RED_CARD("RED_CARD"),

    // 5
    PENALTY("PENALTY"),

    // 6
    SCORE_PENALTY("SCORE_PENALTY"),

    // 7
    MISS_PENALTY("MISS_PENALTY"),

    SECOND_YELLOW_CARD("SECOND_YELLOW_CARD");

    private final String eventName;

    EventEnum(String eventName) {
        this.eventName = eventName;
    }

    public static String getEventNameById(Long id) {
        switch (id.intValue()) {
            case 1:
                return EventEnum.GOAL.getEventName();
            case 2:
                return EventEnum.ASSIST.getEventName();
            case 3:
                return EventEnum.YELLOW_CARD.getEventName();
            case 4:
                return EventEnum.RED_CARD.getEventName();
            case 5:
                return EventEnum.PENALTY.getEventName();
            case 6:
                return EventEnum.SCORE_PENALTY.getEventName();
            case 7:
                return EventEnum.MISS_PENALTY.getEventName();
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }
    }

    public static EventEnum getEventById(Long id) {
        switch (id.intValue()) {
            case 1:
                return EventEnum.GOAL;
            case 2:
                return EventEnum.ASSIST;
            case 3:
                return EventEnum.YELLOW_CARD;
            case 4:
                return EventEnum.RED_CARD;
            case 5:
                return EventEnum.PENALTY;
            case 6:
                return EventEnum.SCORE_PENALTY;
            case 7:
                return EventEnum.MISS_PENALTY;
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }
    }


    public String getEventName() {
        return this.eventName;
    }
}
