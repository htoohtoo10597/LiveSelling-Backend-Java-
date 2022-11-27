package sg.edu.iss.restfulend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sg.edu.iss.restfulend.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    @Query("SELECT u FROM User u WHERE u.username = :un AND u.password = :pw")
    User findUserByUsernameAndPassword(String un, String pw);
    
}
