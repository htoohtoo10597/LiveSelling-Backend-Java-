package sg.edu.iss.restfulend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.iss.restfulend.Helper.DateTimeConverter;
import sg.edu.iss.restfulend.Helper.StreamStatus;
import sg.edu.iss.restfulend.Model.*;
import sg.edu.iss.restfulend.Repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins= "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepo;
    @Autowired
    ChannelStreamRepository channelRepo;
    @Autowired
    MessageRepository messageRepo;
    @Autowired
    OrderProductRepository orderProductRepo;
    @Autowired
    OrdersRepository ordersRepo;
    @Autowired
    ProductRepository productRepo;
    @Autowired
    RatingRepository ratingRepo;
    @Autowired
    StreamRepository streamRepo;
    @Autowired
    StreamLogRepository logRepo;
    @Autowired
    DateTimeConverter dtc;

    @GetMapping("/streams")
    public List<Stream> getAllStreams() {
        return streamRepo.findAll();
    }
    
    @GetMapping("/notcompletedstreams")
    public ResponseEntity<List<Stream>> getAllNonCompletedStreams() {
        List<Stream> streamsNotCompleted = streamRepo.findAll()
        		.stream()
        		.filter(stream -> !stream.getStatus().equals(StreamStatus.COMPLETED))
        		.collect(Collectors.toList());
        
        return new ResponseEntity<>(streamsNotCompleted, HttpStatus.OK);
    }

    @GetMapping("/userstreams/{userId}")
    public ResponseEntity<List<Stream>> getAllUserStreams(@PathVariable("userId") String userId) {
    	List<Stream> streamsNotCompleted = streamRepo.findAll()
        		.stream()
        		.filter(stream -> (!stream.getStatus().equals(StreamStatus.COMPLETED) && (stream.getChannel().getUser().getId().equals(userId))))
        		.collect(Collectors.toList());
    	
        return new ResponseEntity<>(streamsNotCompleted, HttpStatus.OK);
    }
    
    @GetMapping("/notuserstreams/{userId}/")
    public ResponseEntity<List<Stream>> getAllStreamsNotByUser(@PathVariable("userId") String userId) {
    	List<Stream> notByUser = streamRepo.findAll()
    			.stream()
    			.filter(stream -> !stream.getChannel().getUser().getId().equals(userId))
    			.collect(Collectors.toList());
        return new ResponseEntity<>(notByUser, HttpStatus.OK);
    }

    @GetMapping("/userstreamspending/{userId}")
    public ResponseEntity<List<Stream>> getAllUserStreamsPending(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(streamRepo.getStreamsByUserIdAndStatus(userId, StreamStatus.PENDING), HttpStatus.OK);
    }
    
    @PutMapping("/setstreamtoongoing/{streamId}")
    public ResponseEntity<Stream> setStreamToOngoing(@PathVariable("streamId") String streamId) {
        Stream stream = findStreamById(streamId);
        stream.setStatus(StreamStatus.ONGOING);
        streamRepo.save(stream);
    	
    	return new ResponseEntity<>(stream, HttpStatus.OK);
    }

    @DeleteMapping("/deletestream/{streamId}")
    public ResponseEntity<HttpStatus> deleteStream(@PathVariable("streamId") String streamId) {
        Stream selected = findStreamById(streamId);
        streamRepo.delete(selected);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/channels")
    public List<ChannelStream> getAllChannels() {
        return channelRepo.findAll();
    }
    
    @GetMapping("/channels/notbyuser/{userId}")
    public List<ChannelStream> getAllChannelsNotByUser(@PathVariable("userId") String userId) {
        return channelRepo.findAll()
        		.stream()
        		.filter(channel -> !channel.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @GetMapping("/streams/{streamId}")
    public ResponseEntity<Stream> selectStream(@PathVariable("streamId") String streamId) {
        Stream selected = findStreamById(streamId);
        return selected != null ? new ResponseEntity<>(selected, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/channels/{channelId}")
    public ResponseEntity<ChannelStream> selectChannel(@PathVariable("channelId") String channelId) {
        ChannelStream selected = findChannelById(channelId);
        return selected != null ? new ResponseEntity<>(selected, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/channels/finduser/{userId}")
    public ResponseEntity<ChannelStream> findChannelByUserId(@PathVariable("userId") String userId) {
    	List<ChannelStream> allChannels = 
    	channelRepo.findAll()
    		.stream()
    		.filter(x -> x.getUser().getId().equals(userId))
    		.collect(Collectors.toList());
    	
        ChannelStream selected = allChannels.get(0);
        return selected != null ? new ResponseEntity<>(selected, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    

    @PostMapping("/addstream/{userId}")
    public ResponseEntity<Stream> addNewStream(@RequestBody Stream newStream, @PathVariable("userId") String userId) {
        try {
            Stream stream = streamRepo.save(new Stream(newStream.getTitle(), dtc.dateTimeConvertClient(newStream.getTempSchedule()), findUserById(userId).getChannel(), StreamStatus.PENDING));
            return new ResponseEntity<>(stream, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    //Gab's API for Registration
    @PostMapping("/register/{channelName}/{username}/{password}/{address}")
    public ResponseEntity<User> addNewUser(@RequestBody User newUser, @PathVariable("username") String username, @PathVariable("password") String password , @PathVariable("address") String address, @PathVariable("channelName") String channelName) {
        try {
        	//add check for existing user
        	List<User> userList = userRepo.findAll()
        			.stream()
        			.filter(user -> user.getUsername().equals(username))
        			.collect(Collectors.toList());
        	
        	if(userList.size() < 1) {
        		User user = userRepo.save(new User(newUser.getFirstName(), newUser.getLastName(), address, username, password, newUser.getIsVerified()));
            	channelRepo.save(new ChannelStream(channelName, user));
                return new ResponseEntity<>(user, HttpStatus.CREATED);
        	} else {
        		return new ResponseEntity<>(HttpStatus.CONFLICT);
        	}
        	
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
    
    @PutMapping("/editstream/{streamId}")
    public ResponseEntity<Stream> editStream(@RequestBody Stream stream, @PathVariable("streamId") String streamId) {
        try {
            Stream selectStream = findStreamById(streamId);
            selectStream.setTitle(stream.getTitle());
            streamRepo.save(selectStream);
            selectStream.setSchedule(dtc.dateTimeConvertClient(stream.getTempSchedule()));
            streamRepo.save(selectStream);
            return new ResponseEntity<>(selectStream, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }


    //OTHER METHODS
    public Stream findStreamById(String id) {
        Optional<Stream> stream = streamRepo.findById(id);
        return stream.isPresent() ? stream.get() : null;
    }

    public User findUserById(String id) {
        Optional<User> buyer = userRepo.findById(id);
        return buyer.isPresent() ? buyer.get() : null;
    }

    public Product findProductById(String id) {
        Optional<Product> product = productRepo.findById(id);
        return product.isPresent() ? product.get() : null;
    }

    public OrderProduct findOrderProductById(String id) {
        Optional<OrderProduct> oProduct = orderProductRepo.findById(id);
        return oProduct.isPresent() ? oProduct.get() : null;
    }

    public ChannelStream findChannelById(String id) {
        Optional<ChannelStream> channel = channelRepo.findById(id);
        return channel.isPresent() ? channel.get() : null;
    }
    
    @GetMapping("/upcomingstreams/{userId}")
    public ResponseEntity<List<Stream>> getThreeClosestUserStreamsPending(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(streamRepo.getThreeClosestStreamsByUserId(userId), HttpStatus.OK);
    }
    
    @GetMapping("/upcomingstreamcount/{userId}")
    public String PendingStreamCountByUser(@PathVariable("userId") String userId) {
    	Integer pendingStreamCount = streamRepo.getPendingStreamCountByUser(userId);
        return String.valueOf(pendingStreamCount);
    }
    
    @GetMapping("searchstreams/{searchterm}")
    public ResponseEntity<List<Stream>> getStreamsBySearchTerm(@PathVariable("searchterm") String searchTerm) {
        List<Stream> listOfStreamsAfterSearch = new ArrayList<Stream>();
        
        List<ChannelStream> listOfChannels = productRepo.findAll()
        		.stream()
        		.filter(product -> product.getName().equals(searchTerm.toLowerCase()))
        		.map(product -> product.getChannel())
        		.collect(Collectors.toList());
        
        listOfStreamsAfterSearch = streamRepo.findAll()
        		.stream()
        		.filter(stream -> 
        				stream.getChannel().getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
        				stream.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
        				listOfChannels.contains(stream.getChannel()))
        		.collect(Collectors.toList());
        
        return new ResponseEntity<>(listOfStreamsAfterSearch, HttpStatus.OK);
    }
    
    @PutMapping("/verify/{userId}")
    public ResponseEntity<User> verifyUser(@PathVariable("userId") String userId) {
        try {
            User user = findUserById(userId);
            user.setIsVerified(true);
            userRepo.saveAndFlush(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
    
    @GetMapping("/getverifiedchannels/{channelId}")
    public ResponseEntity<List<ChannelStream>> getAllVerifiedChannels(@PathVariable("channelId") String channelId) {
        List<ChannelStream> verifiedChannels = channelRepo
        		.findAll()
        		.stream()
        		.filter(channel -> channel.getUser().getIsVerified() && !channel.getId().equals(channelId))
        		.collect(Collectors.toList());
        return new ResponseEntity<>(verifiedChannels, HttpStatus.OK);
    }
}
