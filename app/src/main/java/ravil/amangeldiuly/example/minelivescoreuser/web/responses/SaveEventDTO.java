package ravil.amangeldiuly.example.minelivescoreuser.web.responses;


public class SaveEventDTO extends AbstractSaveEventDTO {

    private Long eventEnumId;

    public SaveEventDTO() {
    }

    public SaveEventDTO(Long eventEnumId) {
        this.eventEnumId = eventEnumId;
    }

    public Long getEventEnumId() {
        return eventEnumId;
    }

    public void setEventEnumId(Long eventEnumId) {
        this.eventEnumId = eventEnumId;
    }
}
