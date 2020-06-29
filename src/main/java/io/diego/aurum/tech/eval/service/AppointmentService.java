package io.diego.aurum.tech.eval.service;

import io.diego.aurum.tech.eval.model.entity.Appointment;
import io.diego.aurum.tech.eval.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppointmentService {

    private final AppointmentRepository repository;

    public Page<Appointment> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

}
