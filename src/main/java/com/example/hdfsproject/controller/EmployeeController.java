package com.example.hdfsproject.controller;

import com.example.hdfsproject.entity.Employee;
import com.example.hdfsproject.repository.DepartmentRepository;
import com.example.hdfsproject.service.EmployeeService;
import com.example.hdfsproject.service.HDFSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private HDFSService hdfsService;

    @GetMapping("/employees")
    public String getEmployees(Model model) {
        List<Employee> employees = employeeService.findAll();
        model.addAttribute("employees", employees);
        model.addAttribute("employeeService", employeeService);
        return "employees";
    }

    @GetMapping("/addEmployee")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("employees", employeeService.findAll());
        return "addEmployee";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(@ModelAttribute Employee employee, @RequestParam("file") MultipartFile file, Model model) {
        try {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                hdfsService.uploadFile(file, fileName);
                employee.setImg(fileName);
            } else if (employee.getImg() == null || employee.getImg().isEmpty()) {
                employee.setImg("default-image.png");
            }
            employeeService.save(employee);
            return "redirect:/employees";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Image upload failed!");
            return "error";
        }
    }

    @GetMapping("/editEmployee/{empno}")
    public String showEditEmployeeForm(@PathVariable Integer empno, Model model) {
        Employee employee = employeeService.findById(empno);
        model.addAttribute("employee", employee);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("employees", employeeService.findAll());
        return "editEmployee";
    }

    @PostMapping("/editEmployee/{empno}")
    public String editEmployee(@PathVariable Integer empno, @ModelAttribute Employee employee, @RequestParam("file") MultipartFile file, Model model) {
        try {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                hdfsService.uploadFile(file, fileName);
                employee.setImg(fileName);
            } else if (employee.getImg() == null || employee.getImg().isEmpty()) {
                employee.setImg("default-image.png");
            }
            employee.setEmpno(empno);
            employeeService.save(employee);
            return "redirect:/employees";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Image upload failed!");
            return "error";
        }
    }

    @GetMapping("/deleteEmployee/{empno}")
    public String deleteEmployee(@PathVariable Integer empno) {
        employeeService.deleteById(empno);
        return "redirect:/employees";
    }
}
