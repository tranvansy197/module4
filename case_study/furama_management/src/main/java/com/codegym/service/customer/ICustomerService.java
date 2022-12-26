package com.codegym.service.customer;

import com.codegym.model.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomerService {
    Page<Customer> findByNameAndEmailAndCustomerType(String name, String email, String customerTypeName, Pageable pageable);

    void add(Customer customer);
}
