package io.diego.aurum.tech.eval.service;

import io.diego.aurum.tech.eval.entity.Publication;
import io.diego.aurum.tech.eval.repository.PublicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicationService {

    private final PublicationRepository repository;

    public void save(Publication publication) {
        repository.save(publication);
    }

}
