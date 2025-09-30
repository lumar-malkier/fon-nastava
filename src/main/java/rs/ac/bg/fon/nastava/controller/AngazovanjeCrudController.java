package rs.ac.bg.fon.nastava.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.nastava.model.dto.AngazovanjeDto;
import rs.ac.bg.fon.nastava.model.entity.Angazovanje;
import rs.ac.bg.fon.nastava.service.AngazovanjeService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/engagements")
class AngazovanjeCrudController {

    private final AngazovanjeService angazovanjeService;

    /**
     * GET /api/engagements
     * Returns all engagements
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    public ResponseEntity<List<AngazovanjeDto>> getAll() {
        List<Angazovanje> angazovanja = angazovanjeService.findAll();
        List<AngazovanjeDto> dtoList = angazovanja.stream()
                .map(AngazovanjeDto::from)
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    /**
     * GET /api/engagements/{id}
     * Returns engagement by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    public ResponseEntity<AngazovanjeDto> getById(@PathVariable UUID id) {
        Optional<Angazovanje> optional = angazovanjeService.findById(id);
        return optional
                .map(a -> ResponseEntity.ok(AngazovanjeDto.from(a)))
                .orElseThrow(() ->
                        new EntityNotFoundException("Angazovanje sa ID '%s' nije pronađeno.".formatted(id))
                );
    }

    /**
     * POST /api/engagements
     * Creates a new engagement
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AngazovanjeDto> create(@RequestBody AngazovanjeDto dto) {
        Angazovanje entity = dto.toEntity();
        entity.setId(null); // Ensure new entity creation

        Angazovanje saved = angazovanjeService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(AngazovanjeDto.from(saved));
    }

    /**
     * PUT /api/engagements/{id}
     * Updates existing engagement
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AngazovanjeDto> update(@PathVariable UUID id, @RequestBody AngazovanjeDto dto) {
        Angazovanje entity = dto.toEntity();
        Angazovanje updated = angazovanjeService.update(id, entity);
        return ResponseEntity.ok(AngazovanjeDto.from(updated));
    }

    /**
     * DELETE /api/engagements/{id}
     * Deletes engagement by ID
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        angazovanjeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}