package ru.bashirov.fjproj.service.barcode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        final List<String> list = new ArrayList<String>(){{
            add("1");
            add("2");
            add("3");
        }};

        for (Iterator<String> iter = list.iterator(); iter.hasNext();){
            if (iter.next().equals("2")) {
                iter.remove();
            }
        }
        System.out.println(list.size());
    }
}
