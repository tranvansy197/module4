package com.codegym.controller;

import com.codegym.dto.customer.CustomerDto;
import com.codegym.model.customer.Customer;
import com.codegym.model.customer.CustomerType;
import com.codegym.model.facility.Facility;
import com.codegym.service.customer.ICustomerService;
import com.codegym.service.customer.ICustomerTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Objects;

@RequestMapping("/customer")
@Controller
public class CustomerController {
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ICustomerTypeService customerTypeService;

    @GetMapping("")
    public String showList(Model model, @RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") String email, @RequestParam(defaultValue = "") String customerTypeName, @PageableDefault(size = 5) Pageable pageable) {
        Page<Customer> customerPage;
        if(Objects.equals(customerTypeName, "")){
            customerPage = customerService.findByNameAndEmail(name,email,pageable);
        }else {
            customerPage = customerService.findByNameAndEmailAndCustomerType(name, email, customerTypeName, pageable);
        }
        model.addAttribute("customerPage", customerPage);
        model.addAttribute("customerTypeList", customerTypeService.findAll());
        model.addAttribute("customerTypeName",customerTypeName);
        return "customer/list";
    }

    @GetMapping("/create")
    public String showAdd(Model model){
        model.addAttribute("customerDto",new CustomerDto());
        model.addAttribute("customerTypeList", customerTypeService.findAll());
        return "customer/create";
    }

    @PostMapping("/update")
    public String update(@Validated CustomerDto customerDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        new CustomerDto().validate(customerDto,bindingResult);
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto,customer);
        Map<String,String> errorMap = customerService.getError(customerDto);
        if(errorMap.get("errorIdCard")!= null) {
            bindingResult.rejectValue("idCard", "idCard", errorMap.get("errorIdCard"));
        }
        if(errorMap.get("errorPhoneNumber")!= null) {
            bindingResult.rejectValue("phoneNumber", "phoneNumber", errorMap.get("errorPhoneNumber"));
        }
        if(errorMap.get("errorEmail")!= null) {
            bindingResult.rejectValue("email","email",errorMap.get("errorEmail"));}
        if(bindingResult.hasErrors()){
            model.addAttribute("customerTypeList", customerTypeService.findAll());
            return "customer/create";
        }

        if(errorMap.isEmpty()){
            customerService.update(customer);
            redirectAttributes.addFlashAttribute("mess","Chỉnh sửa thành công");
        }
        return "redirect:/customer";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(@PathVariable int id, Model model){
        Customer customer = customerService.findById(id).orElseThrow(()-> new IllegalArgumentException("not found"));
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer,customerDto);
        model.addAttribute("customer",customerDto);
        model.addAttribute("customerTypeList", customerTypeService.findAll());
        return "customer/update";
    }

    @PostMapping("/create")
    public String add(@Validated CustomerDto customerDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        new CustomerDto().validate(customerDto,bindingResult);
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto,customer);
        Map<String,String> errorMap = customerService.getError(customerDto);
        if(errorMap.get("errorIdCard")!= null) {
            bindingResult.rejectValue("idCard", "idCard", errorMap.get("errorIdCard"));
        }
        if(errorMap.get("errorPhoneNumber")!= null) {
            bindingResult.rejectValue("phoneNumber", "phoneNumber", errorMap.get("errorPhoneNumber"));
        }
        if(errorMap.get("errorEmail")!= null) {
        bindingResult.rejectValue("email","email",errorMap.get("errorEmail"));}
        if(bindingResult.hasErrors()){
            model.addAttribute("customerTypeList", customerTypeService.findAll());
            return "customer/create";
        }

        if(errorMap.isEmpty()){
                customerService.add(customer);
            redirectAttributes.addFlashAttribute("mess","Thêm mới thành công");
        }
        return "redirect:/customer";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam int id,RedirectAttributes redirectAttributes){
        customerService.remove(id);
        redirectAttributes.addFlashAttribute("mess", "Xóa thành công");
        return "redirect:/customer";
    }
}
