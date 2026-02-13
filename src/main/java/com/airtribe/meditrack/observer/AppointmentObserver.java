package com.airtribe.meditrack.observer;

import com.airtribe.meditrack.entity.Appointment;

/**
 * Observer interface for appointment events.
 * Demonstrates: Observer design pattern.
 */
public interface AppointmentObserver {

    void onAppointmentCreated(Appointment appointment);

    void onAppointmentCancelled(Appointment appointment);

    void onAppointmentStatusChanged(Appointment appointment);
}
