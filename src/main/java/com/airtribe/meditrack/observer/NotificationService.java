package com.airtribe.meditrack.observer;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Concrete observer — logs appointment notifications to console.
 * Demonstrates: Observer pattern implementation.
 */
@Component
public class NotificationService implements AppointmentObserver {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Override
    public void onAppointmentCreated(Appointment appointment) {
        log.info("[NOTIFICATION] New appointment created: {} with Dr. {} on {}",
                appointment.getPatientName(), appointment.getDoctorName(),
                DateUtil.formatForDisplay(appointment.getAppointmentDateTime()));
    }

    @Override
    public void onAppointmentCancelled(Appointment appointment) {
        log.info("[NOTIFICATION] Appointment CANCELLED: {} with Dr. {} (ID: {})",
                appointment.getPatientName(), appointment.getDoctorName(), appointment.getId());
    }

    @Override
    public void onAppointmentStatusChanged(Appointment appointment) {
        log.info("[NOTIFICATION] Appointment status changed to {} for {} (ID: {})",
                appointment.getStatus(), appointment.getPatientName(), appointment.getId());
    }
}
