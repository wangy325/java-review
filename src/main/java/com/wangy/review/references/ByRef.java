package com.wangy.review.references;

import java.util.Objects;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/14 / 15:22
 */
public class ByRef {

    static void raiseSalary(Employee2 o) {
        o.raiseSalary(3);
        System.out.println("after salary of o = " + o.getSalary());
    }

    static void swap(Employee2 j, Employee2 k){
        System.out.println("before swap j.name = " + j.getName());
        System.out.println("before swap k.name = " + k.getName());
        Employee2 temp = j;
        j = k;
        k = temp;
        System.out.println("after swap j.name = " + j.getName());
        System.out.println("after swap k.name = " + k.getName());
    }

    public static void main(String[] args) {
        /*Employee2 a = new Employee2("ali", 1200);
        System.out.println("before salary = " + a.getSalary());
        raiseSalary(a);
        System.out.println("after salary of a = " + a.getSalary());*/
        Employee2 a = new Employee2("ali", 1200);
        Employee2 b = new Employee2("bad", 1300);
        swap(a,b);
        System.out.println("after swap a.name = " + a.getName());
        System.out.println("after swap b.name = " + b.getName());
    }
}

class Employee2 {
    private String name;
    private int salary;

    public Employee2(String name, int salary) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee2 employee2 = (Employee2) o;

       /* if (salary != employee2.salary) return false;
        return name != null ? name.equals(employee2.name) : employee2.name == null;*/
       return Objects.equals(name,employee2.name)
           && salary == employee2.salary;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + salary;
        return result;
    }
}