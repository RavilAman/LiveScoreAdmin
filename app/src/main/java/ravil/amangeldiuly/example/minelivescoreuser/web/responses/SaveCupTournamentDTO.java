package ravil.amangeldiuly.example.minelivescoreuser.web.responses;

public class SaveCupTournamentDTO extends SaveTournamentDTO {

    private boolean isPlayOf;

    public SaveCupTournamentDTO(String tournamentName, String tournamentType, String tournamentLogo, String location, Integer teamsNum,boolean isPlayOf) {
        super(tournamentName, tournamentType, tournamentLogo, location, teamsNum);
        this.isPlayOf = isPlayOf;
    }

    public boolean isPlayOf() {
        return isPlayOf;
    }

    public void setPlayOf(boolean playOf) {
        isPlayOf = playOf;
    }

    @Override
    public String toString() {
        return "SaveCupTournamentDTO{" +
                "tournamentName='" + super.getTournamentName() + '\'' +
                ", tournamentType='" + super.getTournamentType()  + '\'' +
                ", tournamentLogo='" + super.getTournamentLogo()  + '\'' +
                ", location='" + super.getLocation()  + '\'' +
                ", teamsNum=" + super.getTeamsNum()  + '\'' +
                ", isPlayOf=" + isPlayOf +
                '}';
    }
}
