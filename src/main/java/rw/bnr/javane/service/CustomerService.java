package rw.bnr.javane.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.bnr.javane.dto.CustomerDTO;
import rw.bnr.javane.exceptions.DuplicateEmailException;
import rw.bnr.javane.exceptions.ResourceNotFoundException;
import rw.bnr.javane.model.Customer;
import rw.bnr.javane.repository.CustomerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        return convertToDTO(customer);
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        if (customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new DuplicateEmailException("Email is already in use");
        }
        Customer customer = convertToEntity(customerDTO);
        customer.setLastUpdatedTime(LocalDateTime.now());
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        if (!customer.getEmail().equals(customerDTO.getEmail()) && customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new DuplicateEmailException("Email is already in use");
        }
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setMobile(customerDTO.getMobile());
        customer.setDob(customerDTO.getDob());
        customer.setAccount(customerDTO.getAccount());
        customer.setBalance(customerDTO.getBalance());
        customer.setLastUpdatedTime(LocalDateTime.now());
        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDTO(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        customerRepository.delete(customer);
    }

    private CustomerDTO convertToDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .mobile(customer.getMobile())
                .dob(customer.getDob())
                .account(customer.getAccount())
                .balance(customer.getBalance())
                .lastUpdatedTime(customer.getLastUpdatedTime())
                .build();
    }

    private Customer convertToEntity(CustomerDTO customerDTO) {
        return Customer.builder()
                .firstName(customerDTO.getFirstName())
                .lastName(customerDTO.getLastName())
                .email(customerDTO.getEmail())
                .mobile(customerDTO.getMobile())
                .dob(customerDTO.getDob())
                .account(customerDTO.getAccount())
                .balance(customerDTO.getBalance())
                .lastUpdatedTime(LocalDateTime.now())
                .build();
    }
}