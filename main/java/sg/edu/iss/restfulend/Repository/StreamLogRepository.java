package sg.edu.iss.restfulend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sg.edu.iss.restfulend.Model.StreamLog;

@Repository
public interface StreamLogRepository extends JpaRepository<StreamLog, String> {
	
	@Query(value = "select avg(s.num_likes) from stream_log s where s.seller_id = ?1" , nativeQuery = true)
	Double getAvgLikesByUserId(String id);
  
   @Query("SELECT AVG(s.numLikes) FROM StreamLog s") 	
	Double getAverageRating();
	
}
 