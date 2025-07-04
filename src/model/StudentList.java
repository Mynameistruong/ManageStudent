/*
 * Danh sách sinh viên với các chức năng quản lý, lưu trữ, tìm kiếm, thống kê.
 */
package model;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import view.Utils;

public class StudentList {
    private final ArrayList<Student> students = new ArrayList<>();

    /**
     * Đọc danh sách sinh viên từ file.
     */
    public boolean Read(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File " + filename + " does not exist!");
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Student student = parseStudentFromLine(line);
                if (student != null) {
                    addNew(student);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Ghi danh sách sinh viên ra file (không dùng hàm phụ, build chuỗi trực tiếp).
     */
    public boolean save(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Student s : students) {
                StringBuilder sb = new StringBuilder();
                sb.append(s.getStudentID()).append(", ")
                  .append(s.getName()).append(", ")
                  .append(s.getAddress().getCountry()).append(", ")
                  .append(s.getAddress().getCity()).append(", ")
                  .append(s.getAddress().getDistrict()).append(", ")
                  .append(s.getAddress().getStreet()).append(", ");
                if (s instanceof ITStudent it) {
                    sb.append(it.getJavaScore()).append(", ").append(it.getCssScore());
                } else if (s instanceof BizStudent biz) {
                    sb.append(biz.getAccounting()).append(", ").append(biz.getMarketing());
                }
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Thêm sinh viên mới vào danh sách.
     */
    public void addNew(Student s) {
        students.add(s);
    }

    /**
     * Sắp xếp danh sách sinh viên theo tiêu chí.
     */
    public void sort(String criteria) {
        Comparator<Student> comparator = switch (criteria.toLowerCase()) {
            case "name" -> Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER);
            case "country" -> Comparator.comparing(s -> s.getAddress().getCountry(), String.CASE_INSENSITIVE_ORDER);
            case "city" -> Comparator.comparing(s -> s.getAddress().getCity(), String.CASE_INSENSITIVE_ORDER);
            case "district" -> Comparator.comparing(s -> s.getAddress().getDistrict(), String.CASE_INSENSITIVE_ORDER);
            case "street" -> Comparator.comparing(s -> s.getAddress().getStreet(), String.CASE_INSENSITIVE_ORDER);
            case "gpa" -> Comparator.comparingDouble(Student::getGPA);
            default -> null;
        };
        if (comparator != null) {
            students.sort(comparator);
        }
    }

    /**
     * Hiển thị danh sách sinh viên.
     */
    public void display(List<Student> list) {
        if (list.isEmpty()) {
            System.out.println("No students found");
            return;
        }
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.printf("|%-10s |%-18s |%-50s |%-5s |%n", "ID", "Name", "Address", "GPA");
        System.out.println("--------------------------------------------------------------------------------------------");
        for (Student s : list) {
            System.out.println(s.display());
        }
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Total: " + list.size());
    }

    /**
     * Lấy toàn bộ danh sách sinh viên.
     */
    public ArrayList<Student> getAll() {
        return students;
    }

    /**
     * Tìm kiếm sinh viên theo điều kiện.
     */
    public ArrayList<Student> search(Predicate<Student> condition) {
        ArrayList<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (condition.test(s)) {
                result.add(s);
            }
        }
        return result;
    }

    /**
     * Thống kê số lượng sinh viên IT và Biz ở cùng thành phố.
     */
    public void countSameCity() {
        Map<String, List<ITStudent>> itCities = new HashMap<>();
        Map<String, List<BizStudent>> bizCities = new HashMap<>();
        for (Student s : getAll()) {
            String city = s.getAddress().getCity().trim().toUpperCase();
            if (s instanceof ITStudent it) {
                itCities.computeIfAbsent(city, k -> new ArrayList<>()).add(it);
            } else if (s instanceof BizStudent biz) {
                bizCities.computeIfAbsent(city, k -> new ArrayList<>()).add(biz);
            }
        }
        Set<String> commonCities = new HashSet<>(itCities.keySet());
        commonCities.retainAll(bizCities.keySet());
        if (commonCities.isEmpty()) {
            System.out.println("No common cities between IT and Biz students.");
        } else {
            System.out.println("Cities with students from both IT and Biz:");
            for (String city : commonCities) {
                System.out.println("City: " + city);
                System.out.println("-----------------------------------------------------------------------------------------------");
                System.out.printf("|%-10s |%-18s |%-50s|%-10s|%n", "ID", "Name", "Address", "Facilities");
                System.out.println("-----------------------------------------------------------------------------------------------");
                itCities.get(city).forEach(System.out::println);
                bizCities.get(city).forEach(System.out::println);
                System.out.println("-----------------------------------------------------------------------------------------------");
                System.out.println();
                System.out.println("IT: " + itCities.get(city).size());
                System.out.println("BIZ: " + bizCities.get(city).size());
                System.out.println("Total: " + (itCities.get(city).size() + bizCities.get(city).size()));
            }
        }
    }

    /**
     * Xóa sinh viên khỏi danh sách.
     */
    public void remove(Student s) {
        students.remove(s);
    }

    /**
     * Báo cáo tổng hợp thông tin sinh viên.
     */
    public void report() {
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("|%-10s |%-18s |%-10s |%-30s |%-10s |%n", "ID", "Name", "Quantity", "Subjects", "GPA");
        System.out.println("-----------------------------------------------------------------------------------------");
        for (Student s : getAll()) {
            System.out.printf("|%-10s |%-18s |%-10s |%-30s |%-10.2f |%n", s.getStudentID(), s.getName(), s.countPassedSubject(), s.PassedSubject(), s.getPassedAverage());
            System.out.println("-----------------------------------------------------------------------------------------");
        }
    }

    // --- Các hàm tiện ích nội bộ ---
    /**
     * Parse một dòng text thành đối tượng Student.
     */
    private Student parseStudentFromLine(String line) {
        String[] parts = line.split(", ");
        if (parts.length != 8 || Utils.checkID(parts[0]) == null) return null;
        Address addr = new Address(parts[2].trim(), parts[3].trim(), parts[4].trim(), parts[5].trim());
        double score1 = Double.parseDouble(parts[6].trim());
        double score2 = Double.parseDouble(parts[7].trim());
        if (Utils.checkID(parts[0]).equalsIgnoreCase("IT")) {
            return new ITStudent(parts[0].trim(), parts[1].trim(), addr, score1, score2);
        } else {
            return new BizStudent(parts[0].trim(), parts[1].trim(), addr, score1, score2);
        }
    }
}
