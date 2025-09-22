package rs.ac.bg.fon.nastava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.nastava.model.entity.Zaposleni;
import rs.ac.bg.fon.nastava.repo.ZaposleniRepo;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class ZaposleniService {

    @Autowired
    private ZaposleniRepo zaposleniRepo;

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private static final Pattern JMBG_PATTERN =
        Pattern.compile("^\\d{13}$");

    public List<Zaposleni> findAll() {
        return zaposleniRepo.findAll();
    }

    public List<Zaposleni> findAllActive() {
        return zaposleniRepo.findByDeletedAtIsNull();
    }

    public Optional<Zaposleni> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        return zaposleniRepo.findById(id);
    }

    public Optional<Zaposleni> findActiveById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        return zaposleniRepo.findByIdAndDeletedAtIsNull(id);
    }

    public Zaposleni save(Zaposleni zaposleni) {
        validateZaposleni(zaposleni);
        validateUniqueConstraints(zaposleni);
        if (zaposleni.getDatumZaposlenja() == null) {
            zaposleni.setDatumZaposlenja(LocalDateTime.now());
        }
        zaposleni.setDeletedAt(null);
        return zaposleniRepo.save(zaposleni);
    }

    public Zaposleni update(UUID id, Zaposleni zaposleni) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        Optional<Zaposleni> existing = zaposleniRepo.findByIdAndDeletedAtIsNull(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Aktivni zaposleni sa ID " + id + " nije pronađen");
        }
        zaposleni.setId(id);
        zaposleni.setDatumZaposlenja(existing.get().getDatumZaposlenja());
        zaposleni.setDeletedAt(null);
        validateZaposleni(zaposleni);
        validateUniqueConstraints(zaposleni);
        return zaposleniRepo.save(zaposleni);
    }

    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        Optional<Zaposleni> zaposleni = zaposleniRepo.findByIdAndDeletedAtIsNull(id);
        if (zaposleni.isEmpty()) {
            throw new IllegalArgumentException("Aktivni zaposleni sa ID " + id + " nije pronađen");
        }
        Zaposleni entity = zaposleni.get();
        entity.setDeletedAt(OffsetDateTime.now());
        zaposleniRepo.save(entity);
    }

    public void hardDeleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        if (!zaposleniRepo.existsById(id)) {
            throw new IllegalArgumentException("Zaposleni sa ID " + id + " nije pronađen");
        }
        zaposleniRepo.deleteById(id);
    }

    private void validateZaposleni(Zaposleni zaposleni) {
        if (zaposleni == null) {
            throw new IllegalArgumentException("Zaposleni ne može biti null");
        }
        if (zaposleni.getIme() == null || zaposleni.getIme().trim().isEmpty()) {
            throw new IllegalArgumentException("Ime ne može biti null ili prazno");
        }
        if (zaposleni.getPrezime() == null || zaposleni.getPrezime().trim().isEmpty()) {
            throw new IllegalArgumentException("Prezime ne može biti null ili prazno");
        }
        if (zaposleni.getJmbg() == null || !JMBG_PATTERN.matcher(zaposleni.getJmbg()).matches()) {
            throw new IllegalArgumentException("JMBG mora biti tačno 13 cifara");
        }
        if (zaposleni.getEmail() == null || !EMAIL_PATTERN.matcher(zaposleni.getEmail()).matches()) {
            throw new IllegalArgumentException("Email format nije valjan");
        }
        if (zaposleni.getIme().length() > 100) {
            throw new IllegalArgumentException("Ime ne može biti duže od 100 karaktera");
        }
        if (zaposleni.getPrezime().length() > 100) {
            throw new IllegalArgumentException("Prezime ne može biti duže od 100 karaktera");
        }
        if (zaposleni.getEmail().length() > 255) {
            throw new IllegalArgumentException("Email ne može biti duži od 255 karaktera");
        }
    }

    private void validateUniqueConstraints(Zaposleni zaposleni) {
        Optional<Zaposleni> existingByJmbg = zaposleniRepo.findByJmbgAndDeletedAtIsNull(zaposleni.getJmbg());
        if (existingByJmbg.isPresent() && !existingByJmbg.get().getId().equals(zaposleni.getId())) {
            throw new IllegalArgumentException("Zaposleni sa JMBG '" + zaposleni.getJmbg() + "' već postoji");
        }

        Optional<Zaposleni> existingByEmail = zaposleniRepo.findByEmailAndDeletedAtIsNull(zaposleni.getEmail());
        if (existingByEmail.isPresent() && !existingByEmail.get().getId().equals(zaposleni.getId())) {
            throw new IllegalArgumentException("Zaposleni sa email '" + zaposleni.getEmail() + "' već postoji");
        }
    }
}