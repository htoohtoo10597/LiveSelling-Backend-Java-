package sg.edu.iss.restfulend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(length = 100)
    private String firstName;
    @Column(length = 100)
    private String lastName;
    //Removed @JsonIgnore for registration
    @Column(length = 200)
    @JsonIgnore
    private String address;
    @JsonIgnore
    private String username;
    @JsonIgnore
    private String password;
    
    private Boolean isVerified;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private ChannelStream channel;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rating> reviews;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Orders> ordersHistory;
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<StreamLog> streamlogs;    

    public User(String firstName, String lastName, String address, String username, String password, Boolean isVerified) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.username = username;
        this.password = password;
        this.isVerified = isVerified;
        this.reviews = new ArrayList<>();
        this.ordersHistory = new ArrayList<>();
    }
}
