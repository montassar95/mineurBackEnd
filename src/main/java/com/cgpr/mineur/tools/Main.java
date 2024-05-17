package com.cgpr.mineur.tools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Locale;

public class Main {

	public static String getArabicMajorityAgeDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        LocalDate d = LocalDate.parse(date, formatter);
        LocalDate c = d.plusYears(18);

        String monthInArabic = getCurrentArabicMonth(c.getMonthValue());

        String day = String.valueOf(c.getDayOfMonth());
        if (c.getDayOfMonth() < 10) {
            day = "0" + day;
        }

        return "تـــاريخ بلـــوغ سن الرشـــد: " + day + " " + monthInArabic + " " + c.getYear();
    }

    public static String getCurrentArabicMonth(int currentMonth) {
        String[] arabicMonths = { "جانفي", "فيفري", "مارس", "أفريل", "ماي", "جوان", "جويلية", "أوت", "سبتمبر", "أكتوبر",
                "نوفمبر", "ديسمبر" };

        int currentMonthIndex = currentMonth  - 1;

        return arabicMonths[currentMonthIndex];
    }

    public static void main(String[] args) {
        String ageCon = getArabicMajorityAgeDate("2022-01-01");
        System.out.println(ageCon);
    }
}
