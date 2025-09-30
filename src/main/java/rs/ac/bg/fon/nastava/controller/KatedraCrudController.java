package rs.ac.bg.fon.nastava.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.nastava.model.dto.KatedraDto;
import rs.ac.bg.fon.nastava.model.entity.Katedra;
import rs.ac.bg.fon.nastava.service.KatedraService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/katedra")
class KatedraCrudController {

    private final KatedraService katedraService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    public ResponseEntity<List<KatedraDto>> getAll() {
        List<Katedra> katedre = katedraService.findAll();
        List<KatedraDto> katedreDtos = katedre.stream()
                .map(KatedraDto::from)
                .toList();
        return ResponseEntity.ok(katedreDtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    public ResponseEntity<KatedraDto> getById(@PathVariable UUID id) {
        Optional<Katedra> optionalKatedra = katedraService.findById(id);
        return optionalKatedra.map(k -> ResponseEntity.ok(KatedraDto.from(k)))
                .orElseThrow(() -> new EntityNotFoundException("Katedra sa datim identifikatorom '%s' nije pronađena.".formatted(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<KatedraDto> create(@RequestBody KatedraDto katedraDto) {
        Katedra katedra = katedraDto.toEntity();
        katedra.setId(null); // Ensure new entity creation
        Katedra savedKatedra = katedraService.save(katedra);
        KatedraDto responseDto = KatedraDto.from(savedKatedra);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<KatedraDto> update(@PathVariable UUID id, @RequestBody KatedraDto katedraDto) {
        Katedra katedra = katedraDto.toEntity();
        Katedra updatedKatedra = katedraService.update(id, katedra);
        KatedraDto responseDto = KatedraDto.from(updatedKatedra);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        katedraService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
