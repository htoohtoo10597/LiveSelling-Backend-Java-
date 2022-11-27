package sg.edu.iss.restfulend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sg.edu.iss.restfulend.Helper.DateTimeConverter;
import sg.edu.iss.restfulend.Helper.OrderStatus;
import sg.edu.iss.restfulend.Helper.ProductCategories;
import sg.edu.iss.restfulend.Helper.StreamStatus;
import sg.edu.iss.restfulend.Model.*;
import sg.edu.iss.restfulend.Repository.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Random;
import java.util.logging.Logger;

@SpringBootApplication
public class RestfulEndApplication {

	@Autowired
	UserRepository UserRepo;
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

	private static final Logger LOGGER = Logger.getLogger(RestfulEndApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(RestfulEndApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
			clearDatabase();
			populateDatabase();
		};
	}

	public void clearDatabase() {
		UserRepo.deleteAll();
		channelRepo.deleteAll();
		messageRepo.deleteAll();
		orderProductRepo.deleteAll();
		ordersRepo.deleteAll();
		productRepo.deleteAll();
		ratingRepo.deleteAll();
		streamRepo.deleteAll();
		logRepo.deleteAll();

		LOGGER.info("----------------------------clearing database");
	}

	public void populateDatabase() {

		LOGGER.info("----------------------------Populating database.." );		
		
		User s1 = new User("Amanda", "Chong", "Upper Paya Lebar West", "amandachong", "amandachong", true);
		UserRepo.save(s1);
		ChannelStream cs1 = new ChannelStream("Better Living", s1);
		channelRepo.save(cs1);
		Product pcs1_1 = new Product("Aloe Vera Gel", ProductCategories.HEALTH, "Healing gel from South Korea. Used by BTS.", 50.00, 100, cs1);
		productRepo.save(pcs1_1);
		Product pcs1_2 = new Product("Collagen Face Mask", ProductCategories.HEALTH, "Get glowing skin in 1 week! Number 1 product in Busan.", 25.00, 100, cs1);
		productRepo.save(pcs1_2);
		Product pcs1_3 = new Product("Anti-aging body lotion", ProductCategories.HEALTH, "Get snow white skin. Number 1 product in Busan.", 25.00, 100, cs1);
		productRepo.save(pcs1_3);
		Stream ps1 = new Stream("Beauty Bazaar", LocalDateTime.now(), cs1, StreamStatus.ONGOING);
		streamRepo.save(ps1);

		User s2 = new User("James", "Seah", "Punggol West Avenue 2", "jamesseah", "jamesseah", true);
		UserRepo.save(s2);
		ChannelStream cs2 = new ChannelStream("HighTech Gadgets", s2);
		channelRepo.save(cs2);
		Product pcs2_1 = new Product("iPhone 22", ProductCategories.TECHNOLOGY, "Latest iPhone from the future", 1500.00, 2, cs2);
		productRepo.save(pcs2_1);
		Product pcs2_2 = new Product("MacBook Pro 15 M6", ProductCategories.TECHNOLOGY, "Latest MacBook from the future", 3500.00, 3, cs2);
		productRepo.save(pcs2_2);
		Product pcs2_3 = new Product("Playstation 5", ProductCategories.TECHNOLOGY, "Next-gen console from Sony", 799.00, 5, cs2);
		productRepo.save(pcs2_3);
		Stream ps2 = new Stream("Tech Bazaar", dtc.dateTimeConvert("04/09/2022 13:30"), cs2, StreamStatus.PENDING);
		streamRepo.save(ps2);

		User b1 = new User("Tom", "Tan", "Chua Chu Kang Avenue 2", "tomtan", "tomtan", false);
		UserRepo.save(b1);
		ChannelStream csb1 = new ChannelStream("tomtan", b1);
		channelRepo.save(csb1);

		User b2 = new User("Melinda", "Mak", "Woodlands Avenue 2", "melmak", "melmak", false);
		UserRepo.save(b2);
		ChannelStream csb2 = new ChannelStream("melmak", b2);
		channelRepo.save(csb2);

		User b3 = new User("Joe", "Lim", "Upper Changi Road 2", "joelim", "joelim", false);
		UserRepo.save(b3);
		ChannelStream csb3 = new ChannelStream("joelim", b3);
		channelRepo.save(csb3);

		User b4 = new User("Jess", "Park", "Coorperation Drive Avenue 2", "jesspark", "jesspark", false);
		UserRepo.save(b4);
		ChannelStream csb4 = new ChannelStream("jesspark", b4);
		channelRepo.save(csb4);

		User b5 = new User("Peter", "Pang", "Sentosa Cove 2", "peterpang", "peterpang", false);
		UserRepo.save(b5);
		ChannelStream csb5 = new ChannelStream("peterpang", b5);
		

		channelRepo.save(csb5);
		Orders o2 = new Orders(b5, LocalDateTime.now(), OrderStatus.PENDING, cs2, null);
		ordersRepo.save(o2);
		OrderProduct op2 = new OrderProduct(1,pcs2_1, o2);
		orderProductRepo.save(op2);

		User b6 = new User("Sarah", "Sim", "Sims Avenue 2", "sarahsim", "sarahsim", false);
		UserRepo.save(b6);
		ChannelStream csb6 = new ChannelStream("sarahsim", b6);
		channelRepo.save(csb6);
		Orders o1 = new Orders(b6, LocalDateTime.now(), OrderStatus.PENDING, cs2, null);
		ordersRepo.save(o1);
		OrderProduct op1 = new OrderProduct(1,pcs2_2, o1);
		orderProductRepo.save(op1);

		
		//populate rating for dashboard
		Rating rs1 = new Rating(2, cs1, s1); //Amanda's rating
		Rating rs2 = new Rating(3, cs2, s2); //James's rating
		Rating rs3 = new Rating(4, cs2, s2); //James's another rating
		Rating rs4 = new Rating(1, cs2, s2); //James's another rating
		
		ratingRepo.save(rs1);ratingRepo.save(rs2);ratingRepo.save(rs3);ratingRepo.save(rs4);

		
		//populate orders to plot djshbord charts
		LOGGER.info("---------------------------This will take a while. Please wait" );
		
		User buyer1 = new User("Jack", "Lee", "Chua Chu Kang Avenue 2", "jacklee", "jacklee", false); //buyer
		User buyer2 = new User("Jack", "Mann", "Chua Chu Kang Avenue 8", "jackmann", "jackmann", false); //buyer
		UserRepo.save(buyer1);UserRepo.save(buyer2);
		LocalDateTime startDateTime = LocalDateTime.of(2022,Month.JULY,1,1,30,40,50000); //2022-07-08T01:30:40.000050
		LocalDateTime now = LocalDateTime.now();
		Integer daysBetween = (int)Duration.between(startDateTime, now).toDays();
	
		for (int i =0; i <daysBetween; i++) {	
			Random r = new Random();
			int low = 1;
			int high = 20;
			int noOfOrders = r.nextInt(high-low) + low;			
			LocalDateTime datetime = startDateTime.plusDays(i);
			for (int j =0; j <noOfOrders; j++) {
				LocalDateTime datetime1 = datetime;
				LocalDateTime datetime2 = datetime1.plusHours(6);
				LocalDateTime datetime3 = datetime1.plusHours(12);
				LocalDateTime datetime4 = datetime1.plusHours(18);
				Orders order1 = new Orders(buyer1, datetime1, OrderStatus.CONFIRMED, cs2, null);
				Orders order2 = new Orders(buyer1, datetime2, OrderStatus.CONFIRMED, cs2, null);
				Orders order3 = new Orders(buyer1, datetime2, OrderStatus.CONFIRMED, cs2, null);
				Orders order4 = new Orders(buyer1, datetime2, OrderStatus.CONFIRMED, cs2, null);
				Orders order5 = new Orders(buyer1, datetime3, OrderStatus.CONFIRMED, cs2, null);
				Orders order6 = new Orders(buyer1, datetime3, OrderStatus.CONFIRMED, cs2, null);
				Orders order7 = new Orders(buyer1, datetime3, OrderStatus.CONFIRMED, cs2, null);
				Orders order8 = new Orders(buyer1, datetime3, OrderStatus.CONFIRMED, cs2, null);
				Orders order9 = new Orders(buyer1, datetime3, OrderStatus.CONFIRMED, cs2, null);
				Orders order10 = new Orders(buyer1, datetime4, OrderStatus.CONFIRMED, cs2, null);
				Orders order11 = new Orders(buyer1, datetime4, OrderStatus.CONFIRMED, cs2, null);
				ordersRepo.save(order1);	ordersRepo.save(order2);
				ordersRepo.save(order3);ordersRepo.save(order4);
				ordersRepo.save(order5);ordersRepo.save(order6);
				ordersRepo.save(order7);ordersRepo.save(order8);
				ordersRepo.save(order9);ordersRepo.save(order10);	
				ordersRepo.save(order11);
			}			
		}
		//populate streamlog for (initilal dashboard display)		
		
		User u1 = new User("Jane", "Chong", "Upper Paya Lebar West", "janechong", "janechong", true);
		UserRepo.save(u1);
		ChannelStream csu1 = new ChannelStream("Better Living", u1);
		channelRepo.save(csu1);
		Product pcs1_1a = new Product("Aloe Vera Gel", ProductCategories.HEALTH, "Healing gel from South Korea. Used by BTS.", 50.00, 100, csu1);
		productRepo.save(pcs1_1a);
		Product pcs1_2b = new Product("Collagen Face Mask", ProductCategories.HEALTH, "Get glowing skin in 1 week! Number 1 product in Busan.", 25.00, 100, csu1);
		productRepo.save(pcs1_2b);
		Product pcs1_3c = new Product("Anti-aging body lotion", ProductCategories.HEALTH, "Get snow white skin. Number 1 product in Busan.", 25.00, 100, csu1);
		productRepo.save(pcs1_3c);
		Stream psu1 = new Stream("Beauty Bazaar", LocalDateTime.now(), csu1, StreamStatus.ONGOING);
		streamRepo.save(psu1);

		User u2 = new User("John", "Seah", "Punggol West Avenue 2", "johnseah", "johnseah", true);
		UserRepo.save(u2);
		ChannelStream csu2 = new ChannelStream("HighTech Gadgets", u2);
		channelRepo.save(csu2);
		Product pcs2_1a = new Product("iPhone 22", ProductCategories.TECHNOLOGY, "Latest iPhone from the future", 1500.00, 2, csu2);
		productRepo.save(pcs2_1a);
		Product pcs2_2b = new Product("MacBook Pro 15 M6", ProductCategories.TECHNOLOGY, "Latest MacBook from the future", 3500.00, 3, csu2);
		productRepo.save(pcs2_2b);
		Product pcs2_3c = new Product("Playstation 5", ProductCategories.TECHNOLOGY, "Next-gen console from Sony", 799.00, 5, csu2);
		productRepo.save(pcs2_3c);
		Stream psu2 = new Stream("Tech Bazaar", dtc.dateTimeConvert("04/09/2022 13:30"), csu2, StreamStatus.PENDING);
		streamRepo.save(psu2);		

		Stream str2 = new Stream("John's Tech Fest2", LocalDateTime.now().minusHours(2),csu2, StreamStatus.COMPLETED); //John' stream 
		Stream str1 = new Stream("Jane's beauty Fest2", LocalDateTime.now().minusHours(2), csu1, StreamStatus.COMPLETED); //jane's stream
		streamRepo.save(str1);
		streamRepo.save(str2);
	
		StreamLog streamlog1 = new StreamLog(45,u2, str2, 10, LocalDateTime.now().minusHours(2), LocalDateTime.now()); //Jame's streamlog 
		StreamLog streamlog2 = new StreamLog(35, u1, str1, 30, LocalDateTime.now().minusHours(2), LocalDateTime.now()); //Jame's streamlog 
		
		logRepo.save(streamlog1);	logRepo.save(streamlog2);
		
		
		//	Populate some pending orders
		LocalDateTime currTime = LocalDateTime.now();
		
		Orders order12 = new Orders(buyer1, currTime, OrderStatus.PENDING, cs2, null);
		OrderProduct orderprod1 = new OrderProduct(10, pcs2_1, order12);
		order12.getOrderProduct().add(orderprod1);
		ordersRepo.saveAndFlush(order12);
		
		Orders order13 = new Orders(buyer1, currTime, OrderStatus.PENDING, cs2, null);
		OrderProduct orderprod2 = new OrderProduct(10, pcs2_1, order13);
		order13.getOrderProduct().add(orderprod2);
		ordersRepo.saveAndFlush(order13);

		
		LOGGER.info("----------------------------Database populated successfully!");	        
    }
}
