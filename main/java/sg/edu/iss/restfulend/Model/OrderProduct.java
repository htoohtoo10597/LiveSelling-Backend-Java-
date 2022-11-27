package sg.edu.iss.restfulend.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
public class OrderProduct {
    @Id
    @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private int quantity;
    @ManyToOne
    @JsonBackReference
    private Orders order;
    @ManyToOne
    private Product product;

    public OrderProduct(int quantity) {
        this.quantity = quantity;
    }

    public OrderProduct(int quantity, Product product, Orders order) {
        this.quantity = quantity;
        this.product = product;
        this.order = order;
    }
}
