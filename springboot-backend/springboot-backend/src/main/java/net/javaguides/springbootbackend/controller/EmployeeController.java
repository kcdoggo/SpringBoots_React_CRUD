package net.javaguides.springbootbackend.controller;

import net.javaguides.springbootbackend.exception.ResourceNotFoundException;
import net.javaguides.springbootbackend.model.Employee;
import net.javaguides.springbootbackend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController //json형식으로 data반환
@RequestMapping("/api/v1/")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    //get all employees
    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();

    }
    //프론트에서 정보를 보내는 post 요청을 했으니, postmapping
    @PostMapping("/employees/save")
    //프론트에서 json형식{key:value}으로 data 객체 데이터 주니까, 그걸 employee객체에 매핑하기 위해 @RequestBody줌
    //@RequestBody는 httpbodyrequest(클라이언트에서 백으로 보내는 데이터 json)을 도메인객체에 매핑시켜줌.
    public Employee createEmployee(@RequestBody Employee employee){
      return  employeeRepository.save(employee);
    }


    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> findEmployee(@PathVariable Long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("employee with this id not founded"+id));

        return ResponseEntity.ok(employee);
    }


    @PutMapping("/employees/{id}") //리소스 업데이트할때 put mapping씀
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails){

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("employee with this id not founded"+id));

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmailId(employeeDetails.getEmailId());

        Employee updatedEmployee = employeeRepository.save(employee);

        return ResponseEntity.ok(updatedEmployee);
    }
/**
    @DeleteMapping ("/employees/{id}")
    public Map<String,Boolean> deleteEmployee(@PathVariable Long id){

        Employee employee = EmployeeRepository.findByID(id)
                        .orElseThrows(()->{
                            new ResourceNotFoundException("Employee does not existed with this id :"+id)
                        })

        EmployeeRepository.delete(id);

    }**/


    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable Long id){
        Optional<Employee> employee = employeeRepository.findById(id);
        employeeRepository.delete(employee.get());

        return ResponseEntity.ok(employee.get());
    }


}
