/*
 * Sinh viên IT với điểm Java và CSS.
 */
package model;

import java.util.ArrayList;
import java.util.List;
import view.Utils;

/**
 *
 * @author DELL
 */
public class ITStudent extends Student {
    private double javaScore;
    private double cssScore;

    public ITStudent(String studentID, String name, Address address, double javaScore, double cssScore) {
        super(studentID, name, address);
        this.javaScore = javaScore;
        this.cssScore = cssScore;
    }

    public double getJavaScore() {
        return javaScore;
    }

    public void setJavaScore(double javaScore) {
        this.javaScore = javaScore;
    }

    public double getCssScore() {
        return cssScore;
    }

    public void setCssScore(double cssScore) {
        this.cssScore = cssScore;
    }

    /**
     * Tính GPA theo trọng số: Java*3 + CSS*1 chia 4.
     */
    @Override
    public final double getGPA() {
        return (3 * javaScore + cssScore) / 4;
    }

    /**
     * Đếm số môn qua (>=5).
     */
    @Override
    public final int countPassedSubject() {
        int count = 0;
        if (javaScore >= 5) count++;
        if (cssScore >= 5) count++;
        return count;
    }

    /**
     * Danh sách môn đã qua.
     */
    @Override
    public final List<String> PassedSubject() {
        List<String> pass = new ArrayList<>();
        if (javaScore >= 5) pass.add("Java: " + getJavaScore());
        if (cssScore >= 5) pass.add("CSS: " + getCssScore());
        return pass;
    }

    /**
     * Trung bình các môn đã qua.
     */
    @Override
    public final double getPassedAverage() {
        double sum = 0;
        int count = countPassedSubject();
        if (javaScore >= 5) sum += javaScore;
        if (cssScore >= 5) sum += cssScore;
        return count == 0 ? 0 : sum / count;
    }

    /**
     * Hiển thị thông tin sinh viên IT.
     */
    @Override
    public String toString() {
        return String.format("|%-10s |%-18s |%-50s|%-10s|", getStudentID(), getName(), getAddress(), Utils.checkID(getStudentID()));
    }
}
