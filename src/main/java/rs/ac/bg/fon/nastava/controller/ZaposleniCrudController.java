package rs.ac.bg.fon.nastava.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.nastava.model.dto.ZaposleniDto;
import rs.ac.bg.fon.nastava.model.entity.Zaposleni;
import rs.ac.bg.fon.nastava.service.ZaposleniService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/zaposleni")
class ZaposleniCrudController {

    private final ZaposleniService zaposleniService;

    @GetMapping
    public ResponseEntity<List<ZaposleniDto>> getAll() {
        List<Zaposleni> zaposleni = zaposleniService.findAllActive();
        List<ZaposleniDto> zaposleniDtos = zaposleni.stream()
                .map(ZaposleniDto::from)
                .toList();
        return ResponseEntity.ok(zaposleniDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZaposleniDto> getById(@PathVariable UUID id) {
        Optional<Zaposleni> optionalZaposleni = zaposleniService.findActiveById(id);
        return optionalZaposleni.map(z -> ResponseEntity.ok(ZaposleniDto.from(z)))
                .orElseThrow(() -> new EntityNotFoundException("Zaposleni sa datim identifikatorom '%s' nije pronađen.".formatted(id)));
    }

    @PostMapping
    public ResponseEntity<ZaposleniDto> create(@RequestBody ZaposleniDto zaposleniDto) {
        Zaposleni zaposleni = zaposleniDto.toEntity();
        zaposleni.setId(null);
        Zaposleni savedZaposleni = zaposleniService.save(zaposleni);
        ZaposleniDto responseDto = ZaposleniDto.from(savedZaposleni);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZaposleniDto> update(@PathVariable UUID id, @RequestBody ZaposleniDto zaposleniDto) {
        Zaposleni zaposleni = zaposleniDto.toEntity();
        Zaposleni updatedZaposleni = zaposleniService.update(id, zaposleni);
        ZaposleniDto responseDto = ZaposleniDto.from(updatedZaposleni);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        zaposleniService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}