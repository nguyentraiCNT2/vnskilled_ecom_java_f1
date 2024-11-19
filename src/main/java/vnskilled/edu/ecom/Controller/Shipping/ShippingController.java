package vnskilled.edu.ecom.Controller.Shipping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnskilled.edu.ecom.Model.DTO.Shipping.ShippingDTO;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Service.Shipping.ShippingService;
import vnskilled.edu.ecom.Util.EndpointConstant.ShippingApiPaths;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ShippingApiPaths.BASE_PATH)
public class ShippingController {
    @Autowired
    private ShippingService shippingService;


    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<ShippingDTO> Shippings = shippingService.getAll();
            return ResponseEntity.ok(Shippings);
        } catch (Exception e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(ShippingApiPaths.CREATE_PATHS)
    public ResponseEntity<?> create(@RequestBody ShippingDTO shipping) {
        try {
            shippingService.save(shipping);
            return ResponseEntity.ok(" shipping created successfully");
        } catch (Exception e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<?> buildErrorResponse(String message, HttpStatus status) {
        RequestContext requestContext = RequestContext.get();
        return ResponseEntity.status(status).body(Map.of("message", message,
                "status", status.value(),
                "requestUrl", requestContext.getRequestURL(),
                "requestId", requestContext.getRequestId(),
                "timestamp", requestContext.getTimestamp()));
    }

}
