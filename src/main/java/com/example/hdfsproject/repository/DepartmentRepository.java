package com.example.hdfsproject.repository;

import com.example.hdfsproject.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {}