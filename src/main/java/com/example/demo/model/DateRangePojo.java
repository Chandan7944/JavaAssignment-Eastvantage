package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class DateRangePojo {
    public DateRangePojo(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @DateTimeFormat(style = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "DateRangePojo.class: startDate field cannot be blank.")
    private Date startDate;

    @DateTimeFormat(style = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "DateRangePojo.class: endDate field cannot be blank.")
    private Date endDate;

}
