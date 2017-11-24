package net.orange.yap.util.lang;


import java.text.NumberFormat;
import java.util.BitSet;
import java.util.Collection;


/**
 * @author sjsmit
 * @since Oct 18, 2011
 */
public class StringUtils {

    private static final NumberFormat decimalFormat = NumberFormat.getInstance();

    static {
        decimalFormat.setMinimumFractionDigits(3);
        decimalFormat.setMaximumFractionDigits(3);
    }

    public static String abbreviate(String s, int maxWidth) {
        return org.apache.commons.lang3.StringUtils.abbreviate(s, maxWidth);
    }

    public static String format(double d) {
        return decimalFormat.format(d);
    }

    public static String format(float f) {
        return decimalFormat.format(f);
    }

    public static String toBinaryString(BitSet bits, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            boolean value = bits.get(i);
            if (value) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }
        return sb.toString();
    }

    public static String toBinaryString(boolean[] arr) {
        if (arr == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (boolean b : arr) {
            sb.append(b ? '1' : '0');
        }
        return sb.toString();
    }

    public static String toBinaryString(Collection list) {
        if (list == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Object o : list) {
            if (o instanceof Number) {
                int b = ((Number) o).intValue();
                sb.append(b == 0 ? '0' : '1');
            } else if (o instanceof Boolean) {
                Boolean b = (Boolean) o;
                sb.append(b ? '1' : '0');
            } else {
                sb.append(String.valueOf(o));
            }
        }
        return sb.toString();
    }


    public static String toString(Object[] a) {
        StringBuilder sb = new StringBuilder("[");
        boolean empty = true;
        for (Object s : a) {
            empty = false;
            String sv = s instanceof Number && !(s instanceof Integer) ?
                    decimalFormat.format(s) : String.valueOf(s);
            sb.append(sv).append(" ");
        }
        if (!empty) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }


    public static char[] toBinary(int value, int parity) {
        final char[] results = new char[parity];
        final char[] chars = Integer.toBinaryString(value).toCharArray();
        System.arraycopy(chars, 0, results, parity - chars.length, chars.length);
        return results;
    }
}


