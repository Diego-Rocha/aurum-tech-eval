package io.diego.aurum.tech.eval.controller;

import io.diego.aurum.tech.eval.converter.AppointmentConverter;
import io.diego.aurum.tech.eval.model.dto.AppointmentDTO;
import io.diego.aurum.tech.eval.model.entity.Appointment;
import io.diego.aurum.tech.eval.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService service;

    @GetMapping
    public ResponseEntity<Page<AppointmentDTO>> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Appointment> appointmentPage = service.list(PageRequest.of(page, size));
        Page<AppointmentDTO> appointmentDTOPage = appointmentPage.map(AppointmentConverter::convert);
        return ResponseEntity.ok(appointmentDTOPage);
    }
}
