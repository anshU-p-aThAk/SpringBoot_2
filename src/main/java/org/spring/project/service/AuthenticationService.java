package org.spring.project.service;

import org.spring.project.dto.ForgotPasswordDTO;
import org.spring.project.dto.ResetPasswordDTO;
import org.spring.project.interfaces.IAuthenticationService;
import org.spring.project.util.JwtToken;
import org.spring.project.exception.UserException;
import org.spring.project.dto.AuthUserDTO;
import org.spring.project.dto.LoginDTO;
import org.spring.project.model.AuthUser;
import org.spring.project.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private JwtToken tokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthUser register(AuthUserDTO userDTO) throws Exception {
        AuthUser user = new AuthUser(userDTO);
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        String token = tokenUtil.createToken(user.getUserId());

        user.setPassword(encodedPassword);
        user.setResetToken(token);
        System.out.println(user);

        authUserRepository.save(user);
        emailSenderService.sendEmail(user.getEmail(),"Welcome to MyHI App", "Hello "
                + user.getFirstName()
                + "\n I am MyHI, your Assistant.\nYou have been successfully registered to MyHI Platform!.\n"
                + "Feel free to ask anything.\n\n\n"
                + "Till then, Here is your Profile and Registration Details:\n\n User Id:  "
                + user.getUserId() + "\n First Name:  "
                + user.getFirstName() + "\n Last Name:  "
                + user.getLastName() + "\n Email:  "
                + user.getEmail() + "\n Address:  "
                + "\n Token:  " + token);
        return user;
    }

    public Optional<AuthUser> existsByEmail(String email) {
        Optional<AuthUser> user = Optional.ofNullable(authUserRepository.findByEmail(email));
        return user;
    }

    @Override
    public String login(LoginDTO loginDTO) throws UserException {
        Optional<AuthUser> user = existsByEmail(loginDTO.getEmail());
        if (user.isPresent() && passwordEncoder.matches(loginDTO.getPassword(), user.get().getPassword())) {
            emailSenderService.sendEmail(user.get().getEmail(),"Logged in Successfully!", "Hii...."+user.get().getFirstName()+"\n\n You have successfully logged in into Greeting App!");
            return "Congratulations!! You have logged in successfully!";
        } else if (!user.isPresent()) {
            throw new UserException("Sorry! User not Found!");
        } else if (!passwordEncoder.matches(loginDTO.getPassword(), user.get().getPassword())) {
            throw new UserException("Sorry! Password is incorrect!");
        } else {
            throw new UserException("Sorry! Email or Password is incorrect!");
        }
    }

    @Override
    public String forgotPassword(String email, ForgotPasswordDTO forgotPasswordDTO) throws UserException {
        AuthUser user = authUserRepository.findByEmail(email);
        if (user != null) {
            String newEncodedPassword = passwordEncoder.encode(forgotPasswordDTO.getPassword());
            user.setPassword(newEncodedPassword);
            String token = tokenUtil.createToken(user.getUserId());
            user.setResetToken(token);
            authUserRepository.save(user);
            return "Password has been changed successfully!";
        } else {
            throw new UserException("Sorry! User not Found!");
        }
    }

    @Override
    public String resetPassword(String email, ResetPasswordDTO resetPasswordDTO) throws UserException {
        AuthUser user = authUserRepository.findByEmail(email);
        if (user != null) {
            if (passwordEncoder.matches(resetPasswordDTO.getCurrentPassword(), user.getPassword())) {
                String resetEncodedPassword = passwordEncoder.encode(resetPasswordDTO.getNewPassword());
                user.setPassword(resetEncodedPassword);
                authUserRepository.save(user);
                return "Password reset successfully!";
            } else {
                throw new UserException("Sorry! Current Password is incorrect!");
            }
        } else {
            throw new UserException("Sorry! User not Found with email: " + email);
        }
    }
}
