package sg.edu.iss.restfulend.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sg.edu.iss.restfulend.Helper.StreamStatus;
import sg.edu.iss.restfulend.Model.ChannelStream;
import sg.edu.iss.restfulend.Model.Stream;
import sg.edu.iss.restfulend.Model.StreamLog;
import sg.edu.iss.restfulend.Model.User;
import sg.edu.iss.restfulend.Repository.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@CrossOrigin
@RestController
@RequestMapping("/api/likes")
public class StreamLogController {
	
	private static final DecimalFormat df = new DecimalFormat("0.0");

    @Autowired
    StreamLogRepository logRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    StreamRepository streamRepo;

  
    @GetMapping("/avgstreamlikes")
    public String getAverageLikes() {    	
    	Double avgLikes = logRepo.getAverageRating();     
        if (avgLikes != null) {
        	int avgLikesRounded = (int)Math.round(avgLikes);
            return String.valueOf(avgLikesRounded);
        }      
        	//no rating yet        	
            return "0";
    }    
    @GetMapping("/userlikes/{userId}")    
    public String getUserLikes(@PathVariable("userId") String userId) { 
    	Double avgUserLikes = logRepo.getAvgLikesByUserId(userId);
        if (avgUserLikes != null) {
        	int avgUserLikesRounded = (int)Math.round(avgUserLikes);
            return String.valueOf(avgUserLikesRounded);
        }   
        	//the user has no rating yet        	
            return "0";        
    }
    

    
    @PostMapping("/newstreamlog/{sellerId}/{streamId}")
    public ResponseEntity<StreamLog> addNewLogList(@RequestBody StreamLog streamLog, @PathVariable("sellerId") String sellerId, @PathVariable("streamId") String streamId) {
    	Optional<User> user = userRepo.findById(sellerId);
        User seller = user.isPresent() ? user.get() : null;
        
        Optional<Stream> sampleStream = streamRepo.findById(streamId);
        Stream stream = sampleStream.isPresent() ? sampleStream.get() : null;

        if (stream != null) {
            stream.setStatus(StreamStatus.COMPLETED);
            streamRepo.save(stream);
        }
        
    	try {

        	//add other information here (start, end and maxViewers)
        	//get streamstartdate for start, get localdatetime.now for end, get maxviewers from body


        	StreamLog newLog = new StreamLog(streamLog.getNumLikes(), seller, stream, streamLog.getNumViewers(), stream.getSchedule(), LocalDateTime.now());

        	logRepo.save(newLog);
        	return new ResponseEntity<StreamLog>(newLog, HttpStatus.CREATED);
        	
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
     
    
    
}
