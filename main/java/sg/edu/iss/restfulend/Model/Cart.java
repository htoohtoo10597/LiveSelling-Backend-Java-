package sg.edu.iss.restfulend.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
public class Cart {

    private User user;

    private List<OrderProduct> orderProduct;

    public Cart(User user) {
        this.orderProduct = new ArrayList<>();
        this.user = user;
    }
}
