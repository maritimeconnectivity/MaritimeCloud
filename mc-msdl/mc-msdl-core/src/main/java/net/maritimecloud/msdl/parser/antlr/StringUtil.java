/*
 * Copyright (c) 2008 Kasper Nielsen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.maritimecloud.msdl.parser.antlr;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Various String utils.
 *
 * @author Kasper Nielsen
 */
public class StringUtil {

    /** An empty string array. */
    public static final String[] EMPTY = new String[0];

    /** The default line separator. */

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /** The Unix line separator. */
    public static final String UNIX_LINE_SEPARATOR = "\n";

    public static String spaces(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    /**
     * Adds the specified count of spaces to the specified string builder.
     *
     * @param sb
     *            the string builder to add to
     * @param count
     *            the number of spaces to add
     * @return the specified string builder
     */
    public static StringBuilder spaces(StringBuilder sb, int count) {
        for (int i = 0; i < count; i++) {
            sb.append(' ');
        }
        return sb;
    }

    /**
     * Removes all leading zeroes from the specified string
     *
     * @param stripFrom
     *            the string to remove leading zeroes from
     * @return a new string (not a substring) stripped of all zeroes
     */
    public static String stripLeadingZeroes(String stripFrom) {
        // Not optimized, we currently do not use it anywhere critically.
        return stripFrom.length() > 0 && stripFrom.charAt(0) == '0' ? stripLeadingZeroes(stripFrom.substring(1))
                : stripFrom;
    }

    /**
     * Counts occurrences of the specified character in the specified string.
     *
     * @param str
     *            the string to count occurrences in
     * @param c
     *            the char to look for
     * @return the number of occurrences of the specified character
     */
    public static int countOccurrences(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    public static String replaceUppercaseWithCharacter(String str, char ch) {
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append(ch);
                c = Character.toLowerCase(c);
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Returns a new string where the first letter of the specified string is capitalized.
     *
     * @param str
     *            the string to capitalize
     * @return the string to capitalize
     */
    public static String capitalizeFirstLetter(String str) {
        if (str.length() > 0) {
            return replaceCharAt(str, 0, Character.toUpperCase(str.charAt(0)));
        }
        return str;
    }

    /**
     * Returns a new string where the first letter of the specified string is capitalized.
     *
     * @param str
     *            the string to capitalize
     * @return the string to capitalize
     */
    public static String uncapitalizeFirstLetter(String str) {
        if (str.length() > 0) {
            return replaceCharAt(str, 0, Character.toLowerCase(str.charAt(0)));
        }
        return str;
    }

    public static String removePlural(String s) {
        return s.length() > 1 && s.endsWith("s") ? s.substring(0, s.length() - 1) : s;
    }

    public static String replaceCharAt(String s, int pos, char c) {
        return s.substring(0, pos) + c + s.substring(pos + 1);
    }

    /**
     * Computes the Levenshtein distance from the two specified char sequences.
     *
     * @param str1
     *            the first char sequence
     * @param str2
     *            the second char sequence
     * @return the Levenshtein distance
     * @throws NullPointerException
     *             if any of the specified strings are null
     */
    public static int computeLevenshteinDistance(String str1, String str2) {
        int l1 = requireNonNull(str1, "str1 is null").length();
        int l2 = requireNonNull(str2, "str2 is null").length();

        if (l1 == 0) { // Quit early if one of the string lengths are 0
            return l2;
        } else if (l2 == 0) {
            return l1;
        }
        // We want to operate on the smallest possible string
        return l1 > l2 ? computeLevenshteinDistance0(str2, l2, str1, l1) : computeLevenshteinDistance0(str1, l1, str2,
                l2);
    }

    public static String findMinimumLevenshteinDistanceErrorString(String word, Iterable<String> options,
            int maximumDistance, String tooManyMatches, int maximumMatches) {
        List<String> candidates = StringUtil.findMinimumLevenshteinDistance(word, options, maximumDistance);
        if (candidates.size() == 1) {
            return ", did you mean: " + candidates.iterator().next();
        } else if (candidates.size() > 1 && candidates.size() <= maximumMatches) {
            return ", did you mean one of: " + candidates;
        } else {
            return tooManyMatches;
        }
    }

    public static List<String> findMinimumLevenshteinDistance(String word, Iterable<String> options, int maximumDistance) {
        int currentMinimumDistance = Integer.MAX_VALUE;
        HashSet<String> tmp = new HashSet<>();
        for (String str : requireNonNull(options, "options is null")) {
            int r = computeLevenshteinDistance(word, str);
            if (r <= maximumDistance && r <= currentMinimumDistance) {
                if (r < currentMinimumDistance) {
                    tmp.clear();
                    currentMinimumDistance = r;
                }
                tmp.add(str);
            }
        }
        ArrayList<String> result = new ArrayList<>(tmp);
        Collections.sort(result);
        return result;
    }

    private static int computeLevenshteinDistance0(String str1, int l1, String str2, int l2) {
        int[] prev = new int[l1 + 1];
        int[] cost = new int[l1 + 1];
        for (int i = 0; i <= l1; i++) {
            prev[i] = i;
        }

        for (int i = 1; i <= l2; i++) {
            cost[0] = i;
            char t_j = str2.charAt(i - 1);
            for (int j = 1; j <= l1; j++) {
                int c = str1.charAt(j - 1) == t_j ? 0 : 1;
                cost[j] = Math.min(Math.min(cost[j - 1] + 1, prev[j] + 1), prev[j - 1] + c);
            }

            int[] tmp = prev;
            prev = cost;
            cost = tmp;
        }
        return prev[l1];
    }
}
