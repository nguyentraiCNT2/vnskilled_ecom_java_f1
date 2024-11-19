package vnskilled.edu.ecom.Controller.Address;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vnskilled.edu.ecom.Model.DTO.Address.AddressCountryDTO;
import vnskilled.edu.ecom.Model.Response.Address.AddressCountryOutPut;
import vnskilled.edu.ecom.Service.Address.AddressCountryService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address-country")
public class AddressCountryController {
    @Autowired
    private AddressCountryService addressCountryService;

    @GetMapping
    public ResponseEntity<?> getAllAddressCountry() {
        try {
            List<AddressCountryDTO> result =addressCountryService.getAllAddressCountry();
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("message", e.getMessage(),
                            "status", HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }
    }

    @PostMapping
    public String addAddressCounbtry(@RequestBody AddressCountryDTO addressCountryDTO) {
        try {
            addressCountryService.saveAddressCountry(addressCountryDTO);

        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }
    @PutMapping("/{id}")
    public String updateAddressCity(@PathVariable Long id, @RequestBody AddressCountryDTO addressCountryDTO) {
        try {
            addressCountryDTO.setId(id);
            addressCountryService.updateAddressCountry(addressCountryDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "success";
    }
    @DeleteMapping("/{id}")
    public String deleteAddressCountry(@PathVariable Long id) {
        try {
            addressCountryService.deleteAddressCountry(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "AddressCity deleted successfully";
    }

}
