package sg.edu.iss.restfulend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.iss.restfulend.Model.LoginBag;
import sg.edu.iss.restfulend.Model.User;
import sg.edu.iss.restfulend.Repository.UserRepository;

@CrossOrigin(origins= "*")
@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    UserRepository userRepo;

    @PostMapping("/login")
    public ResponseEntity<User> loginCheck(@RequestBody LoginBag loginUser) {
        if (userRepo.findUserByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword()) != null) {
            User user = userRepo.findUserByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
