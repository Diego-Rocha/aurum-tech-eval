package io.diego.aurum.tech.eval.controller;

import io.diego.aurum.tech.eval.converter.AlertConverter;
import io.diego.aurum.tech.eval.model.dto.AlertDTO;
import io.diego.aurum.tech.eval.model.entity.Alert;
import io.diego.aurum.tech.eval.service.AlertService;
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
@RequestMapping("/alert")
public class AlertController {

    private final AlertService service;

    @GetMapping
    public ResponseEntity<Page<AlertDTO>> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Alert> alertPage = service.list(PageRequest.of(page, size));
        Page<AlertDTO> alertDTOPage = alertPage.map(AlertConverter::convert);
        return ResponseEntity.ok(alertDTOPage);
    }
}
