package fr.pantheonsorbonne.service;

import fr.pantheonsorbonne.dao.UserDAO;
import fr.pantheonsorbonne.dto.UserDTO;
import fr.pantheonsorbonne.entity.User;
import fr.pantheonsorbonne.exception.InvalidUserException;
import fr.pantheonsorbonne.exception.UserAlreadyExistWithTheSameEmail;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {

    @Inject
    UserDAO userDAO;

    @Transactional
    public UserDTO getUserByID(Long id) {
        User user = userDAO.getById(id);
        if (user == null) {
            return null;
        }
        return new UserDTO(user.getName(), user.getEmail());
    }

    @Transactional
    public Long checkAndSaveUser(UserDTO userDTO) throws InvalidUserException, UserAlreadyExistWithTheSameEmail {
        //vérification métier (email correct)
        if (!userDTO.email().contains("@")) {
            throw new InvalidUserException("email is malformed");
        }
        //verification existance de l'utilisateur
        if (userDAO.isUserPresent(userDTO.email())) {
            throw new UserAlreadyExistWithTheSameEmail();
        }

        User user = new User();
        user.setEmail(userDTO.email());
        user.setName(userDTO.name());

        userDAO.saveUser(user);
        return user.getId();
    }

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

}



