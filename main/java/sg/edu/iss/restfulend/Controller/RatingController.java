package sg.edu.iss.restfulend.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sg.edu.iss.restfulend.Model.Rating;
import sg.edu.iss.restfulend.Repository.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



@CrossOrigin
@RestController
@RequestMapping("/api/rating")
public class RatingController {
	
	private static final DecimalFormat df = new DecimalFormat("0.0");

    @Autowired
    RatingRepository ratingRepo;
    UserRepository userRepo;
    
    @Autowired
    UserController userCont;
  
    @GetMapping("/avgrating")
    public String getAverageRating() {    	
    	Double avgrating = ratingRepo.getAverageRating();     
        if (avgrating != null) {
        	int ratingRounded = (int)Math.round(avgrating);
            return String.valueOf(ratingRounded);
        }      
        	//no rating yet        	
            return "0";
    }
    
    @GetMapping("/userrating/{userId}")    
    public String getUserRating(@PathVariable("userId") String userId) { 
    	Double rating = ratingRepo.getAvgRatingByUserId(userId);
        if (rating != null) {
        	int ratingRounded = (int)Math.round(rating);
            return String.valueOf(ratingRounded);
        }   
        	//the user has no rating yet        	
            return "0";        
    }
    
    @GetMapping("/channelrating/{channelId}")
    public Double getChannelAvgRating(@PathVariable("channelId") String channelId) { 
    	List<Integer> ratingList = ratingRepo.findAll()
    			.stream()
    			.filter(rating -> rating.getChannel().getId().equals(channelId))
    			.map(rating -> rating.getRate())
    			.collect(Collectors.toList());
    	
    	Double avg = 0.0;
    	
    	for (Integer i : ratingList) {
    		avg += i;
    	}
    	
    	return avg/ratingList.size();     
    }
    
    @PostMapping("/sendrating/{channelId}/{userId}")
    public ResponseEntity<Rating> saveRating(@RequestBody Rating rating, @PathVariable("channelId") String channelId, @PathVariable("userId") String userId) { 
    	Rating newRating = new Rating(rating.getRate(), userCont.findChannelById(channelId), userCont.findUserById(userId));
    	ratingRepo.save(newRating);
    	
    	return new ResponseEntity<>(newRating, HttpStatus.OK);     
    }
    
}
