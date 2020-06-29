package io.diego.aurum.tech.eval.service;

import io.diego.aurum.tech.eval.model.entity.Alert;
import io.diego.aurum.tech.eval.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlertService {

    private final AlertRepository repository;

    public Page<Alert> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

}
