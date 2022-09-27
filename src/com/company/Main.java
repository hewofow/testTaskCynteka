package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Main {
    private static Set<String> set1;
    private static Set<String> set2;
    private static final Map<String, String> map = new HashMap<>();
    private static final Map<String, String> dictionary = new HashMap<>();

    public static void main(String[] args) {
        readInput();
        readDictionary();

        for (String s : set2.size() > set1.size() ? set2 : set1) {
            map.put(s, pullResembling(s,
                    set2.size() > set1.size() ? set1 : set2,
                    (a, b) -> dictionary.getOrDefault(a, "").equals(b) ||
                            dictionary.getOrDefault(b, "").equals(a)));
        }

        writeOutput();
    }

    private static void readInput() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/com/company/input.txt"))) {
            int x = Integer.parseInt(br.readLine());
            set1 = br.lines().limit(x).collect(Collectors.toSet());

            int y = Integer.parseInt(br.readLine());
            set2 = br.lines().limit(y).collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readDictionary() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/com/company/dictionary.txt"))) {
            String s;
            while ((s = br.readLine()) != null) {
                dictionary.put(s.substring(0, s.indexOf(":")), s.substring(s.indexOf(":") + 2));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeOutput() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/com/company/output.txt"))) {
            map.forEach((key, value) -> {
                try {
                    bw.write(key + ":" + value + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String pullResembling(String key, Set<String> set,
                                         BiPredicate<? super String, ? super String> biPredicate) {
        Optional<String> match = set.stream()
                .filter(s -> biPredicate.test(s, key) || compareStrings(s, key) > 20 ||
                        Arrays.stream(s.split("\\s+"))
                                .anyMatch(x -> compareStrings(x, key) > 20))
                .findFirst();
        match.ifPresent(set::remove);
        return match.orElse("?");
    }

    private static int compareStrings(String s1, String s2) {
        int result = 0;
        int weight;
        int longer = Math.max(s1.length(), s2.length());
        int shorter = Math.min(s1.length(), s2.length());
        String longerString = longer == s1.length() ? s1.toLowerCase() : s2.toLowerCase();
        String shorterString = shorter == s2.length() ? s2.toLowerCase() : s1.toLowerCase();

        for (int j = 0; j < shorter; j++) {
            weight = j;
            for (int i = 0; i < longer; i++) {
                if (shorterString.charAt(weight) == longerString.charAt(i)) {
                    weight++;
                } else {
                    weight = 0;
                }

                if (weight > result) {
                    result = weight;
                }

                if (weight == shorter) {
                    break;
                }
            }
        }

        return 100 * (result > 2 ? result : 0) / Math.min(s1.length(), s2.length());
    }
}
