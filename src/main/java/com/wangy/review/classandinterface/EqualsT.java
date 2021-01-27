package com.wangy.review.classandinterface;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/15 / 14:48
 */
public class EqualsT {

    public static void main(String[] args) {
        Stu a = new Stu("ali",18, "190410");
        StuM b = new StuM("ali",18, "190410","班长");
        System.out.println(a.equals(b));
        System.out.println(b.equals(a));
    }
}

class Stu{
    protected String name;
    protected int age;
    protected String code;

    public Stu(String name, int age, String code) {
        this.name = name;
        this.age = age;
        this.code = code;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Stu)) return false;

        Stu stu = (Stu) o;

        if (age != stu.age) return false;
        if (name != null ? !name.equals(stu.name) : stu.name != null) return false;
        return code != null ? code.equals(stu.code) : stu.code == null;
    }
}

class StuM extends Stu{
    private String resp;

    public StuM(String name, int age, String code, String resp) {
        super(name, age, code);
        this.age = age;
        this.code = code;
        this.name = name;
        this.resp = resp;
    }

    // 导出类无法覆盖equals()方法

}
