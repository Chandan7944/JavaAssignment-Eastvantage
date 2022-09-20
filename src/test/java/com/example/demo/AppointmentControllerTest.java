package com.example.demo;

import com.example.demo.controller.AppointmentController;
import com.example.demo.model.Appointment;
import com.example.demo.model.DateRangePojo;
import com.example.demo.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AppointmentService appointmentService;

    private final String URL = "/appointment";

    @Test
    void getAppointmentDetailsTest() throws Exception {
        Appointment returnValue = new Appointment("user name", new Date(), 1, "appointment purpose");
        returnValue.setId(1);
        when(appointmentService.getAppointmentDetails(isA(Integer.class))).thenReturn(returnValue);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    void saveAppointmentTest() throws Exception {
        Appointment appointmentRecord = new Appointment("user name", new Date(), 1, "appointment purpose");
        Appointment returnValue = new Appointment("user name", new Date(), 1, "appointment purpose");
        returnValue.setId(1);
        when(appointmentService.saveAppointment(isA(Appointment.class))).thenReturn(returnValue);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(appointmentRecord);

        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"))
                .andExpect(status().isCreated());

    }

    @Test
    void updateAppointmentTest() throws Exception {
        Appointment updatedRecord = new Appointment("user name", new Date(), 1, "updated appointment purpose");
        Appointment returnRecord = new Appointment("user name", new Date(), 1, "updated appointment purpose");
        returnRecord.setId(1);
        when(appointmentService.updateAppointment(isA(Integer.class), isA(Appointment.class))).thenReturn(returnRecord);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(updatedRecord);

        mockMvc.perform(put(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAppointmentTest() throws Exception {
        doNothing().when(appointmentService).deleteAppointment(isA(Integer.class));

        String expectedResponse = "Record with id: 1 deleted from db";

        MvcResult result = mockMvc.perform(delete(URL + "/1"))
                .andExpect(status().isOk())
                .andReturn();


        String response = result.getResponse().getContentAsString();
        assert(response.equals(expectedResponse));
    }

    @Test
    void getAppointmentsForDateRangeTest() throws Exception {
        Appointment record1 = new Appointment("user name", new Date(), 1, "appointment purpose");
        Appointment record2 = new Appointment("user name", new Date(), 1, "appointment purpose");
        record1.setId(1);
        record2.setId(2);
        List<Appointment> list = new ArrayList<>();
        list.add(record1);
        list.add(record2);
        when(appointmentService.getAppointmentsForDateRange(isA(Date.class), isA(Date.class))).thenReturn(list);
        DateRangePojo dateRangePojo = new DateRangePojo(new Date(), new Date());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dateRangePojo);

        mockMvc.perform(get(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }
}
