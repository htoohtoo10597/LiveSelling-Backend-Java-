package sg.edu.iss.restfulend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.iss.restfulend.Helper.ProductCategories;
import sg.edu.iss.restfulend.Model.OrderProduct;
import sg.edu.iss.restfulend.Model.Product;
import sg.edu.iss.restfulend.Repository.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@CrossOrigin(origins= "*")
@RestController
@RequestMapping("/api/product")
public class ProductController {

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

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    @GetMapping("/channelproducts/{userId}")
    public ResponseEntity<List<Product>> getProductsByUserId(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(userRepo.findById(userId).get().getChannel().getProducts(), HttpStatus.OK);
    }

    @GetMapping("/channelproductssortbyname/{userId}/{ascOrDesc}")
    public ResponseEntity<List<Product>> getProductsByUserIdSorted(@PathVariable("userId") String userId, @PathVariable("ascOrDesc") String ascOrDesc) {
        List<Product> list;
        if (ascOrDesc.equals("ascending")) {
            list = userRepo.findById(userId).get().getChannel().getProducts()
                    .stream()
                    .sorted(Comparator.comparing(Product::getName, String::compareToIgnoreCase))
                    .collect(Collectors.toList());
        } else {
            list = userRepo.findById(userId).get().getChannel().getProducts()
                    .stream()
                    .sorted(Comparator.comparing(Product::getName, String::compareToIgnoreCase).reversed())
                    .collect(Collectors.toList());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/channelproductssortbycat/{userId}/{ascOrDesc}")
    public ResponseEntity<List<Product>> getProductsByUserIdCatSorted(@PathVariable("userId") String userId, @PathVariable("ascOrDesc") String ascOrDesc) {
        List<Product> list;
        if (ascOrDesc.equals("ascending")) {
            list = userRepo.findById(userId).get().getChannel().getProducts()
                    .stream()
                    .sorted(Comparator.comparing(Product::getCategory))
                    .collect(Collectors.toList());
        } else {
            list = userRepo.findById(userId).get().getChannel().getProducts()
                    .stream()
                    .sorted(Comparator.comparing(Product::getCategory).reversed())
                    .collect(Collectors.toList());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/channelproductssortbyprice/{userId}/{ascOrDesc}")
    public ResponseEntity<List<Product>> getProductsByUserIdPriceSorted(@PathVariable("userId") String userId, @PathVariable("ascOrDesc") String ascOrDesc) {
        List<Product> list;
        if (ascOrDesc.equals("ascending")) {
            list = userRepo.findById(userId).get().getChannel().getProducts()
                    .stream()
                    .sorted(Comparator.comparing(Product::getPrice))
                    .collect(Collectors.toList());
        } else {
            list = userRepo.findById(userId).get().getChannel().getProducts()
                    .stream()
                    .sorted(Comparator.comparing(Product::getPrice).reversed())
                    .collect(Collectors.toList());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/channelproductssortbyqty/{userId}/{ascOrDesc}")
    public ResponseEntity<List<Product>> getProductsByUserIdQtySorted(@PathVariable("userId") String userId, @PathVariable("ascOrDesc") String ascOrDesc) {
        List<Product> list;
        if (ascOrDesc.equals("ascending")) {
            list = userRepo.findById(userId).get().getChannel().getProducts()
                    .stream()
                    .sorted(Comparator.comparing(Product::getQuantity))
                    .collect(Collectors.toList());
        } else {
            list = userRepo.findById(userId).get().getChannel().getProducts()
                    .stream()
                    .sorted(Comparator.comparing(Product::getQuantity).reversed())
                    .collect(Collectors.toList());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/selectproduct/{prodId}")
    public ResponseEntity<Product> findProductById(@PathVariable("prodId") String prodId) {
        String s = prodId;
        Optional<Product> pData = productRepo.findById(s);
        if (pData.isPresent()) {
            return new ResponseEntity<>(pData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/products/getchannelproducts/{channelId}")
    public ResponseEntity<List<Product>> getAllProductsInStore(@PathVariable("channelId") String channelId) {
        List<Product> channelProducts = productRepo.findAll()
        		.stream()
        		.filter(product -> product.getChannel().getId().equals(channelId))
        		.collect(Collectors.toList());
    	

       return new ResponseEntity<>(channelProducts, HttpStatus.OK);
    }

    @PutMapping("/editproduct/{prodId}")
    public ResponseEntity<Product> editProduct(@PathVariable("prodId") String prodId, @RequestBody Product product) {
        Optional<Product> pData = productRepo.findById(prodId);
        if (pData.isPresent()) {
            Product _product = pData.get();
            String category = product.getCategory().toString();
            ProductCategories cat;
            switch(category) {
                case ("CLOTHING"):
                    cat = ProductCategories.CLOTHING;
                    break;
                case ("FOOD"):
                    cat = ProductCategories.FOOD;
                    break;
                case ("FURNITURES"):
                    cat = ProductCategories.FURNITURES;
                    break;
                case ("APPLIANCES"):
                    cat = ProductCategories.APPLIANCES;
                    break;
                case ("TECHNOLOGY"):
                    cat = ProductCategories.TECHNOLOGY;
                    break;
                case ("BABY"):
                    cat = ProductCategories.BABY;
                    break;
                case ("HEALTH"):
                    cat = ProductCategories.HEALTH;
                    break;
                case ("SPORTS"):
                    cat = ProductCategories.SPORTS;
                    break;
                case ("GROCERIES"):
                    cat = ProductCategories.GROCERIES;
                    break;
                case ("OTHERS"):
                    cat = ProductCategories.OTHERS;
                    break;
                default:
                    cat = ProductCategories.OTHERS;
                    break;
            }
            _product.setName(product.getName());
            _product.setCategory(cat);
            _product.setDescription(product.getDescription());
            _product.setPrice(product.getPrice());
            _product.setQuantity(product.getQuantity());
            return new ResponseEntity<>(productRepo.save(_product), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addtostore/{userId}")
    public ResponseEntity<Product> addToStore(@PathVariable("userId") String userId, @RequestBody Product product) {
    	try {
            List<Product> pList = productRepo.findAll()
                    .stream()
                    .filter(p -> p.getName().equals(product.getName()) && p.getChannel().getUser().getId().equals(userId))
                    .collect(Collectors.toList());

            if (pList.size() < 1) {
                String category = product.getCategory().toString();
                ProductCategories cat;
                switch (category) {
                    case ("CLOTHING"):
                        cat = ProductCategories.CLOTHING;
                        break;
                    case ("FOOD"):
                        cat = ProductCategories.FOOD;
                        break;
                    case ("FURNITURES"):
                        cat = ProductCategories.FURNITURES;
                        break;
                    case ("APPLIANCES"):
                        cat = ProductCategories.APPLIANCES;
                        break;
                    case ("TECHNOLOGY"):
                        cat = ProductCategories.TECHNOLOGY;
                        break;
                    case ("BABY"):
                        cat = ProductCategories.BABY;
                        break;
                    case ("HEALTH"):
                        cat = ProductCategories.HEALTH;
                        break;
                    case ("SPORTS"):
                        cat = ProductCategories.SPORTS;
                        break;
                    case ("GROCERIES"):
                        cat = ProductCategories.GROCERIES;
                        break;
                    case ("OTHERS"):
                        cat = ProductCategories.OTHERS;
                        break;
                    default:
                        cat = ProductCategories.OTHERS;
                        break;
                }
                Product p = productRepo.save(new Product(product.getName(), cat, product.getDescription(),
                        product.getPrice(), product.getQuantity(), userRepo.findById(userId).get().getChannel()));
                return new ResponseEntity<>(p, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/products/{prodId}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("prodId") String prodId) {
        try {
            productRepo.deleteById(prodId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/products")
    public ResponseEntity<HttpStatus> deleteAllProducts() {
        try {
            productRepo.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);

        }
    }

    @PostMapping("/searchproduct/{userId}")
    public ResponseEntity<List<Product>> searchProduct(@RequestBody Product search, @PathVariable("userId") String userId) {
        return new ResponseEntity<>(productRepo.findClosestProductsByNameAndUserId(search.getSearch(), userId), HttpStatus.OK);
    }
    
    @GetMapping("/order/{orderProductId}")
    public ResponseEntity<Product> getProductFromOrderProduct(@PathVariable("orderProductId") String orderProductId) {
    	
    	Optional<OrderProduct> orderProduct = orderProductRepo.findById(orderProductId);
        Product product =  orderProduct.isPresent() ? orderProduct.get().getProduct() : null;
    	
    	return new ResponseEntity<>(product, HttpStatus.OK);
    }
    

}










