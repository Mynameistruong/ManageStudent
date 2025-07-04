/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.*;
import model.*;
import view.Menu;
import view.Utils;

/**
 *
 * @author DELL
 */
public class ManageStudent extends Menu {
    
    static String[] mchoice = {"Add a new student.",
                               "Export Student list including name and GPA.",
                               "Sort and print the list of Students",
                               "Count and print out the number of students in the same city of 2 faculties.",
                               "Update/Delete a student",
                               "Report"};

    StudentList list = new StudentList();
    Scanner sc = new Scanner(System.in);
    List<Student> students;
    
    public ManageStudent(String title, String[] choice){
        super(title, choice);
        list.Read("student.txt");
        students = list.getAll();
    }
    
    public static void main(String[] args) {
        ManageStudent m = new ManageStudent("ManageStudent", mchoice);
        m.run();
    }
    
    @Override
    public void execute(int ch) {
        switch (ch) {
            case 1 -> {
                while(true){
                    System.out.println("Add a new student.");
                    addStudent();
                    list.save("student.txt");
                    if(!Utils.checkAnswer("Do you want to add more student: ", "Selection is invalid!", "Y", "N"))
                        break;
                }
            }
            case 2 -> list.display(students);

            case 3 -> {
                String value = Utils.checkString("Enter criteria (name/country/district/city/street/GPA): ", "^[A-Za-z\\s]+$");
                list.sort(value);
                list.display(students);
            }
            case 4 -> list.countSameCity();

            case 5 ->{
                System.out.println("Update/Delete a student");
                while(true){
                    try {
                        updateOrDeleteStudent();
                    } catch (NoSuchFieldException ex) {
                        System.out.println(ex.getMessage());
                    }
                    if(!Utils.checkAnswer("Do you want to update/delete any more student: ", "Selection is invalid!", "Y", "N"))
                        break;
                }
                list.save("student.txt");
            }
            case 6 -> {
                System.out.println("Report");
                list.report();
            }
        }
    }
    public void addStudent(){
        String id, name, country, city, district, street;
        String check = null;
        List<Student> found;

        do{
            System.out.print("Student ID (ITXXX/BIZXXX)\nEnter '0' to exit: ");
            id = sc.nextLine().trim();
            String ID = id;
            found = list.search((s) -> s.getStudentID().equalsIgnoreCase(ID));
                if(id.equalsIgnoreCase("0"))
                    return;
                else
                    if(Utils.checkID(ID) == null || !found.isEmpty())
                        System.out.println("Invalid or existing ID, re-enter!");
                else 
                     check = Utils.checkID(ID);
            
        } while(id.isEmpty() || !found.isEmpty() || check == null);

        name = Utils.checkString("Enter name: ", "^[A-Za-z\\s]+$");
        country = Utils.checkString("Enter country: ", "^[A-Za-z\\s]+$");
        city = Utils.checkString("Enter city: ", "^[A-Za-z\\s]+$");
        district = Utils.checkString("Enter district: ", "^[A-Za-z\\s]+$");
        street = Utils.checkString("Enter street: ", "^[A-Za-z\\s]+$");
        
        Address addr = new Address(country, city, district, street);

            if(Utils.checkID(id).equalsIgnoreCase("IT")){
                double java = Utils.checkValidDouble("Java score: ", "Invalid! Re-enter", 0.0, 10.0);
                double css = Utils.checkValidDouble("CSS score: ", "Invalid! Re-enter", 0.0, 10.0);
                list.addNew(new ITStudent(id, name, addr, java, css));
            }
            else{
                double a = Utils.checkValidDouble("Accounting score: ", "Invalid! Re-enter", 0.0, 10.0);
                double mkt= Utils.checkValidDouble("Marketing score: ", "Invalid! Re-enter", 0.0, 10.0);
                list.addNew(new BizStudent(id, name, addr, a, mkt));
            }
            System.out.println("Add new student successfully.");
    }
    
    /**
     * Cập nhật hoặc xóa sinh viên theo ID. Hàm này rút gọn, dễ hiểu, có chú thích từng bước.
     */
    public void updateOrDeleteStudent() throws NoSuchFieldException {
        List<Student> found;
        do {
            System.out.print("Student ID (ITXXX/BIZXXX)-(enter '0' to exit): ");
            String idInput = sc.nextLine().trim();
            if (idInput.equals("0")) return; // Thoát nếu nhập 0
            found = list.search(s -> s.getStudentID().equalsIgnoreCase(idInput));
            if (Utils.checkID(idInput) == null || found.isEmpty())
                System.out.println("Invalid or ID not found, re-enter!");
        } while (found == null || found.isEmpty() || Utils.checkID(found.getFirst().getStudentID()) == null);

        Student s = found.getFirst();
        list.display(found);

        // 2. Hỏi người dùng muốn xóa hay cập nhật
        boolean isDelete = Utils.checkAnswer("Update or Delete: ", "Selection is invalid!", "D", "U");
        if (isDelete) {
            list.remove(s);
            System.out.println("Deleted successfully!");
            return;
        }

        // 3. Nếu cập nhật, hiển thị các trường có thể sửa
        String[] fields = getUpdatableFields(s);
        for (int i = 0; i < fields.length; i++) {
            System.out.println((i + 1) + ". " + fields[i]);
        }

        // 4. Lặp cho phép cập nhật nhiều trường
        while (true) {
            System.out.print("Enter your choice (0 to exit): ");
            int ch = Integer.parseInt(sc.nextLine());
            if (ch == 0) break;
            updateFieldByChoice(s, fields[ch - 1]);
        }
        System.out.println("Updated successfully!");
    }

    /**
     * Lấy danh sách các trường có thể cập nhật cho sinh viên (không dùng reflection).
     */
    private String[] getUpdatableFields(Student s) {
        List<String> fields = new ArrayList<>();
        fields.add("name");
        fields.add("country");
        fields.add("city");
        fields.add("district");
        fields.add("street");
        if (s instanceof ITStudent) {
            fields.add("javascore");
            fields.add("cssscore");
        } else if (s instanceof BizStudent) {
            fields.add("accounting");
            fields.add("marketing");
        }
        return fields.toArray(new String[0]);
    }

    /**
     * Cập nhật giá trị cho trường được chọn.
     */
    private void updateFieldByChoice(Student s, String field) {
        // Cập nhật các trường cơ bản
        switch (field.toLowerCase()) {
            case "name" -> s.setName(Utils.checkString("Enter name: ", "^[A-Za-z\\s]+$"));
            case "country", "city", "district", "street" -> {
                // Lấy lại địa chỉ hiện tại
                Address addr = s.getAddress();
                String country = field.equals("country") ? Utils.checkString("Enter country: ", "^[A-Za-z\\s]+$") : addr.getCountry();
                String city = field.equals("city") ? Utils.checkString("Enter city: ", "^[A-Za-z\\s]+$") : addr.getCity();
                String district = field.equals("district") ? Utils.checkString("Enter district: ", "^[A-Za-z\\s]+$") : addr.getDistrict();
                String street = field.equals("street") ? Utils.checkString("Enter street: ", "^[A-Za-z\\s]+$") : addr.getStreet();
                s.setAddress(new Address(country, city, district, street));
            }
            case "javascore" -> {
                if (s instanceof ITStudent it)
                    it.setJavaScore(Utils.checkValidDouble("Java score: ", "Invalid! Re-enter", 0.0, 10.0));
            }
            case "cssscore" -> {
                if (s instanceof ITStudent it)
                    it.setCssScore(Utils.checkValidDouble("CSS score: ", "Invalid! Re-enter", 0.0, 10.0));
            }
            case "accounting" -> {
                if (s instanceof BizStudent biz)
                    biz.setAccounting(Utils.checkValidDouble("Accounting score: ", "Invalid! Re-enter", 0.0, 10.0));
            }
            case "marketing" -> {
                if (s instanceof BizStudent biz)
                    biz.setMarketing(Utils.checkValidDouble("Marketing score: ", "Invalid! Re-enter", 0.0, 10.0));
            }
        }
    }
}
