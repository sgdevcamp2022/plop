package smilegate.plop.chat.dto;

public class APIMessage {
    private ResultEnum message;
    private Object data;

    public enum ResultEnum{
        success, failed
    }

    public APIMessage() {
    }

    public APIMessage(ResultEnum message) {
        this.message = message;
    }

    public APIMessage(ResultEnum message, Object data) {
        this.message = message;
        this.data = data;
    }

    public ResultEnum getMessage() {
        return message;
    }

    public void setMessage(ResultEnum message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
