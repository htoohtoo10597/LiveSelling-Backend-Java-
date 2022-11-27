package sg.edu.iss.restfulend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sg.edu.iss.restfulend.Helper.OrderStatus;
import sg.edu.iss.restfulend.Model.Orders;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, String> {
	@Query(value ="SELECT count(*) FROM orders o\r\n"		
			+ "INNER JOIN channel_stream cs ON o.channel_id = cs.id\r\n"
			+ "where o.status = 'PENDING' and cs.user_id = ?1", nativeQuery = true)	
	Integer getPendingOrderCountBySeller(String id);

	@Query("SELECT o FROM Orders o WHERE o.channel.user.id = :id AND o.status = :status ORDER BY o.orderDateTime DESC")
	List<Orders> findChannelOrdersByUserIdAndStatus(String id, OrderStatus status);

	@Query("SELECT o FROM Orders o WHERE o.channel.user.id = :id AND o.status <> :status ORDER BY o.orderDateTime DESC")
	List<Orders> findChannelOrdersByUserIdAndNotPending(String id, OrderStatus status);

	@Query("SELECT o FROM Orders o WHERE o.channel.user.id = :id AND o.status = :status AND (o.user.firstName LIKE %:contains% OR o.user.lastName LIKE %:contains%)")
	List<Orders> findClosestPendingOrdersByNameAndUserId(String contains, String id, OrderStatus status);


}
