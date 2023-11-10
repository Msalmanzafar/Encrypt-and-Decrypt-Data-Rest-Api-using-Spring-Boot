package com.encryption.encryption.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.encryption.encryption.Entity.Employee;
import com.encryption.encryption.Exception.ResourceNotFoundException;
import com.encryption.encryption.repository.EmployeeRepository;
import com.encryption.encryption.service.EncryptionService;

@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EncryptionService encryptionService;

	// get all employees
	@GetMapping("/employees")
	public List<Employee> getAllEmployees() {
		// // all data to decrypt frist
		return employeeRepository.findAll();
	}

	// create employee rest api
	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee employee) {
		String encyrptFName = encryptionService.encrypt(employee.getFirstName());
		String encyrptLName = encryptionService.encrypt(employee.getLastName());
		String encyrptEmail = encryptionService.encrypt(employee.getEmailId());

		Employee employee2 = new Employee();
		employee2.setFirstName(encyrptFName);
		employee2.setLastName(encyrptLName);
		employee2.setEmailId(encyrptEmail);

		return employeeRepository.save(employee2);
	}

	// get employee by id rest api
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));
		String decryptFName = encryptionService.decrypt(employee.getFirstName());
		String decryptLName = encryptionService.decrypt(employee.getLastName());
		String decryptEmail = encryptionService.decrypt(employee.getEmailId());

		Employee employee2 = new Employee();
		employee2.setId(employee.getId());
		employee2.setFirstName(decryptFName);
		employee2.setLastName(decryptLName);
		employee2.setEmailId(decryptEmail);

		return ResponseEntity.ok(employee2);
	}

	// update employee rest api

	@PutMapping("/employees/update/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));

		String encyrptFName = encryptionService.encrypt(employeeDetails.getFirstName());
		String encyrptLName = encryptionService.encrypt(employeeDetails.getLastName());
		String encyrptEmail = encryptionService.encrypt(employeeDetails.getEmailId());

		employee.setFirstName(encyrptFName);
		employee.setLastName(encyrptLName);
		employee.setEmailId(encyrptEmail);

		Employee updatedEmployee = employeeRepository.save(employee);
		return ResponseEntity.ok(updatedEmployee);
	}

	// delete employee rest api
	@DeleteMapping("/employees/delete/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));

		employeeRepository.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
