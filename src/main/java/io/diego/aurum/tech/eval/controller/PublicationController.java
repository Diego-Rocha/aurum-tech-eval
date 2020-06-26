package io.diego.aurum.tech.eval.controller;

import io.diego.aurum.tech.eval.entity.Publication;
import io.diego.aurum.tech.eval.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/publication")
public class PublicationController {

    private final PublicationService service;

    @PostMapping
    public void save(@Valid @RequestBody Publication publication){
        service.save(publication);
    }
}
