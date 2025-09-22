package rs.ac.bg.fon.nastava.model.dto;

import rs.ac.bg.fon.nastava.model.entity.Angazovanje;
import rs.ac.bg.fon.nastava.model.entity.OblikNastave;

import java.time.LocalDateTime;
import java.util.UUID;

public record AngazovanjeDto(
        UUID id,
        Integer brojCasova,
        OblikNastave uloga,
        LocalDateTime datumPocetka,
        LocalDateTime datumZavrsetka,
        PredmetDto predmet,
        ZaposleniDto zaposleni
) {

    public static AngazovanjeDto from(Angazovanje angazovanje) {
        return new AngazovanjeDto(
                angazovanje.getId(),
                angazovanje.getBrojCasova(),
                angazovanje.getUloga(),
                angazovanje.getDatumPocetka(),
                angazovanje.getDatumZavrsetka(),
                angazovanje.getPredmet() != null ? PredmetDto.from(angazovanje.getPredmet()) : null,
                angazovanje.getZaposleni() != null ? ZaposleniDto.from(angazovanje.getZaposleni()) : null
        );
    }

    public Angazovanje toEntity() {
        Angazovanje angazovanje = new Angazovanje();
        angazovanje.setId(id);
        angazovanje.setBrojCasova(brojCasova);
        angazovanje.setUloga(uloga);
        angazovanje.setDatumPocetka(datumPocetka);
        angazovanje.setDatumZavrsetka(datumZavrsetka);
        angazovanje.setPredmet(predmet != null ? predmet.toEntity() : null);
        angazovanje.setZaposleni(zaposleni != null ? zaposleni.toEntity() : null);
        return angazovanje;
    }
}