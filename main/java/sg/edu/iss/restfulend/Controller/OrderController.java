package sg.edu.iss.restfulend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.iss.restfulend.Helper.OrderStatus;
import sg.edu.iss.restfulend.Model.OrderProduct;
import sg.edu.iss.restfulend.Model.Orders;
import sg.edu.iss.restfulend.Repository.ChannelStreamRepository;
import sg.edu.iss.restfulend.Repository.OrderProductRepository;
import sg.edu.iss.restfulend.Repository.OrdersRepository;
import sg.edu.iss.restfulend.Repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;



@CrossOrigin
@RestController
@RequestMapping("/api/orders")
public class OrderController {
	
    @Autowired
    OrdersRepository ordersRepo;
    @Autowired
    OrderProductRepository orderProdRepo;
    @Autowired
    ChannelStreamRepository channelRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    UserController userCont;
  
    @GetMapping("/pendingorders/{userId}")
    public String getPendingOrdersCount(@PathVariable("userId") String userId) {    	
    	Integer pendingOrdersCount = ordersRepo.getPendingOrderCountBySeller(userId);    
            return String.valueOf(pendingOrdersCount);
    }
    
    @GetMapping("/purchases/{userId}")
    public ResponseEntity<List<Orders>> getUserPurchases(@PathVariable("userId") String userId) {
        List<Orders> allPurchases = ordersRepo.findAll()
        .stream()
        .filter(order -> order.getUser().getId().equals(userId))
        .sorted((o1, o2) -> o1.getOrderDateTime().compareTo(o2.getOrderDateTime()))
        .collect(Collectors.toList());
        
        return new ResponseEntity<>(allPurchases, HttpStatus.OK);
    }
    
    @GetMapping("/channelorders/{channelId}")
    public ResponseEntity<List<Orders>> getChannelOrders(@PathVariable("channelId") String channelId) {
        List<Orders> allOrders = ordersRepo.findAll()
        .stream()
        .filter(order -> order.getChannel().getId().equals(channelId) && (order.getStatus() == OrderStatus.PENDING))
        .sorted((o1, o2) -> o1.getOrderDateTime().compareTo(o2.getOrderDateTime()))
        .collect(Collectors.toList());
        
        return new ResponseEntity<>(allOrders, HttpStatus.OK);
    }

    @GetMapping("/channelordersuserpending/{userId}")
    public ResponseEntity<List<Orders>> getChannelOrdersPendingByUserId(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(ordersRepo.findChannelOrdersByUserIdAndStatus(userId, OrderStatus.PENDING), HttpStatus.OK);
    }

    @GetMapping("/channelordersuserhistory/{userId}")
    public ResponseEntity<List<Orders>> getChannelOrdersHistory(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(ordersRepo.findChannelOrdersByUserIdAndNotPending(userId, OrderStatus.PENDING), HttpStatus.OK);
    }

    @GetMapping("/getorder/{orderId}")
    public ResponseEntity<Orders> getOrderById(@PathVariable("orderId") String orderId) {
        try {
            return new ResponseEntity<>(ordersRepo.findById(orderId).get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateorderstatus/{orderId}/{status}")
    public ResponseEntity<Orders> updateOrderStatus(@PathVariable("orderId") String orderId,
                                                    @PathVariable("status") String status) {
        try{
            Orders updateOrder = ordersRepo.findById(orderId).get();
            updateOrder.setStatus((status.equals("CONFIRMED")) ? OrderStatus.CONFIRMED : OrderStatus.CANCELLED);
            ordersRepo.save(updateOrder);
            return new ResponseEntity<>(HttpStatus.OK); 
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
    
    @GetMapping("/products/{orderId}")
    public ResponseEntity<List<OrderProduct>> getProductsInOrder(@PathVariable("orderId") String orderId){
    	List<OrderProduct> productsInOrder = orderProdRepo.findAll()
    			.stream()
    			.filter(orderProduct -> orderProduct.getOrder().getId().equals(orderId))
    			.collect(Collectors.toList());
    	
    	return new ResponseEntity<>(productsInOrder, HttpStatus.OK);
    }

    @PostMapping("/searchorder/{userId}")
    public ResponseEntity<List<Orders>> searchOrder(@RequestBody Orders search, @PathVariable("userId") String userId) {
        return new ResponseEntity<>(ordersRepo.findClosestPendingOrdersByNameAndUserId(search.getSearch(), userId, OrderStatus.PENDING), HttpStatus.OK);
    }

    @PostMapping("/addorder/{userId}/{channelId}/{streamId}")
    public ResponseEntity<Orders> addNewOrder(@RequestBody Orders order, @PathVariable("userId") String userId, @PathVariable("channelId") String channelId, @PathVariable("streamId") String streamId) {
    	Orders newOrder = new Orders();
    	newOrder.setChannel(userCont.findChannelById(channelId));
    	newOrder.setUser(userCont.findUserById(userId));
    	newOrder.setStream(userCont.findStreamById(streamId));
    	newOrder.setStatus(OrderStatus.PENDING);
    	newOrder.setOrderDateTime(LocalDateTime.now());
    	
    	ordersRepo.saveAndFlush(newOrder);
    	
    	List<OrderProduct> listOfOrderProd = newOrder.getOrderProduct();
    	
    	for (OrderProduct orderProd : order.getOrderProduct()) {
    		OrderProduct newOrderProd = new OrderProduct(orderProd.getQuantity(), 
    				userCont.findProductById(orderProd.getProduct().getId()), //might need to find the ID
    				newOrder);
    		orderProdRepo.saveAndFlush(newOrderProd);
    	}

        
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    
    }

}
