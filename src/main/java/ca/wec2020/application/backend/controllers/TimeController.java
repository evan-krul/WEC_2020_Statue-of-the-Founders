package ca.wec2020.application.backend.controllers;

import java.time.LocalDate;

// Singleton to manage date

public final class TimeController {

    private static TimeController INSTANCE;
    private LocalDate date;
    private TimeController() {
        date = LocalDate.now();
    }

    public static TimeController getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new TimeController();
        }
        return INSTANCE;
    }

    public LocalDate getDate() {
        return date;
    }

    public void incrementDay() {
        date = date.plusDays(1);
    }
}
