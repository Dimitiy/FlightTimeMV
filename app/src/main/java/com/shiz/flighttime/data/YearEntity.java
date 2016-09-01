package com.shiz.flighttime.data;

/**
 * Created by oldman on 24.08.16.
 */
public class YearEntity {
    public String years;
    public String countHoursInYear;

    public YearEntity(String years, String countHoursInYear) {
        this.years = years;
        this.countHoursInYear = countHoursInYear;
    }

    public String getYears() {
        return years;
    }

    public String getCountHoursInYear() {
        return countHoursInYear;
    }
}
