package ravil.amangeldiuly.example.minelivescoreuser.web.requests;


import java.util.List;

public class CustomNotificationDto {

    private String title;
    private String body;
    private String image = "https://cdn-icons-png.flaticon.com/512/53/53283.png";
    private List<String> registrationTokens;

    public CustomNotificationDto() {
    }

    public CustomNotificationDto(String title, String body, String image, List<String> registrationTokens) {
        this.title = title;
        this.body = body;
        this.image = image;
        this.registrationTokens = registrationTokens;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getRegistrationTokens() {
        return registrationTokens;
    }

    public void setRegistrationTokens(List<String> registrationTokens) {
        this.registrationTokens = registrationTokens;
    }
}
