package vnskilled.edu.ecom.Model.DTO.Notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Model.DTO.User.UserDTO;

import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificantionDTO {
    private Long id;
    private UserDTO userId;
    private String title;
    private String content;
    private Timestamp createAt;
    private Timestamp recelveAt;

}
