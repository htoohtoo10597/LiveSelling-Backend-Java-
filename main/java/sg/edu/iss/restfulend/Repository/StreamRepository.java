package sg.edu.iss.restfulend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sg.edu.iss.restfulend.Helper.StreamStatus;
import sg.edu.iss.restfulend.Model.Stream;

import java.util.List;

@Repository
public interface StreamRepository extends JpaRepository<Stream, String> {
    @Query("SELECT s FROM Stream s JOIN s.channel sc WHERE sc.user.id = :id AND s.status = :status")
    List<Stream> getStreamsByUserIdAndStatus(String id, StreamStatus status);    
    
    @Query(value ="SELECT * FROM stream s\r\n"
    		+ "join channel_stream cs\r\n"
    		+ "on s.channel_id=cs.id\r\n"
    		+ "where s.schedule >= CURRENT_TIMESTAMP\r\n"
    		+ "and s.status ='PENDING' and cs.user_id = ?1\r\n"
    		+ "order by s.schedule asc\r\n"
    		+ "limit 3 " , nativeQuery = true)    
     List<Stream> getThreeClosestStreamsByUserId(String id);
    
    @Query(value ="SELECT count(*) FROM stream s\r\n"
    		+ "join channel_stream cs\r\n"
    		+ "on s.channel_id=cs.id\r\n"
    		+ "where s.schedule >= CURRENT_TIMESTAMP\r\n"
    		+ "and s.status ='PENDING' and cs.user_id = ?1", nativeQuery = true)    	
     Integer getPendingStreamCountByUser(String id);
    
}
