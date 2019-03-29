package io.zeleo.jenkins.util;

import java.util.Calendar;

public class DateUtils {
	
	/**
	 * Return the string value for the day of the week.
	 * 
	 * @param dayOfTheWeek The integer of the day of the week.
	 * @return The name of the day, or "None" if an invalid integer is passed in.
	 */
	public static String getDay(Integer dayOfTheWeek) {
		if(dayOfTheWeek == Calendar.SUNDAY) {
			return "Sunday";
		}
		if(dayOfTheWeek == Calendar.MONDAY) {
			return "Monday";
		}
		if(dayOfTheWeek == Calendar.TUESDAY) {
			return "Tuesday";
		}
		if(dayOfTheWeek == Calendar.WEDNESDAY) {
			return "Wednesday";
		}
		if(dayOfTheWeek == Calendar.THURSDAY) {
			return "Thursday";
		}
		if(dayOfTheWeek == Calendar.FRIDAY) {
			return "Friday";
		}
		if(dayOfTheWeek == Calendar.SATURDAY) {
			return "Saturday";
		}
		else {
			return "None";
		}
	}
	
	/**
	 * Return the String value for the month.
	 * 
	 * @param monthOfTheYear The integer for the month.
	 * @return The string value for the month, like "January". Return "None" if 
	 * an invalied value is passed.
	 */
	public static String getMonth(Integer monthOfTheYear) {
		if(monthOfTheYear == Calendar.JANUARY) {
			return "January";
		}
		if(monthOfTheYear == Calendar.FEBRUARY) {
			return "February";
		}
		if(monthOfTheYear == Calendar.MARCH) {
			return "March";
		}
		if(monthOfTheYear == Calendar.APRIL) {
			return "April";
		}
		if(monthOfTheYear == Calendar.MAY) {
			return "May";
		}
		if(monthOfTheYear == Calendar.JUNE) {
			return "June";
		}
		if(monthOfTheYear == Calendar.JULY) {
			return "Juy";
		}
		if(monthOfTheYear == Calendar.AUGUST) {
			return "August";
		}
		if(monthOfTheYear == Calendar.SEPTEMBER) {
			return "September";
		}
		if(monthOfTheYear == Calendar.OCTOBER) {
			return "October";
		}
		if(monthOfTheYear == Calendar.NOVEMBER) {
			return "November";
		}
		if(monthOfTheYear == Calendar.DECEMBER) {
			return "December";
		}
		else {
			return "None";
		}
	}
}
