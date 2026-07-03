package mg.yoan.course.endpoint.rest.controller;

import java.util.UUID;
import lombok.AllArgsConstructor;
import mg.yoan.course.PojaGenerated;
import mg.yoan.course.repository.UserRepository;
import mg.yoan.course.repository.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@PojaGenerated
@RestController
@AllArgsConstructor
public class UserController {

  private final UserRepository userRepository;

  @PostMapping("/users/register")
  public User register(@RequestBody RegisterUser registerUser) {
    User user =
        User.builder()
            .id(UUID.randomUUID())
            .firstName(registerUser.firstName())
            .lastName(registerUser.lastName())
            .address(registerUser.address())
            .email(registerUser.email())
            .build();
    return userRepository.save(user);
  }

  public record RegisterUser(String firstName, String lastName, String address, String email) {}
}
