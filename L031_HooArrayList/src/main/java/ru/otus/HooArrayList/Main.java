package ru.otus.HooArrayList;

import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> strList1 = new HooArrayList<>(15);
        List<Integer> intList1 = new HooArrayList<>(5);

        System.out.println("(1) HooArrayList.add: ");
        "abcdefghijklmnopqrstuvwxyz".chars()
                .mapToObj(c -> String.valueOf((char) c))
                .forEach(s -> strList1.add(s));

        System.out.println(String.format("\t%s\n", strList1));

        System.out.println("(2) Collections.addAll: ");
        Collections.addAll(intList1, 1, 10, 9, 7, 5, 3, 2, 6, 8, 4);
        Collections.addAll(strList1, "alpha", "beta", "gamma");

        System.out.println(String.format("\t%s", intList1));
        System.out.println(String.format("\t%s\n", strList1));

        System.out.println("(3) Collections.copy: ");
        List<String> strList2 = new HooArrayList<>();

        strList1.stream().forEach(s -> strList2.add("0"));

        System.out.println(String.format("Source list:\n\t%s", strList2));
        Collections.copy(strList2, strList1);
        System.out.println(String.format("Dest. list:\n\t%s\n", strList2));

        System.out.println("(4) Collections.sort: ");
        Collections.sort(intList1);
        System.out.println(String.format("\t%s", intList1));

        Collections.sort(intList1, (i1, i2) -> i2.compareTo(i1));
        System.out.println(String.format("\t%s", intList1));

        strList1.clear();
        strList1.add("1");

        List<String> list = new HooArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.remove("b");
        System.out.println(list.size());

        List<String> list1 = new HooArrayList<>();
        list1.add("a");
        list1.add("b");
        list1.add("c");
        list1.add("d");
        List<String> list2 = list1.subList(1, 2);
        System.out.println(list2);

    }
}
