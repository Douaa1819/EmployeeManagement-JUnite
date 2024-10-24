package org.employeemanagement.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.Date;
import java.util.List;


@Entity
public class Employee extends User {

    private String department;
    private String position;
    private String address;
    private String phoneNumber;
    private double salary;
    private double soldConge;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<LeaveRequest> leaveRequests;
    private String socialSecurityNumber;
    @ManyToOne
    private Admin admin;



    public Employee() {
        super();
    }

    public Employee(String name, String email, String password , Date birthDate, String department, String position, String address, String phoneNumber, double salary, double soldConge , String socialSecurityNumber) {
        super(name, email, password, birthDate);
        this.department = department;
        this.position = position;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
        this.soldConge = soldConge;
        this.socialSecurityNumber = socialSecurityNumber;

    }



    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getSoldConge() {
        return soldConge;
    }

    public void setSoldConge(double soldConge) {
        this.soldConge = soldConge;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public Admin getAdmin() {
        return admin;
    }
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", salary=" + salary +
                ", soldConge='" + soldConge + '\'' +
                ", socialSecurityNumber='" + socialSecurityNumber + '\'' +
                '}';
    }
}
