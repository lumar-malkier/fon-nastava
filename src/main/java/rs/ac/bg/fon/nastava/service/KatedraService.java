package rs.ac.bg.fon.nastava.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.nastava.model.entity.Katedra;
import rs.ac.bg.fon.nastava.repo.KatedraRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KatedraService {

    private final KatedraRepo katedraRepo;

    public List<Katedra> findAll() {
        return katedraRepo.findAll();
    }

    public Optional<Katedra> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        return katedraRepo.findById(id);
    }

    public Katedra save(Katedra katedra) {
        validateKatedra(katedra);
        validateUniqueConstraints(katedra);
        return katedraRepo.save(katedra);
    }

    public Katedra update(UUID id, Katedra katedra) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        if (!katedraRepo.existsById(id)) {
            throw new IllegalArgumentException("Katedra sa ID " + id + " nije pronađena");
        }
        katedra.setId(id);
        validateKatedra(katedra);
        validateUniqueConstraints(katedra);
        return katedraRepo.save(katedra);
    }

    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        if (!katedraRepo.existsById(id)) {
            throw new IllegalArgumentException("Katedra sa ID " + id + " nije pronađena");
        }
        katedraRepo.deleteById(id);
    }

    private void validateKatedra(Katedra katedra) {
        if (katedra == null) {
            throw new IllegalArgumentException("Katedra ne može biti null");
        }
        if (katedra.getNaziv() == null || katedra.getNaziv().trim().isEmpty()) {
            throw new IllegalArgumentException("Naziv ne može biti null ili prazan");
        }
        if (katedra.getSifra() == null || katedra.getSifra().trim().isEmpty()) {
            throw new IllegalArgumentException("Šifra ne može biti null ili prazna");
        }
        if (katedra.getNaziv().length() > 255) {
            throw new IllegalArgumentException("Naziv ne može biti duži od 255 karaktera");
        }
        if (katedra.getSifra().length() > 50) {
            throw new IllegalArgumentException("Šifra ne može biti duža od 50 karaktera");
        }
    }

    private void validateUniqueConstraints(Katedra katedra) {
        Optional<Katedra> existingBySifra = katedraRepo.findBySifra(katedra.getSifra());
        if (existingBySifra.isPresent() && !existingBySifra.get().getId().equals(katedra.getId())) {
            throw new IllegalArgumentException("Katedra sa šifrom '" + katedra.getSifra() + "' već postoji");
        }
    }
}