package ravil.amangeldiuly.example.minelivescoreadmin.web.requests;


public class CreateTopicDTO {

    private String topicName;
    private Long tournamentId;
    private String registrationToken;

    public CreateTopicDTO() {
    }

    public CreateTopicDTO(String topicName, Long tournamentId, String registrationToken) {
        this.topicName = topicName;
        this.tournamentId = tournamentId;
        this.registrationToken = registrationToken;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }
}
