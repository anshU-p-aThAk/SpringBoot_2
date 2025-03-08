package org.spring.project.interfaces;

import org.spring.project.dto.AuthUserDTO;
import org.spring.project.dto.ForgotPasswordDTO;
import org.spring.project.dto.LoginDTO;
import org.spring.project.dto.ResetPasswordDTO;
import org.spring.project.exception.UserException;
import org.spring.project.model.AuthUser;

public interface IAuthenticationService {
    AuthUser register(AuthUserDTO userDTO) throws Exception;
    String login(LoginDTO loginDTO) throws UserException;
    String forgotPassword(String email, ForgotPasswordDTO forgotPasswordDTO) throws UserException;
    String resetPassword(String email, ResetPasswordDTO resetPasswordDTO) throws UserException;
}
