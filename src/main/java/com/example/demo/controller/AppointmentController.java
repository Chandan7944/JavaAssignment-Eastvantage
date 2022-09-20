package com.example.demo.controller;

import com.example.demo.model.Appointment;
import com.example.demo.model.DateRangePojo;
import com.example.demo.service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Post mapping to save the appointment record to the db.
     * Validation is in-place to validate the request body to check for NotBlank, NotNull, Min constraints.
     * @param appointment
     * @return HttpStatus.CREATED on saving the record to db
     */
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Appointment saveAppointment(@RequestBody @Valid Appointment appointment) {
        return appointmentService.saveAppointment(appointment);
    }

    /**
     * Get mapping to fetch the appointment record based on id.
     * Validation is in-place to check if path variable is positive.
     * If the record is not found returns 404 Not found.
     * @param id
     */
    @GetMapping("/{id}")
    public Appointment getAppointmentDetails(
            @PathVariable @Positive(message = "Controller: Path variable id must be greater than zero.") int id
    ) {
        return appointmentService.getAppointmentDetails(id);
    }

    /**
     * Put mapping to update the record based on id.
     * Validation is in-place for both path variable and request body.
     * If the record is not found returns 404 Not found.
     * @param id
     * @param appointment
     */
    @PutMapping("/{id}")
    public Appointment updateAppointment(
            @PathVariable @Positive(message = "Controller: Path variable id must be greater than zero.") int id,
            @RequestBody @Valid Appointment appointment
    ) {
        return appointmentService.updateAppointment(id, appointment);
    }

    /**
     * Delete mapping to delete record with the given id.
     * Validation is in-place for both path variable and request body.
     * If the record is not found returns 404 Not found.
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(
            @PathVariable @Positive(message = "Controller: Path variable id must be greater than zero.") int id
    ) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok().body("Record with id: " + id + " deleted from db");
    }

    /**
     * Get mapping to fetch appointments between given date range.
     * Validation is in-place for the request body.
     * @param dateRange
     */
    @GetMapping
    public List<Appointment> getAppointmentsForDateRange(
            @RequestBody @Valid DateRangePojo dateRange
    ) {
        return appointmentService.getAppointmentsForDateRange(
                dateRange.getStartDate(),
                dateRange.getEndDate()
        );
    }
}
