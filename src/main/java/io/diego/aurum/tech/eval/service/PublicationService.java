package io.diego.aurum.tech.eval.service;

import io.diego.aurum.tech.eval.business.PublicationBusiness;
import io.diego.aurum.tech.eval.model.entity.Publication;
import io.diego.aurum.tech.eval.repository.PublicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicationService {

    private final PublicationRepository repository;

    public Publication save(Publication publication) {
        PublicationBusiness.beforeSave(publication);
        return repository.save(publication);
    }

    public void delete(long publicationId){
        repository.deleteById(publicationId);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public Page<Publication> list(Pageable pageable){
         return repository.findAll(pageable);
    }

}
