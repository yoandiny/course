package mg.yoan.course.endpoint.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import mg.yoan.course.conf.FacadeIT;
import mg.yoan.course.endpoint.rest.controller.UserController.RegisterUser;
import mg.yoan.course.repository.UserRepository;
import mg.yoan.course.repository.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserIT extends FacadeIT {

  @Autowired private UserController userController;

  @Autowired private UserRepository userRepository;

  @Test
  void can_register_user() {
    RegisterUser registerUser =
        new RegisterUser("John", "Doe", "123 Main St", "john.doe@example.com");

    User registered = userController.register(registerUser);

    assertNotNull(registered.getId());
    assertEquals("John", registered.getFirstName());
    assertEquals("Doe", registered.getLastName());
    assertEquals("123 Main St", registered.getAddress());
    assertEquals("john.doe@example.com", registered.getEmail());

    User fetched = userRepository.findById(registered.getId()).orElse(null);
    assertNotNull(fetched);
    assertEquals("John", fetched.getFirstName());
  }
}
