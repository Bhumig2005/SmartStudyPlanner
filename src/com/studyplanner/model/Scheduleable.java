package com.studyplanner.model;

import java.time.LocalDate;

public interface Scheduleable {
    LocalDate getDeadline();

    boolean isDueToday();
}
