package com.cgpr.mineur.config;

import java.time.*;  
import java.util.*;  
public class test  
{    
public static void main(String args[])  
{  
//obtains an instance of LocalDate from a year, month and date  
LocalDate dob = LocalDate.of(1988, 12, 13);  
//obtains the current date from the system clock  
LocalDate curDate = LocalDate.now();  
//calculates the difference betwween two dates  
Period period = Period.between(dob, curDate);  
//prints the differnce in years, months, and days  
System.out.printf("Your age is %d years %d months and %d days.", period.getYears(), period.getMonths(), period.getDays());  
}  
}  