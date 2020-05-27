package com.wangy.review.finalwords;

/**
 * simple example of final parameters
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/14 / 17:45
 */
public class FinalParam {

    static void with(final Employee e) {
        raiseSalary(e);
        System.out.println("salary of e = " + e.getSalary());
    }

    static void raiseSalary(final Employee e) {
        e.raiseSalary(3);
    }

    static void swap(final Employee j, final Employee k) {
        Employee temp = j;
        // k = temp; // not allowed
        // j = k; // not allowed
    }

    static void g(final int i) {
        // i++; // not allowed
    }

    public static void main(String[] args) {
        Employee x = new Employee("ali", 1000);
        with(x);
        System.out.println("salary of x = " + x.getSalary());
    }
}

class Employee {
    private String name;
    private int salary;

    public Employee(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }

    void raiseSalary(int multiple) {
        this.salary = salary * multiple;
    }
}
