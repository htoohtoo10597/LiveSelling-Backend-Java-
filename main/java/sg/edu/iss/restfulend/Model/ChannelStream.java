package sg.edu.iss.restfulend.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class ChannelStream {
    @Id
    @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String name;
    @OneToOne
    private User user;
    
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rating> ratings;
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    /*
    @JsonIdentityInfo(
    		  generator = ObjectIdGenerators.PropertyGenerator.class, 
    		  property = "id")
    		  */
    @JsonIgnore
    private List<Orders> orders;
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Stream> streams;

    public ChannelStream(String name, User user) {
        this.name = name;
        this.products = new ArrayList<>();
        this.ratings = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.streams = new ArrayList<>();
        this.user = user;
    }
}
