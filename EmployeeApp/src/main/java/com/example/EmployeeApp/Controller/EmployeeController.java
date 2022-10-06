package com.example.EmployeeApp.Controller;

import com.example.EmployeeApp.Models.Employee;
import com.example.EmployeeApp.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //Display list of employees
    @GetMapping("/")
    public String viewHomePage(Model model) {
        return findPaginated(1, "firstName", "asc", model); //1 is default
    }

    @GetMapping("/ShowNewEmployeeForm") //Method handler to show employee form when button is clicked
    public String ShowNewEmployeeForm(Model model) {
        //Create model attribute to bind form data
        Employee employee = new Employee();
        model.addAttribute("employee", employee); //Key employee, Value Employee
        return "new_employee";
    }

    @PostMapping("/saveEmployee") //Method handler for saving employee
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        //save employee to database
        employeeService.saveEmployee(employee);
        return "redirect:/"; //to homepage
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {
        //get employee from the service
        Employee employee = employeeService.getEmployeeById(id);

        //set employee as a model attribute to pre-populate the form
        model.addAttribute("employee", employee);
        return "update_employee";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable(value = "id") long id) {
        //call delete employee method
        this.employeeService.deleteEmployeeById(id);
        return "redirect:/";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo, @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) { //Created method handler for pagination
        int pageSize = 5;

        Page<Employee> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Employee> listEmployees = page.getContent(); //Retrieve list of paginated employees

        model.addAttribute("currentPage", pageNo); //Passing page number
        model.addAttribute("totalPages", page.getTotalPages()); //Passing total pages
        model.addAttribute("totalItems", page.getTotalElements()); //Passing total elements/items

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listEmployees", listEmployees); //Passing list of employees
        return "index";
    }

}
