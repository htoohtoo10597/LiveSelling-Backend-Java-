package sg.edu.iss.restfulend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sg.edu.iss.restfulend.Model.Rating;


@Repository
public interface RatingRepository extends JpaRepository<Rating, String> {
	
	@Query(value = "select avg(r.rate) from rating r where r.user_id = ?1" , nativeQuery = true)
		Double getAvgRatingByUserId(String id);
	  
	@Query("SELECT AVG(r.rate) FROM Rating r") 	
		Double getAverageRating();
	
}
