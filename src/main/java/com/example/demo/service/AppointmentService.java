package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Appointment;
import com.example.demo.repository.AppointmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Service method to save record into the db.
     * @param appointment
     */
    public Appointment saveAppointment(Appointment appointment) {
        Appointment savedRecord = appointmentRepository.save(appointment);
        log.info("AppointmentService.class: saved appointment record id: " + savedRecord.getId() + " to DB");
        return savedRecord;
    }

    /**
     * Service method to fetch record for the given id.
     * Throws exception if record is not found.
     * @param id
     */
    public Appointment getAppointmentDetails(int id) {
        return resolveOption(id);
    }

    /**
     * Service method to update record for the given id.
     * Throws exception if record is not found.
     * @param id
     * @param updatedAppointment
     */
    public Appointment updateAppointment(int id, Appointment updatedAppointment) {
        Appointment existingAppointment = resolveOption(id);

        existingAppointment.setPurpose(updatedAppointment.getPurpose());
        existingAppointment.setUserName(updatedAppointment.getUserName());
        existingAppointment.setDateTime(updatedAppointment.getDateTime());
        existingAppointment.setDuration(updatedAppointment.getDuration());
        existingAppointment = appointmentRepository.save(existingAppointment);

        log.info("AppointmentService.class: updated record " + existingAppointment);

        return existingAppointment;
    }

    /**
     * Service method to delete record for the given id from the db.
     * @param id
     */
    public void deleteAppointment(int id) {
        appointmentRepository.deleteById(id);
        log.info("AppointmentService.class: deleted record with id: " + id + " from DB");
    }

    /**
     * Service method to fetch appointment records for the given date range.
     * @param startDate
     * @param endDate
     */
    public List<Appointment> getAppointmentsForDateRange(Date startDate, Date endDate) {
        Optional<List<Appointment>> appointmentList = appointmentRepository.findByDateTimeBetween(startDate, endDate);

        if(appointmentList.isPresent()) {
            List<Appointment> appointments = appointmentList.get();
            if(!appointments.isEmpty()) {
                log.info("AppointmentService.class: number of appointments between " + startDate.toString()
                        + " - " + endDate.toString() + " are: " + appointments.size());
                return appointments;
            }
        }

        log.info("AppointmentService.class: no appointment records found between " + startDate.toString() + " - " + endDate.toString());
        throw new ResourceNotFoundException("No Appointment record found between dates " + startDate
                + " - " + endDate);
    }

    /**
     * Util method in service to fetch the record for given if and resolve the
     * optional value if data is present else throws ResourceNotFoundException
     * custom exception.
     * @param id
     */
    private Appointment resolveOption(int id) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);

        if(appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();
            log.info("AppointmentService.class: fetched record " + appointment + " from DB");
            return appointment;
        }

        log.info("AppointmentService.class: no record found with id: " + id + " in DB");
        throw new ResourceNotFoundException("No Appointment record found for " + id);
    }


}
