package com.musicdiary.projections;

public interface MonthlyCountProjection {

    Integer getYear();

    Integer getMonth();

    Long getCount ();
}
