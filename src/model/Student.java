/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import java.util.Scanner;

/**
 *
 * @author DELL
 */
public abstract class Student {
    private String studentID;
    private String name;
    private Address address;
    public Student(String studentID, String name, Address address){
        this.studentID = studentID;
        this.name = name;
        this.address = address;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
    public abstract double getGPA();
    public abstract int countPassedSubject();
    public abstract List<String> PassedSubject();
    public abstract double getPassedAverage();

    public String display() {
        return String.format("|%-10s |%-18s |%-50s |%-5.2f |", studentID, name, address, getGPA());
    }
}
