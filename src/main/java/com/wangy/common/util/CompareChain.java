package com.wangy.common.util;

import com.google.common.collect.ComparisonChain;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.function.Function;

/**
 * Java 8 函数式接口{@link Comparator}配合{@link Function}可以简化传统比较器的编写：
 * <pre>
 *      &#64;Override
 *      public int compareTo(Person p) {
 *          int cmp = this.lastname.compareTo(p.lastname);
 *          if (cmp != 0){
 *              return cmp;
 *          }
 *          cmp = this.firstname.compareTo(p.firstname);
 *          if (cmp != 0){
 *              return cmp;
 *          }
 *          return Long.compare(this.zipcode, p.zipcode);
 *      }
 * </pre>
 * 可以简化为：
 * <pre>
 *      &#64;Override
 *      public int compareTo(Person p){
 *          return Comparator
 *                     .comparing((Function&lt;Person, String&gt;) p1 -> p1.lastname)
 *                     .thenComparing(person -> person.firstname)
 *                     .thenComparing(person -> person.zipcode)
 *                     .compare(this, p);
 *      }
 * </pre>
 * <p>
 * 此外，guava工具类{@link ComparisonChain}提供了更加易于理解和编写(Java 8的函数式接口不易理解和编写)
 * 的优化比较器的方法：
 * <pre>
 *     &#64;Override
 *     public int compareTo(Person p){
 *         return ComparisonChain.start()
 *                     .compare(this.lastname, p.lastname)
 *                     .compare(this.firstname, p.firstname)
 *                     .compare(this.zipcode, p.zipcode)
 *                     .result();
 *     }
 * </pre>
 * <p>
 * 这种“链式分格”的方法调用在许多优秀的框架中都能看到
 *
 * @author wangy
 * @date 01.26/0026 16:00
 */
public class CompareChain {


    @Data
    @AllArgsConstructor
    static class Person implements Comparable<Person> {
        String lastname;
        String firstname;
        long zipcode;


        /**
         * usage : a.compareTo(b)
         *
         * @param p object to compared
         * @return <pre>
         * a > b ==> positive value <br>
         * a = b ==> 0 <br>
         * a < b ==> negative value
         * </pre>
         */
        @Override
        public int compareTo(Person p) {

            // the old way can be replaced by guava ComparisonChain
            /*return ComparisonChain.start()
                    .compare(this.lastname, p.lastname)
                    .compare(this.firstname, p.firstname)
                    .compare(this.zipcode, p.zipcode)
                    .result();*/

            // The java 8 also offer Functional Interface to do this
            // But the usage of Function<T,U> is little bit complicated to understand
            return Comparator
                    .comparing((Function<Person, String>) person -> person.lastname)
                    .thenComparing(person -> person.firstname)
                    .thenComparing(person -> person.zipcode)
                    .compare(this, p);
        }
    }


    public static void main(String[] args) {
        System.out.println("a".compareTo("b")); // -1

        Person p1 = new Person("steve", "jobs", 520000);
        Person p2 = new Person("steve", "jobs", 510000);
        Person p3 = new Person("steve", "jobs", 530000);
        Person p4 = new Person("steve", "jobs", 520000);

        Assert.isTrue(p1.compareTo(p2) > 0, String.valueOf(p1.compareTo(p2)));
        Assert.isTrue(p1.compareTo(p3) < 0, String.valueOf(p1.compareTo(p3)));
        Assert.isTrue(p1.compareTo(p4) == 0, String.valueOf(p1.compareTo(p4)));
    }
}
