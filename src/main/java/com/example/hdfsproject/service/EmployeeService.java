package com.example.hdfsproject.service;

import com.example.hdfsproject.entity.Employee;
import com.example.hdfsproject.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> findAllWithManagers() {
        return employeeRepository.findAllWithManagers();
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Integer empno) {
        return employeeRepository.findById(empno).orElse(null);
    }

    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    public void deleteById(Integer empno) {
        employeeRepository.deleteById(empno);
    }
}
