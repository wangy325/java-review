package com.wangy.jvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * VM Args: -Xms10m -Xmx10m -XX:+HeapDumpOnOutOfMemoryError
 * @author wangy
 * @version 1.0
 * @date 2022/6/2 / 09:27
 */
public class HeapOOM {

    static class OomObject{ }

    public static void main(String[] args) {
        List<OomObject> list = new ArrayList<>();

        while(true){
            list.add(new OomObject());
        }
    }
}
