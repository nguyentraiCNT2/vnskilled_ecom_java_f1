package vnskilled.edu.ecom.Controller.Address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnskilled.edu.ecom.Exception.ErrorResponse;
import vnskilled.edu.ecom.Model.DTO.Address.UserAddressDTO;
import vnskilled.edu.ecom.Service.Address.UserAddressService;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user-address")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;
    @GetMapping
    public ResponseEntity<?> getAllUserAddress() {
        try {
            List<UserAddressDTO> userAddressList = userAddressService.getByUserId();
            return ResponseEntity.status(HttpStatus.OK).body(userAddressList);
        } catch (Exception e) {
            return ErrorResponse.buildErrorResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping
    public ResponseEntity<?> addUserAddress(@RequestBody UserAddressDTO userAddressDTO) {
        try {
            userAddressService.addUserAddress(userAddressDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm địa chỉ thành công");
        } catch (Exception e) {
            return ErrorResponse.buildErrorResponse(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserAddress(@PathVariable Long id, @RequestBody UserAddressDTO userAddressDTO) {
        try {
            userAddressDTO.setId(id);
            userAddressService.updateUserAddress(userAddressDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Cập nhật địa chỉ thành công");
        } catch (Exception e) {
            return ErrorResponse.buildErrorResponse(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserAddress(@PathVariable Long id) {
        try {
            userAddressService.deleteUserAddress(id);
            return ResponseEntity.status(HttpStatus.OK).body("Xóa địa chỉ thành công");
        } catch (Exception e) {
            return ErrorResponse.buildErrorResponse(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}

