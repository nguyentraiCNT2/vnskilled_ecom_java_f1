package vnskilled.edu.ecom.Controller.Address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vnskilled.edu.ecom.Exception.ErrorResponse;
import vnskilled.edu.ecom.Model.DTO.Address.AddressCityDTO;
import vnskilled.edu.ecom.Model.DTO.Address.AddressWardsDTO;
import vnskilled.edu.ecom.Model.Response.Address.AddressWardOutPut;
import vnskilled.edu.ecom.Service.Address.AddressWardService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address-ward")
public class AddressWardController {
    @Autowired
    private AddressWardService addressWardService;

    @GetMapping
    public ResponseEntity<?> getAllAddressWard() {
        try {

          List<AddressWardsDTO> result = addressWardService.getAllAddressWards();


            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ErrorResponse.buildErrorResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping
    public String addAddressCounbtry(@RequestBody AddressWardsDTO addressWardsDTO) {
        try {
            addressWardService.saveAddressWards(addressWardsDTO);

        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }
    @PutMapping("/{id}")
    public String updateAddressCity(@PathVariable Long id, @RequestBody AddressWardsDTO addressWardsDTO) {
        try {
            addressWardsDTO.setId(id);
            addressWardService.updateAddressWards(addressWardsDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "success";
    }
    @DeleteMapping("/{id}")
    public String deleteAddressCountry(@PathVariable Long id) {
        try {
            addressWardService.deleteAddressWards(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "AddressCity deleted successfully";
    }


	@GetMapping("/city/{id}")
	public ResponseEntity<?> getByCity(
			@PathVariable Long id,
			Model model) {
		try {

			List<AddressWardsDTO> result = addressWardService.getByCity (id);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					Map.of("message", e.getMessage(),
							"status", HttpStatus.INTERNAL_SERVER_ERROR.value())
			);
		}
	}
}
