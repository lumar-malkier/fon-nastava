package rs.ac.bg.fon.nastava.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.nastava.model.dto.PredmetDto;
import rs.ac.bg.fon.nastava.model.entity.Predmet;
import rs.ac.bg.fon.nastava.service.PredmetService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/predmet")
class PredmetCrudController {

    private final PredmetService predmetService;

    @GetMapping
    public ResponseEntity<List<PredmetDto>> getAll() {
        List<Predmet> predmeti = predmetService.findAllActive();
        List<PredmetDto> predmetiDtos = predmeti.stream()
                .map(PredmetDto::from)
                .toList();
        return ResponseEntity.ok(predmetiDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PredmetDto> getById(@PathVariable UUID id) {
        Optional<Predmet> optionalPredmet = predmetService.findActiveById(id);
        return optionalPredmet.map(p -> ResponseEntity.ok(PredmetDto.from(p)))
                .orElseThrow(() -> new EntityNotFoundException("Predmet sa datim identifikatorom '%s' nije pronađen.".formatted(id)));
    }

    @PostMapping
    public ResponseEntity<PredmetDto> create(@RequestBody PredmetDto predmetDto) {
        Predmet predmet = predmetDto.toEntity();
        predmet.setId(null);
        Predmet savedPredmet = predmetService.save(predmet);
        PredmetDto responseDto = PredmetDto.from(savedPredmet);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PredmetDto> update(@PathVariable UUID id, @RequestBody PredmetDto predmetDto) {
        Predmet predmet = predmetDto.toEntity();
        Predmet updatedPredmet = predmetService.update(id, predmet);
        PredmetDto responseDto = PredmetDto.from(updatedPredmet);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        predmetService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}