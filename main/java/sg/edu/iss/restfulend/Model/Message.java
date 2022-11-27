package sg.edu.iss.restfulend.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String message;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDateTime timeStamp;
    
    @ManyToOne
    private StreamLog log;

    public Message(String message, LocalDateTime timeStamp, StreamLog log) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.log = log;
    }
}
