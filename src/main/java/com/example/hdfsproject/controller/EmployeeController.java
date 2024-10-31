package com.example.hdfsproject.controller;

import com.example.hdfsproject.entity.Employee;
import com.example.hdfsproject.service.EmployeeService;
import com.example.hdfsproject.service.HDFSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public String getEmployees(Model model) {
        List<Employee> employees;
        try {
            employees = employeeService.findAll();
            model.addAttribute("employees", employees);
        } catch (Exception e) {
            model.addAttribute("error", "Çalışanlar alınamadı: " + e.getMessage());
        }
        return "employees";
    }
}
