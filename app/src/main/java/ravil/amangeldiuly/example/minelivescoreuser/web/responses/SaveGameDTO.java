package ravil.amangeldiuly.example.minelivescoreuser.web.responses;


import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SaveGameDTO {

    private Long groupId;
    @SerializedName("dateTime")
    @JsonAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime dateTime;
    private Long team1Id;
    private Long team2Id;

    private static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            out.value(formatter.format(value));
        }

        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            String dateString = in.nextString();
            return LocalDateTime.parse(dateString, formatter);
        }
    }

    public SaveGameDTO() {
    }

    public SaveGameDTO(Long groupId, LocalDateTime dateTime, Long team1Id, Long team2Id) {
        this.groupId = groupId;
        this.dateTime = dateTime;
        this.team1Id = team1Id;
        this.team2Id = team2Id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(Long team1Id) {
        this.team1Id = team1Id;
    }

    public Long getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(Long team2Id) {
        this.team2Id = team2Id;
    }

    @Override
    public String toString() {
        return "SaveGameDTO{" +
                "groupId=" + groupId +
                ", dateTime=" + dateTime +
                ", team1Id=" + team1Id +
                ", team2Id=" + team2Id +
                '}';
    }
}
