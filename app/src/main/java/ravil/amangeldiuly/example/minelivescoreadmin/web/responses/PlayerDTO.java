package ravil.amangeldiuly.example.minelivescoreadmin.web.responses;

public class PlayerDTO {

    private Long playerId;
    private Long teamId;
    private String teamName;
    private String name;
    private String surname;
    private Integer playerNumber;
    private String role;

    public PlayerDTO(Long playerId, String teamName, String name, String surname, Integer playerNumber, String role) {
        this.playerId = playerId;
        this.teamName = teamName;
        this.name = name;
        this.surname = surname;
        this.playerNumber = playerNumber;
        this.role = role;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(Integer playerNumber) {
        this.playerNumber = playerNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerDTO playerDTO = (PlayerDTO) o;

        if (!playerId.equals(playerDTO.playerId)) return false;
        if (!name.equals(playerDTO.name)) return false;
        if (!surname.equals(playerDTO.surname)) return false;
        return role.equals(playerDTO.role);
    }

    @Override
    public int hashCode() {
        int result = playerId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + surname.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }
}