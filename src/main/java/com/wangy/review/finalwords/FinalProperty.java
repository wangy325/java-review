package com.wangy.review.finalwords;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * this demo shows when final used to decorate fields
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/23 / 18:26
 */
public class FinalProperty {

    final int a = 1;

    final String s = "java";

    final int[] a_array = new int[3];

    static final String[] gender = new String[]{"male", "female"};

    void test() throws Exception {
        // illegal
//        a--   // cannot modify final fields

//        s= "c++"; // cannot modify
        // use reflect can make that
        Field value = String.class.getDeclaredField("value");
        value.setAccessible(true);
        char[] chars  = (char[]) value.get(s);
        chars[0] = 'J';

        System.out.println(s);

        // object content can be modified
        a_array[0] = 9;
        a_array[1] = 2;
        a_array[2] = 5;

        Arrays.sort(a_array);

        System.out.println(Arrays.toString(a_array));

        String[] Gender = new String[]{"male", "female"};
        // not allowed! cannot reference final property to a new reference!
        // gender = Gender;
        gender[0] = "MALE";


    }

    public static void main(String[] args) throws Exception {
        new FinalProperty().test();
    }
}
