package vnskilled.edu.ecom.Controller.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnskilled.edu.ecom.Model.DTO.User.UserDTO;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Service.User.UserService;
import vnskilled.edu.ecom.Util.EndpointConstant.AuthApiPaths;

import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public UserDTO me() {
        return userService.me();
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserDTO userDTO) {
        try {
            userService.updateUser(userDTO);
            // Trả về đối tượng userDTO trong ResponseEntity với HTTP status 200 OK
            return ResponseEntity.ok("cap nhat thanh cong");
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về phản hồi lỗi với HTTP status 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", e.getMessage(),"status", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword ) {
        try {
            // Gọi phương thức updatePassword trong UserService
            userService.updatePassword(oldPassword, newPassword);

            // Trả về phản hồi thành công
            return ResponseEntity.ok("Password updated successfully!");

        } catch (Exception e) {
            // Xử lý ngoại lệ chung
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", e.getMessage(),"status", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PostMapping(AuthApiPaths.ACTIVE_USER)
    public ResponseEntity<?> activateUser(@PathVariable Long id) {
        try {
            userService.activateUser(id);
            return ResponseEntity.ok("User activated successfully");
        } catch (RuntimeException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(AuthApiPaths.BAN_USER)
    public ResponseEntity<?> banUser(@PathVariable Long id) {
        try {
            userService.banUser(id);
            return ResponseEntity.ok("User banned successfully");
        } catch (RuntimeException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping ("/update-role")
    public ResponseEntity<?> updateRole(@RequestParam("userid") Long userid, @RequestParam("roleid") Long roleid ) {
        try {
            // Gọi phương thức updatePassword trong UserService
            userService.updateRole(userid, roleid);

            // Trả về phản hồi thành công
            return ResponseEntity.ok(" updated role successfully!");
        } catch (Exception e) {
            // Xử lý ngoại lệ chung
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", e.getMessage(),"status", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    private ResponseEntity<?> buildErrorResponse(String message, HttpStatus status) {
        RequestContext requestContext =RequestContext.get ();
        return  ResponseEntity.status(status).body (Map.of("message",message,
                "status",status.value(),
                "requestUrl",requestContext.getRequestURL (),
                "requestId",requestContext.getRequestId (),
                "timestamp",requestContext.getTimestamp ()));
    }

}

