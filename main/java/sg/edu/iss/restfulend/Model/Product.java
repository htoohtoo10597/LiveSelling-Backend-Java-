package sg.edu.iss.restfulend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import sg.edu.iss.restfulend.Helper.ProductCategories;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(length = 100)
    private String name;
    @Enumerated(EnumType.STRING)
    private ProductCategories category;
    @Column(length = 500)
    private String description;
    private double price;
    private int quantity;
    @Transient
    private String search;

    @ManyToOne
    private ChannelStream channel;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderProduct> orderProduct;

    public Product(String name, ProductCategories category, String description, double price, int quantity, ChannelStream channel) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.channel = channel;
        this.orderProduct = new ArrayList<>();
    }

}
