package sg.edu.iss.restfulend.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private int rate;
    @ManyToOne
    private ChannelStream channel;
    @ManyToOne
    private User user;

    public Rating(int rate, ChannelStream channel, User user) {
        this.rate = rate;
        this.channel = channel;
        this.user = user;
    }
}
