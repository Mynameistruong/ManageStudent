/*
 * Sinh viên Biz với điểm Accounting và Marketing.
 */
package model;

import java.util.ArrayList;
import java.util.List;
import view.Utils;


public class BizStudent extends Student {
    private double accounting;
    private double marketing;

    public BizStudent(String studentID, String name, Address address, double accounting, double marketing) {
        super(studentID, name, address);
        this.accounting = accounting;
        this.marketing = marketing;
    }

    public double getAccounting() {
        return accounting;
    }

    public void setAccounting(double accounting) {
        this.accounting = accounting;
    }

    public double getMarketing() {
        return marketing;
    }

    public void setMarketing(double marketing) {
        this.marketing = marketing;
    }

    /**
     * Tính GPA theo trọng số: Accounting*2 + Marketing*1 chia 3.
     */
    @Override
    public final double getGPA() {
        return (2 * accounting + marketing) / 3;
    }

    /**
     * Đếm số môn qua (>=5).
     */
    @Override
    public final int countPassedSubject() {
        int count = 0;
        if (accounting >= 5) count++;
        if (marketing >= 5) count++;
        return count;
    }

    /**
     * Danh sách môn đã qua.
     */
    @Override
    public final List<String> PassedSubject() {
        List<String> pass = new ArrayList<>();
        if (accounting >= 5) pass.add("Accounting: " + getAccounting());
        if (marketing >= 5) pass.add("Marketing: " + getMarketing());
        return pass;
    }

    /**
     * Trung bình các môn đã qua.
     */
    @Override
    public final double getPassedAverage() {
        double sum = 0;
        int count = countPassedSubject();
        if (accounting >= 5) sum += accounting;
        if (marketing >= 5) sum += marketing;
        return count == 0 ? 0 : sum / count;
    }

    /**
     * Hiển thị thông tin sinh viên Biz.
     */
    @Override
    public String toString() {
        return String.format("|%-10s |%-18s |%-50s|%-10s|", getStudentID(), getName(), getAddress(), Utils.checkID(getStudentID()));
    }
}
