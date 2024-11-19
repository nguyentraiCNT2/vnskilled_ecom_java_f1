package vnskilled.edu.ecom.Controller.Payment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnskilled.edu.ecom.Model.DTO.Payment.PaymentDTO;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Service.Payment.PaymentService;
import vnskilled.edu.ecom.Util.EndpointConstant.PaymentApiPaths;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(PaymentApiPaths.BASE_PATH)
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    private ResponseEntity<?> buildErrorResponse(String message, HttpStatus status) {
        RequestContext requestContext = RequestContext.get();
        return ResponseEntity.status(status).body(Map.of("message", message,
                "status", status.value(),
                "requestUrl", requestContext.getRequestURL(),
                "requestId", requestContext.getRequestId(),
                "timestamp", requestContext.getTimestamp()));
    }

    @GetMapping
    public ResponseEntity<?> getAllPayments() {
        try {
            List<PaymentDTO> payments = paymentService.getAll();
            return ResponseEntity.status(HttpStatus.OK).body(payments);
        } catch (Exception e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Lấy thanh toán theo id
    @GetMapping(PaymentApiPaths.DETAILS_PATHS)
    public ResponseEntity<?> getPaymentById(@PathVariable Long id) {
        try {
            PaymentDTO payment = paymentService.getById(id);
            return ResponseEntity.status(HttpStatus.OK).body(payment);
        } catch (Exception e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Thêm thanh toán
    @PostMapping(PaymentApiPaths.CREATE_PATHS)
    public ResponseEntity<?> addPayment(@RequestBody PaymentDTO payment) {
        try {
            paymentService.save(payment);
            return ResponseEntity.ok("create successfully");
        } catch (Exception e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cập nhật thanh toán
    @PutMapping(PaymentApiPaths.EDIT_PATHS)
    public ResponseEntity<?> updatePayment(@PathVariable Long id, @RequestBody PaymentDTO payment) {
        try {
            paymentService.update(id, payment);
            return ResponseEntity.ok("update successfully");
        } catch (Exception e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Xóa thanh toán
    @DeleteMapping(PaymentApiPaths.DELETE_PATHS)
    public ResponseEntity<?> deletePayment(@PathVariable Long id) {
        try {
            paymentService.delete(id);
            return ResponseEntity.ok("delete successfully");
        } catch (Exception e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
