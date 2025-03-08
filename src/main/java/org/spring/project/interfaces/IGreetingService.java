package org.spring.project.interfaces;

import org.spring.project.dto.GreetingDTO;
import org.spring.project.dto.UserDTO;
import java.util.List;


public interface IGreetingService {
    GreetingDTO addGreeting(UserDTO user);
    GreetingDTO getGreetingById(long id);
    List<GreetingDTO> getAllGreetings();
    GreetingDTO editGreeting(long id, UserDTO user);
    void deleteGreeting(long id);
}
