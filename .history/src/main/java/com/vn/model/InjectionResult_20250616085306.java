package com.vn.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "INJECTION_RESULT")
public class InjectionResult {
    @Id
    @Column(name ="INJECTION_RESULT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer injectionResultId;

    @Column(name = "INJECTION_DATE")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Injection date is required")
    private LocalDate injectionDate;

    @Column(name = "INJECTION_PLACE", columnDefinition = "NVARCHAR(100)")
    private String injectionPlace;

    @Column(name = "NEXT_INJECTION_DATE")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Time of next date injection is required")
    @Future(message = "next date must be in the future")
    private LocalDate nextInjectionDate;

    @Column(name = "NUMBER_OF_INJECTION")
    @NotNull(message = "This field cannot be left blank.")
    @Digits(integer = 10, fraction = 0, message = "Only enter numbers")
    private Integer numberOfInjection;

    @Column(name = "PREVENTION", length = 100, columnDefinition = "NVARCHAR(100)")
    @NotBlank(message = "prevention is required")
    private String prevention;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "VACCINE_ID" ,referencedColumnName = "VACCINE_ID")
    @NotNull(message = "Vaccine is required")
    private Vaccine vaccinePKInjection;



}
