package vnskilled.edu.ecom.Controller.Address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vnskilled.edu.ecom.Model.DTO.Address.AddressCityDTO;
import vnskilled.edu.ecom.Model.Response.Address.AddressCityOutPut;
import vnskilled.edu.ecom.Service.Address.AddressCityService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address-city")
public class AddressCityController {
    @Autowired
    private AddressCityService addressCityService;

    @GetMapping
    public ResponseEntity<?> getAllCategories( ) {
        try {

      List<AddressCityDTO> result = addressCityService.getAllAddressCity();

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("message", e.getMessage(),
                            "status", HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }
    }
	@GetMapping("/country/{id}")
	public ResponseEntity<?> getByCountry(
			@PathVariable Long id,
			Model model) {
		try {

	List<AddressCityDTO> result = addressCityService.getByCountry(id);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					Map.of("message", e.getMessage(),
							"status", HttpStatus.INTERNAL_SERVER_ERROR.value())
			);
		}
	}
    @PostMapping
    public String addAddressCity(@RequestBody AddressCityDTO addressCity) {
        try {
            addressCityService.saveAddressCity(addressCity);

        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }
    @PutMapping("/{id}")
    public String updateAddressCity(@PathVariable Long id, @RequestBody AddressCityDTO addressCity) {
        try {
            addressCity.setId(id);
            addressCityService.updateAddressCity(addressCity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "success";
    }
    @DeleteMapping("/{id}")
    public String deleteAddressCity(@PathVariable Long id) {
        try {
            addressCityService.deleteAddressCity(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "AddressCity deleted successfully";
    }

}
