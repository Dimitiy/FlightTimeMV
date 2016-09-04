package com.shiz.flighttime.data;

/**
 * Created by oldman on 24.08.16.
 */
public class YearEntity {
    private String years;
    private String countHoursInYear;

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
