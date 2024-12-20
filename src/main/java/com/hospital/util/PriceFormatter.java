package com.hospital.util;

import java.text.NumberFormat;
import java.util.Locale;

public class PriceFormatter {
    private static final NumberFormat formatter = NumberFormat.getInstance(new Locale("en", "BD"));
    
    public static String format(double amount) {
        return formatter.format(amount);
    }
    
    public static double parse(String amount) {
        try {
            return formatter.parse(amount.replace("Tk ", "")).doubleValue();
        } catch (Exception e) {
            return 0.0;
        }
    }
}