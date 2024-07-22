package dto;

public class VideoDTO {
    public String message;
    public String status;
    public String url;

    @Override
    public String toString() {
        return "VideoAPIResponseDTO { message='" + this.message + ", status='" + this.status + "', url=" + this.url + "' }";
    }
}
