package rs.ac.bg.fon.nastava.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.nastava.model.entity.Angazovanje;
import rs.ac.bg.fon.nastava.model.entity.Predmet;
import rs.ac.bg.fon.nastava.model.entity.Zaposleni;
import rs.ac.bg.fon.nastava.repo.AngazovanjeRepo;
import rs.ac.bg.fon.nastava.repo.PredmetRepo;
import rs.ac.bg.fon.nastava.repo.ZaposleniRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AngazovanjeService {

    private final AngazovanjeRepo angazovanjeRepo;

    private final PredmetRepo predmetRepo;

    private final ZaposleniRepo zaposleniRepo;

    public List<Angazovanje> findAll() {
        return angazovanjeRepo.findAll();
    }

    public Optional<Angazovanje> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        return angazovanjeRepo.findById(id);
    }

    public List<Angazovanje> findByPredmetId(UUID predmetId) {
        if (predmetId == null) {
            throw new IllegalArgumentException("ID predmeta ne može biti null");
        }
        return angazovanjeRepo.findByPredmet_Id(predmetId);
    }

    public List<Angazovanje> findByZaposleniId(UUID zaposleniId) {
        if (zaposleniId == null) {
            throw new IllegalArgumentException("ID zaposlenog ne može biti null");
        }
        return angazovanjeRepo.findByZaposleni_Id(zaposleniId);
    }

    public Angazovanje save(Angazovanje angazovanje) {
        validateAngazovanje(angazovanje);
        validateEntityReferences(angazovanje);
        validateBusinessRules(angazovanje);
        return angazovanjeRepo.save(angazovanje);
    }

    public Angazovanje update(UUID id, Angazovanje angazovanje) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        if (!angazovanjeRepo.existsById(id)) {
            throw new IllegalArgumentException("Angažovanje sa ID " + id + " nije pronađeno");
        }
        angazovanje.setId(id);
        validateAngazovanje(angazovanje);
        validateEntityReferences(angazovanje);
        validateBusinessRules(angazovanje);
        return angazovanjeRepo.save(angazovanje);
    }

    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne može biti null");
        }
        if (!angazovanjeRepo.existsById(id)) {
            throw new IllegalArgumentException("Angažovanje sa ID " + id + " nije pronađeno");
        }
        angazovanjeRepo.deleteById(id);
    }

    private void validateAngazovanje(Angazovanje angazovanje) {
        if (angazovanje == null) {
            throw new IllegalArgumentException("Angažovanje ne može biti null");
        }
        if (angazovanje.getBrojCasova() == null || angazovanje.getBrojCasova() <= 0) {
            throw new IllegalArgumentException("Broj časova mora biti pozitivan broj");
        }
        if (angazovanje.getBrojCasova() > 300) {
            throw new IllegalArgumentException("Broj časova ne može biti veći od 300");
        }
        if (angazovanje.getUloga() == null) {
            throw new IllegalArgumentException("Uloga ne može biti null");
        }
        if (angazovanje.getDatumPocetka() == null) {
            throw new IllegalArgumentException("Datum početka ne može biti null");
        }
        if (angazovanje.getDatumZavrsetka() == null) {
            throw new IllegalArgumentException("Datum završetka ne može biti null");
        }
        if (angazovanje.getDatumPocetka().isAfter(angazovanje.getDatumZavrsetka())) {
            throw new IllegalArgumentException("Datum početka ne može biti posle datuma završetka");
        }
        if (angazovanje.getDatumPocetka().isBefore(LocalDateTime.now().minusYears(5))) {
            throw new IllegalArgumentException("Datum početka ne može biti više od 5 godina u prošlosti");
        }
        if (angazovanje.getDatumZavrsetka().isAfter(LocalDateTime.now().plusYears(5))) {
            throw new IllegalArgumentException("Datum završetka ne može biti više od 5 godina u budućnosti");
        }
    }

    private void validateEntityReferences(Angazovanje angazovanje) {
        if (angazovanje.getPredmet() == null || angazovanje.getPredmet().getId() == null) {
            throw new IllegalArgumentException("Referenca na predmet ne može biti null");
        }
        if (angazovanje.getZaposleni() == null || angazovanje.getZaposleni().getId() == null) {
            throw new IllegalArgumentException("Referenca na zaposlenog ne može biti null");
        }

        Optional<Predmet> predmet = predmetRepo.findByIdAndDeletedAtIsNull(angazovanje.getPredmet().getId());
        if (predmet.isEmpty()) {
            throw new IllegalArgumentException("Predmet sa ID " + angazovanje.getPredmet().getId() + " nije pronađen ili je obrisan");
        }

        Optional<Zaposleni> zaposleni = zaposleniRepo.findByIdAndDeletedAtIsNull(angazovanje.getZaposleni().getId());
        if (zaposleni.isEmpty()) {
            throw new IllegalArgumentException("Zaposleni sa ID " + angazovanje.getZaposleni().getId() + " nije pronađen ili je obrisan");
        }

        angazovanje.setPredmet(predmet.get());
        angazovanje.setZaposleni(zaposleni.get());
    }

    private void validateBusinessRules(Angazovanje angazovanje) {
        List<Angazovanje> existingAngazovanja = angazovanjeRepo.findByZaposleni_IdAndPredmet_IdAndUloga(
            angazovanje.getZaposleni().getId(),
            angazovanje.getPredmet().getId(),
            angazovanje.getUloga()
        );

        for (Angazovanje existing : existingAngazovanja) {
            if (!existing.getId().equals(angazovanje.getId())) {
                if (isDateRangeOverlapping(angazovanje, existing)) {
                    throw new IllegalArgumentException(
                        "Zaposleni je već angažovan na ovom predmetu sa istom ulogom u periodu koji se preklapa"
                    );
                }
            }
        }

        List<Angazovanje> zaposleniAngazovanja = angazovanjeRepo.findByZaposleni_Id(angazovanje.getZaposleni().getId());
        int totalHours = zaposleniAngazovanja.stream()
            .filter(a -> !a.getId().equals(angazovanje.getId()))
            .filter(a -> isDateRangeOverlapping(angazovanje, a))
            .mapToInt(Angazovanje::getBrojCasova)
            .sum();

        totalHours += angazovanje.getBrojCasova();

        if (totalHours > 600) {
            throw new IllegalArgumentException("Ukupan broj časova za zaposlenog ne može previšiti 600 u periodima koji se preklapaju");
        }
    }

    private boolean isDateRangeOverlapping(Angazovanje a1, Angazovanje a2) {
        return !a1.getDatumZavrsetka().isBefore(a2.getDatumPocetka()) &&
               !a1.getDatumPocetka().isAfter(a2.getDatumZavrsetka());
    }
}