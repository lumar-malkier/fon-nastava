package rs.ac.bg.fon.nastava.model.dto;

import rs.ac.bg.fon.nastava.model.entity.Angazovanje;
import rs.ac.bg.fon.nastava.model.entity.OblikNastave;
import rs.ac.bg.fon.nastava.model.entity.Predmet;
import rs.ac.bg.fon.nastava.model.entity.Zaposleni;

import java.time.LocalDateTime;
import java.util.UUID;

public record AngazovanjeDto(
        UUID id,
        Integer brojCasova,
        OblikNastave uloga,
        LocalDateTime datumPocetka,
        LocalDateTime datumZavrsetka,
        UUID predmetId,
        String predmetNaziv,
        UUID zaposleniId,
        String zaposleniIme
) {

    public static AngazovanjeDto from(Angazovanje angazovanje) {
        return new AngazovanjeDto(
                angazovanje.getId(),
                angazovanje.getBrojCasova(),
                angazovanje.getUloga(),
                angazovanje.getDatumPocetka(),
                angazovanje.getDatumZavrsetka(),
                angazovanje.getPredmet() != null ? angazovanje.getPredmet().getId() : null,
                angazovanje.getPredmet() != null ? angazovanje.getPredmet().getNaziv() : null,
                angazovanje.getZaposleni() != null ? angazovanje.getZaposleni().getId() : null,
                angazovanje.getZaposleni() != null
                        ? angazovanje.getZaposleni().getIme() + " " + angazovanje.getZaposleni().getPrezime()
                        : null
        );
    }

    public Angazovanje toEntity() {
        Angazovanje angazovanje = new Angazovanje();
        angazovanje.setId(id);
        angazovanje.setBrojCasova(brojCasova);
        angazovanje.setUloga(uloga);
        angazovanje.setDatumPocetka(datumPocetka);
        angazovanje.setDatumZavrsetka(datumZavrsetka);

        if (predmetId != null) {
            Predmet p = new Predmet();
            p.setId(predmetId);
            angazovanje.setPredmet(p);
        }

        if (zaposleniId != null) {
            Zaposleni z = new Zaposleni();
            z.setId(zaposleniId);
            angazovanje.setZaposleni(z);
        }

        return angazovanje;
    }
}