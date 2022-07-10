package guru.springframework.msscbrewery.web.controller;

import guru.springframework.msscbrewery.web.services.CustomerService;
import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping({"/{customerId}"})
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable UUID customerId) {
        return new ResponseEntity<>(customerService.getCustomerById(customerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity handlePost(@RequestBody CustomerDto customerDto) {
        var savedDto = customerService.saveCustomer(customerDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer" + savedDto.getCustomerId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping({"/{customerId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleUpdate(@PathVariable UUID customerId, @RequestBody CustomerDto customerDto) {
        customerService.updateCustomer(customerId, customerDto);
    }

    @DeleteMapping({"/{customerId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable UUID customerId) {
        customerService.deleteCustomerById(customerId);
    }
}
