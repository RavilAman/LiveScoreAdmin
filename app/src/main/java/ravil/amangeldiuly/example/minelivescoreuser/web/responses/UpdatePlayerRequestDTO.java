package ravil.amangeldiuly.example.minelivescoreuser.web.responses;

public class UpdatePlayerRequestDTO {
    private long newTeamId;
    private long playerId;

    public UpdatePlayerRequestDTO(long newTeamId, long playerId) {
        this.newTeamId = newTeamId;
        this.playerId = playerId;
    }

    public long getNewTeamId() {
        return newTeamId;
    }

    public void setNewTeamId(long newTeamId) {
        this.newTeamId = newTeamId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }
}