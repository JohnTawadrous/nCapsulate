package io.ncapsulate.letsbet.controllers;

import io.ncapsulate.letsbet.models.User;
import io.ncapsulate.letsbet.payload.response.UserAccountResponse;
import io.ncapsulate.letsbet.repository.UserRepository;
import io.ncapsulate.letsbet.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserRepository userRepository;

    @GetMapping("/account")
    public ResponseEntity<?> getUserAccountPage() {
        // Get the UserDetails from the security context
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Use the userDetails.getId() to fetch the corresponding user from the database
        // For simplicity, I assume you have a UserRepository autowired in this class

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Now, you can return the user information to be displayed on the account page
        return ResponseEntity.ok(new UserAccountResponse(user.getId(), user.getUsername(), user.getEmail()));
    }
}
