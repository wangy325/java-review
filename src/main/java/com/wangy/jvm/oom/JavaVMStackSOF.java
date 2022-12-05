package com.wangy.jvm.oom;

/**
 * VM Args: +Xss 160k
 * @author wangy
 * @version 1.0
 * @date 2022/6/2 / 11:09
 */
public class JavaVMStackSOF {

    private int stackLen = 1;

    public void stackLeak(){
        int len ;
        if (stackLen > 1000) return;
        len = stackLen++;

        stackLeak();
    }

    public static void main(String[] args) {
        JavaVMStackSOF sof =  new JavaVMStackSOF();
        try {
            sof.stackLeak();
        }catch (Throwable t) {
            System.out.println("stack length: " + sof.stackLen);
            throw t;
        }
    }
}
