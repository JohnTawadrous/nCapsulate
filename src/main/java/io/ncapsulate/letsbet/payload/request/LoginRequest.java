package io.ncapsulate.letsbet.payload.request;

import jakarta.validation.constraints.NotBlank;

/**
 * LoginRequest represents the data structure for user authentication requests.
 * It contains fields for the user's username and password.
 * The class is used in the authentication process to capture user credentials.
 */
public class LoginRequest {

    @NotBlank
    private String username; // User's username for authentication.

    @NotBlank
    private String password; // User's password for authentication.

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
