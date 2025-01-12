package fr.pantheonsorbonne.service;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named("userValidator")
@RegisterForReflection
public class UserValidator {
    @Inject
    private UserService userService;

    public Boolean validateUser(String email) {
        // Retourne true si l'utilisateur existe, false sinon
        return userService.getUserByEmail(email) != null;
    }
}

