package sg.edu.iss.restfulend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sg.edu.iss.restfulend.Model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("SELECT p FROM Product p JOIN p.channel ch JOIN ch.products chp WHERE ch.user.id =:sellerId AND chp.id =:prodId")
    public Product findExistInChannel(String sellerId, String prodId);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:contains% AND p.channel.user.id = :id")
    List<Product> findClosestProductsByNameAndUserId(String contains, String id);

}


