package vnskilled.edu.ecom.Controller.AdminController.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vnskilled.edu.ecom.Model.DTO.User.UserDTO;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Service.User.UserService;
import vnskilled.edu.ecom.Util.EndpointConstant.AuthApiPaths;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminUserController {
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

    @PostMapping("/active/{id}")
    public String activateUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.activateUser(id);
            redirectAttributes.addFlashAttribute("message", "User activated successfully");
             return "redirect:/admin/user/list";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/user/list";
        }
    }

    @PostMapping("/ban/{id}")
    public String banUser(@PathVariable Long id,  RedirectAttributes redirectAttributes) {
        try {
            userService.banUser(id);
            redirectAttributes.addFlashAttribute("message", "User band successfully");
            return "redirect:/admin/user/list";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/user/list";
        }
    }

    @GetMapping("/update-role/{id}")
    public String updateRole(@PathVariable Long id,  Model redirectAttributes) {
        try {
            UserDTO user = userService.getUserById(id);
            redirectAttributes.addAttribute("user", user);
            return "User/updateRole";
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            return "User/updateRole"; // Trang hiện tại cần render lại khi có lỗi
        }
    }

    @PostMapping("/update-role")
    public String updateRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId, Model model) {
        try {
            // Gọi phương thức updateRole trong UserService để cập nhật quyền hạn
            userService.updateRole(userId, roleId);
            // Thêm thông báo thành công vào Model để hiển thị trong giao diện
            model.addAttribute("message", "Cập nhật quyền hạn thành công!");
            return "redirect:/admin/user/list"; // Đường dẫn điều hướng sau khi cập nhật thành công
        } catch (Exception e) {
            // Thêm thông báo lỗi vào Model để hiển thị trong giao diện
            model.addAttribute("error",  e.getMessage());
            return "User/updateRole"; // Trang hiện tại cần render lại khi có lỗi
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


    @GetMapping("/user/list")
    public String filterUser(
            @RequestParam(name = "email",required = false) String email,
            @RequestParam(name = "roleId", required = false) Long roleId,
            @RequestParam(name = "active",required = false)  Boolean active,
            @RequestParam(name = "emailActive",required = false) Boolean emailActive,
            Model model) {
        try {
            Long role = ( roleId == null )? 2L : roleId;
            List<UserDTO> users = new ArrayList<>();
            if (emailActive == null && active == null && email == null || email == "") {
                 users = userService.getUserByRole(role);
            }
            else {
                users = userService.filterUser(email, roleId, active, emailActive);
            }
            model.addAttribute("users", users);
            return "User/list"; // Trả về template hiển thị danh sách người dùng
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "User/list"; // Trả về trang lỗi nếu có ngoại lệ
        }
    }

    @GetMapping("/user/detail/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        try {
            UserDTO user = userService.getUserById(id);
            model.addAttribute("user", user);
            return "User/userDetail"; // Return the name of the HTML template for user detail
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "User/userDetail";// Return an error page
        }
    }

}

