package com.encryption.encryption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.encryption.encryption.Entity.Employee;


@Repository

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
}
