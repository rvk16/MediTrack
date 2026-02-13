package com.airtribe.meditrack.observer;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.util.DateUtil;
import org.springframework.stereotype.Component;

/**
 * Concrete observer — logs appointment notifications to console.
 * Demonstrates: Observer pattern implementation.
 */
@Component
public class NotificationService implements AppointmentObserver {

    @Override
    public void onAppointmentCreated(Appointment appointment) {
        System.out.println("[NOTIFICATION] New appointment created: "
                + appointment.getPatientName() + " with Dr. " + appointment.getDoctorName()
                + " on " + DateUtil.formatForDisplay(appointment.getAppointmentDateTime()));
    }

    @Override
    public void onAppointmentCancelled(Appointment appointment) {
        System.out.println("[NOTIFICATION] Appointment CANCELLED: "
                + appointment.getPatientName() + " with Dr. " + appointment.getDoctorName()
                + " (ID: " + appointment.getId() + ")");
    }

    @Override
    public void onAppointmentStatusChanged(Appointment appointment) {
        System.out.println("[NOTIFICATION] Appointment status changed to "
                + appointment.getStatus() + " for " + appointment.getPatientName()
                + " (ID: " + appointment.getId() + ")");
    }
}
