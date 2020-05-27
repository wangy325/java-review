package com.wangy.review.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Scanner;

/**
 * copied from core java volume 1 chapter 5 page 195<br>
 * class to analysis a java class via reflect
 *
 * @author wangy
 * @version 1.0
 * @date 2020/4/28 / 16:04
 */
public class ReflectionTest {
    public static void main(String[] args) {
        String name;
        if (args.length > 0) {
            name = args[0];
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Class name(e.g. java.util.ArrayList)");
            name = scanner.next();
        }
        try {
            // print class name and super class name(if != Object)
            Class cls = Class.forName(name);
            Class superCls = cls.getSuperclass();
            String modifiers = Modifier.toString(cls.getModifiers());
            if (modifiers.length() > 0) {
                System.out.print(modifiers + " ");
            }
            System.out.print("class " + cls.getSimpleName());
            if (superCls != null && superCls != Object.class) {
                System.out.print(" extends " + superCls.getSimpleName());
            }
            System.out.print("{\n");
            printConstructors(cls);
            System.out.println();
            printMethods(cls);
            System.out.println();
            printFields(cls);
            System.out.println("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * print all constructors of a class
     *
     * @param cls
     */
    private static void printConstructors(Class cls) {
        Constructor[] constructors = cls.getDeclaredConstructors();
        for (Constructor c : constructors) {
            String cName = c.getDeclaringClass().getSimpleName();
            System.out.print("\t");
            String s = Modifier.toString(c.getModifiers());
            if (s.length() > 0) {
                System.out.print(s + " ");
            }
            System.out.print(cName + " (");
            //  parameters
            Class<?>[] pTypes = c.getParameterTypes();
            for (int i = 0; i < pTypes.length; i++) {
                if (i > 0) {
                    System.out.print(", ");
                }
                System.out.print(pTypes[i].getSimpleName());
            }
            System.out.print(");\n");
        }
    }

    private static void printMethods(Class cls) {
        Method[] methods = cls.getDeclaredMethods();
        for (Method m : methods) {
            Class<?> r = m.getReturnType();
            String mName = m.getName();
            System.out.print("\t");
            String s = Modifier.toString(m.getModifiers());
            if (s.length() > 0) {
                System.out.print(s + " ");
            }
            System.out.print(r.getSimpleName() + " " + mName + "(");
            // print parameters
            Class<?>[] p = m.getParameterTypes();
            for (int i = 0; i < p.length; i++) {
                if (i > 0) {
                    System.out.print(", ");
                }
                System.out.print(p[i].getSimpleName());
            }
            System.out.print(");\n");
        }
    }

    private static void printFields(Class cls) {
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            Class<?> fType = f.getType();
            String fName = f.getName();
            System.out.print("\t");
            String s = Modifier.toString(f.getModifiers());
            if (s.length() > 0) {
                System.out.print(s + " ");
            }
            System.out.print(fType.getName() + " " + fName + ";\n");
        }
    }
}
