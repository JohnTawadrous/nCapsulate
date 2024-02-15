package io.ncapsulate.letsbet.payload.response;

/**
 * MessageResponse is a simple response class used to encapsulate messages.
 * It is typically used for returning messages in API responses, such as success
 * messages or error messages during user authentication and registration.
 */
public class MessageResponse {

    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
