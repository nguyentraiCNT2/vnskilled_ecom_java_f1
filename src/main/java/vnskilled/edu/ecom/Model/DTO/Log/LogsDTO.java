package vnskilled.edu.ecom.Model.DTO.Log;

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
public class LogsDTO {
    private Long id;
    private UserDTO userId;
    private String method;
    private String apiUrl;
    private String status;
    private String errorMessage;
    private String ipAddress;
    private String userAgent;
    private String hostName;
    private Timestamp createAt;
    private Timestamp updateAt;

}
