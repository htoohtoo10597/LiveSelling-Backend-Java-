package sg.edu.iss.restfulend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sg.edu.iss.restfulend.Model.ChannelStream;
import sg.edu.iss.restfulend.Model.Stream;

import java.util.List;

@Repository
public interface ChannelStreamRepository extends JpaRepository<ChannelStream, String> {
    @Query("SELECT scs FROM ChannelStream cs JOIN cs.streams scs WHERE cs.user.id = :id")
    List<Stream> getStreamsByUserId(String id);
}
