package io.diego.aurum.tech.eval.controller;

import io.diego.aurum.tech.eval.business.PublicationBusiness;
import io.diego.aurum.tech.eval.converter.PublicationConverter;
import io.diego.aurum.tech.eval.model.dto.PublicationDTO;
import io.diego.aurum.tech.eval.model.entity.Publication;
import io.diego.aurum.tech.eval.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/publication")
public class PublicationController {

    private final PublicationService service;

    @PostMapping
    public Long save(@Valid @RequestBody PublicationDTO publicationDTO) {
        Publication publication = PublicationConverter.convert(publicationDTO);
        publication = service.save(publication);
        return publication.getId();
    }

    @DeleteMapping("/{publicationId}")
    public void delete(@PathVariable Long publicationId) {
        service.delete(publicationId);
    }

    @DeleteMapping
    public void deleteAll() {
        service.deleteAll();
    }

    @GetMapping
    public ResponseEntity<Page<PublicationDTO>> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Publication> publicationPage = service.list(PageRequest.of(page, size));
        Page<PublicationDTO> publicationDTOPage = publicationPage.map(PublicationConverter::convert);
        return ResponseEntity.ok(publicationDTOPage);
    }
}
