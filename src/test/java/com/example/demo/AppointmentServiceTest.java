package com.example.demo;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Appointment;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void saveAppointmentTest() {
        Appointment newAppointment = new Appointment("user name", new Date(), 1, "appointment purpose");
        Appointment returnValue = new Appointment("user name", new Date(), 1, "appointment purpose");
        returnValue.setId(1);
        when(appointmentRepository.save(isA(Appointment.class))).thenReturn(returnValue);

        Appointment result = appointmentService.saveAppointment(newAppointment);

        assert result != null;
        assert(result.getId() == 1);
    }

    @Test
    void getAppointmentDetailsTest() {
        Appointment returnValue = new Appointment("user name", new Date(), 1, "appointment purpose");
        returnValue.setId(1);
        when(appointmentRepository.findById(isA(Integer.class))).thenReturn(Optional.of(returnValue));

        Appointment result = appointmentService.getAppointmentDetails(1);

        assert result != null;
        assert(result.getId() == 1);
    }

    @Test
    void getAppointmentDetailsTest_Exception() {
        Appointment returnValue = new Appointment("user name", new Date(), 1, "appointment purpose");
        returnValue.setId(1);
        when(appointmentRepository.findById(isA(Integer.class))).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.getAppointmentDetails(1);
        });

        String expectedMessage = "No Appointment record found for 1";
        assertTrue(ex.getMessage().contains(expectedMessage));
    }

    @Test
    void updateAppointmentTest() {
        Appointment updatedRecord = new Appointment("user name", new Date(), 1, "updated appointment purpose");
        Appointment existingRecord = new Appointment("user name", new Date(), 1, "appointment purpose");
        existingRecord.setId(1);
        when(appointmentRepository.findById(isA(Integer.class))).thenReturn(Optional.of(existingRecord));
        when(appointmentRepository.save(isA(Appointment.class))).thenReturn(existingRecord);

        Appointment result = appointmentService.updateAppointment(1, updatedRecord);

        assert result != null;
        assert(result.getId() == 1);
        assert(result.getPurpose().equals(updatedRecord.getPurpose()));
    }

    @Test
    void updateAppointmentTest_Exception() {
        Appointment updatedRecord = new Appointment("user name", new Date(), 1, "updated appointment purpose");
        Appointment existingRecord = new Appointment("user name", new Date(), 1, "appointment purpose");
        existingRecord.setId(1);
        when(appointmentRepository.findById(isA(Integer.class))).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.updateAppointment(1, updatedRecord);
        });

        String expectedMessage = "No Appointment record found for 1";
        assertTrue(ex.getMessage().contains(expectedMessage));
    }

    @Test
    void getAppointmentsForDateRangeTest() {
        Appointment record1 = new Appointment("user name", new Date(), 1, "appointment purpose");
        Appointment record2 = new Appointment("user name", new Date(), 1, "appointment purpose");
        record1.setId(1);
        record2.setId(2);
        List<Appointment> list = new ArrayList<>();
        list.add(record1);
        list.add(record2);
        when(appointmentRepository.findByDateTimeBetween(isA(Date.class), isA(Date.class))).thenReturn(Optional.of(list));

        List<Appointment> result = appointmentService.getAppointmentsForDateRange(new Date(), new Date());

        assert result != null;
        assert(result.size() == 2);
        assert(result.get(0).getId() == 1);
    }

    @Test
    void getAppointmentsForDateRangeTest_Exception() {
        when(appointmentRepository.findByDateTimeBetween(isA(Date.class), isA(Date.class))).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.getAppointmentsForDateRange(new Date(), new Date());
        });

        String expectedMessage = "No Appointment record found between dates";
        System.out.println(ex.getMessage());
        assertTrue(ex.getMessage().contains(expectedMessage));
    }


}
