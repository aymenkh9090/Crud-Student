package com.crud_app.service;

import com.crud_app.model.ERole;
import com.crud_app.model.User;

public interface IUserService {
    User registerUser(User userDto, ERole role);
    void sendActivationEmail(User user);
    boolean activateUserAccount(String token);

}
