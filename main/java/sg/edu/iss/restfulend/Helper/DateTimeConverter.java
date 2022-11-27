package sg.edu.iss.restfulend.Helper;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateTimeConverter {

    public LocalDateTime dateTimeConvert(String time) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public LocalDateTime dateTimeConvertClient(String time) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }
}
