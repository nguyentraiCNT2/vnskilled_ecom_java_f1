package vnskilled.edu.ecom.Service.User;


import vnskilled.edu.ecom.Entity.User.UserEntity;
import vnskilled.edu.ecom.Model.DTO.User.EmailResetDTO;
import vnskilled.edu.ecom.Model.DTO.User.UserDTO;
import vnskilled.edu.ecom.Model.Request.Auth.RegisterReuest;
import vnskilled.edu.ecom.Model.Request.Auth.ResetPasswordRequest;

import java.util.List;

public interface UserService {
    UserDTO register(RegisterReuest registerReuest);
    UserDTO sendEmail(UserEntity userDTO);
    UserDTO reSendEmail(String email);
    void activeEmail(EmailResetDTO emailResetDTO);
    void generateOtpResetPassword(String email);
    void resetPassword(ResetPasswordRequest resetPasswordRequest);
    UserDTO me();
    void updateUser(UserDTO userDTO);
    void updatePassword(String oldPassword, String newPassword);
    void activateUser(Long id);
    void banUser(Long id);
    void updateRole(Long id, Long role);
    List<UserDTO> getUserByRole(Long roleId);
    List<UserDTO> filterUser(String email, Long roleId, Boolean active, Boolean emailActive);
    UserDTO getUserById(Long id);
}