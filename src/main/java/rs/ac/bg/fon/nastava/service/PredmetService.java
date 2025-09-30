package rs.ac.bg.fon.nastava.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.nastava.model.entity.Predmet;
import rs.ac.bg.fon.nastava.repo.PredmetRepo;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PredmetService {

    private final PredmetRepo predmetRepo;

    public List<Predmet> findAll() {
        return predmetRepo.findAll();
    }

    public List<Predmet> findAllActive() {
        return predmetRepo.findByDeletedAtIsNull();
    }

    public Optional<Predmet> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        return predmetRepo.findById(id);
    }

    public Optional<Predmet> findActiveById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        return predmetRepo.findByIdAndDeletedAtIsNull(id);
    }

    public Predmet save(Predmet predmet) {
        validatePredmet(predmet);
        validateUniqueConstraints(predmet);
        predmet.setDeletedAt(null);
        return predmetRepo.save(predmet);
    }

    public Predmet update(UUID id, Predmet predmet) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        Optional<Predmet> existing = predmetRepo.findByIdAndDeletedAtIsNull(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Aktivni predmet sa ID " + id + " nije pronađen");
        }
        predmet.setId(id);
        predmet.setDeletedAt(null);
        validatePredmet(predmet);
        validateUniqueConstraints(predmet);
        return predmetRepo.save(predmet);
    }

    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        Optional<Predmet> predmet = predmetRepo.findByIdAndDeletedAtIsNull(id);
        if (predmet.isEmpty()) {
            throw new IllegalArgumentException("Aktivni predmet sa ID " + id + " nije pronađen");
        }
        Predmet entity = predmet.get();
        entity.setDeletedAt(OffsetDateTime.now());
        predmetRepo.save(entity);
    }

    public void hardDeleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        if (!predmetRepo.existsById(id)) {
            throw new IllegalArgumentException("Predmet sa ID " + id + " nije pronađen");
        }
        predmetRepo.deleteById(id);
    }

    private void validatePredmet(Predmet predmet) {
        if (predmet == null) {
            throw new IllegalArgumentException("Predmet ne može biti null");
        }
        if (predmet.getNaziv() == null || predmet.getNaziv().trim().isEmpty()) {
            throw new IllegalArgumentException("Naziv ne može biti null ili prazan");
        }
        if (predmet.getSifra() == null || predmet.getSifra().trim().isEmpty()) {
            throw new IllegalArgumentException("Šifra ne može biti null ili prazna");
        }
        if (predmet.getEspb() == null || predmet.getEspb() <= 0) {
            throw new IllegalArgumentException("ESPB mora biti pozitivan broj");
        }
        if (predmet.getEspb() > 20) {
            throw new IllegalArgumentException("ESPB ne može biti veći od 20");
        }
        if (predmet.getNaziv().length() > 255) {
            throw new IllegalArgumentException("Naziv ne može biti duži od 255 karaktera");
        }
        if (predmet.getSifra().length() > 50) {
            throw new IllegalArgumentException("Šifra ne može biti duža od 50 karaktera");
        }
    }

    private void validateUniqueConstraints(Predmet predmet) {
        Optional<Predmet> existingBySifra = predmetRepo.findBySifraAndDeletedAtIsNull(predmet.getSifra());
        if (existingBySifra.isPresent() && !existingBySifra.get().getId().equals(predmet.getId())) {
            throw new IllegalArgumentException("Predmet sa šifrom '" + predmet.getSifra() + "' već postoji");
        }

        Optional<Predmet> existingByNaziv = predmetRepo.findByNazivAndDeletedAtIsNull(predmet.getNaziv());
        if (existingByNaziv.isPresent() && !existingByNaziv.get().getId().equals(predmet.getId())) {
            throw new IllegalArgumentException("Predmet sa nazivom '" + predmet.getNaziv() + "' već postoji");
        }
    }
}