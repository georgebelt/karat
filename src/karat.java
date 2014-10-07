
import java.lang.*;
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author GEORGE
 */
public class karat {

    /**
     * @param args the command line arguments
     */
    /**
     * @param args the command line arguments
     */
    public static class BigInteger {

        public int[] digit = new int[20000];
        public int length = 0;

        public BigInteger() {
            Arrays.fill(digit, 0);
            length = 0;
        }

        public BigInteger(String s) {
            Arrays.fill(digit, 0);
            length = s.length();
            for (int i = 0; i < s.length(); i++) {
                digit[length - i - 1] = s.charAt(i) - '0';
            }
        }

        public void add(BigInteger other) {
            length = Math.max(length, other.length);
            for (int i = 0; i < length; i++) {
                digit[i] += other.digit[i];
                if (digit[i] >= 10) {
                    digit[i + 1] += digit[i] / 10;
                    digit[i] = digit[i] % 10;
                }
            }
            if (digit[length] > 0) {
                length++;
            }
        }

        public void sub(BigInteger other) {
            for (int i = 0; i < length; i++) {
                digit[i] -= other.digit[i];
                if (digit[i] < 0) {
                    digit[i+1]--;
                    digit[i] += 10;
                }
            }
            while ((length > 0) && (digit[length - 1] == 0) ) {
                length--;
            }
        }

        public void multiply(BigInteger other) {
            BigInteger[] multiplies = new BigInteger[10];
            multiplies[0] = new BigInteger();
            for (int i = 1; i <= 9; i++) {
                multiplies[i] = new BigInteger();
                multiplies[i].add(multiplies[i - 1]);
                multiplies[i].add(this);
            }
            Arrays.fill(digit, 0);
            length = 0;
            for (int i = 0; i < other.length; i++) {
                BigInteger addition = new BigInteger();
                addition.length = i + multiplies[other.digit[i]].length;
                for (int j = i; j < addition.length; j++) {
                    addition.digit[j] = multiplies[other.digit[i]].digit[j - i];
                }
                this.add(addition);
            }
        }

        public BigInteger subInt(int start, int end) {
            BigInteger ret = new BigInteger();
            if (start <= end) {
                ret.length = end - start + 1;
                for (int i = start; i <= end; i++) {
                    ret.digit[i - start] = digit[i];
                }
            }
            while ((ret.length > 0) && (ret.digit[ret.length - 1] == 0)) {
                ret.length--;
            }
            return ret;
        }

        public void shift(int len) {
            boolean isZero = true;
            for (int i = length - 1; i >= 0; i--) {
                digit[i + len] = digit[i];
                if (digit[i] != 0) {
                    isZero = false;
                }
            }
            for (int i = 0; i < len; i++) {
                digit[i] = 0;
            }
            length += len;
            if (isZero) {
                length = 0;
            }
        }

        public void print() {
            for (int i = length - 1; i >= 0; i--) {
                System.out.print(digit[i]);
            }
            System.out.println();
        }
    }

    public static BigInteger karatsuba(BigInteger bi, BigInteger bi2) {
        if ((bi.length == 0) || (bi2.length == 0)) {
            return new BigInteger("0");
        }
        if ((bi.length == 1) && (bi2.length == 1)) {
            return new BigInteger(Integer.toString(bi.digit[0] * bi2.digit[0]));
        }
        int len = Math.max(bi.length, bi2.length);
        len = len / 2;
        BigInteger bi_low = bi.subInt(0, len - 1);
        BigInteger bi_high = bi.subInt(len, bi.length - 1);
        BigInteger bi2_low = bi2.subInt(0, len - 1);
        BigInteger bi2_high = bi2.subInt(len, bi2.length - 1);
        BigInteger ret0 = karatsuba(bi_low, bi2_low);
        BigInteger ret2 = karatsuba(bi_high, bi2_high);
        bi_low.add(bi_high);
        bi2_low.add(bi2_high);
        BigInteger ret1 = karatsuba(bi_low, bi2_low);
        ret1.sub(ret0);
        ret1.sub(ret2);
        ret2.shift(len * 2);
        ret1.shift(len);
        ret0.add(ret1);
        ret0.add(ret2);
        return ret0;
    }

    public static void main(String[] args) {
        BigInteger bi = new BigInteger("1");
        Scanner stdin = new Scanner(System.in);
        while (stdin.hasNextLine()) {
            bi = karatsuba(bi, new BigInteger(stdin.nextLine()));
            //bi.print();
        }
        bi.print();
    }

}
