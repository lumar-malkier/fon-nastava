package rs.ac.bg.fon.nastava.model.dto;

import rs.ac.bg.fon.nastava.model.entity.Katedra;
import rs.ac.bg.fon.nastava.model.entity.Zaposleni;
import rs.ac.bg.fon.nastava.model.entity.Zvanje;

import java.time.LocalDateTime;
import java.util.UUID;

public record ZaposleniDto(
        UUID id,
        String ime,
        String prezime,
        String email,
        LocalDateTime datumZaposlenja,
        UUID katedraId,
        String jmbg,
        Zvanje zvanje
        ) {

    public static ZaposleniDto from(Zaposleni zaposleni) {
        return new ZaposleniDto(
                zaposleni.getId(),
                zaposleni.getIme(),
                zaposleni.getPrezime(),
                zaposleni.getEmail(),
                zaposleni.getDatumZaposlenja(),
                zaposleni.getKatedra() != null ? zaposleni.getKatedra().getId() : null,
                zaposleni.getJmbg(),
                zaposleni.getZvanje()
        );
    }

    public Zaposleni toEntity() {
        Zaposleni zaposleni = new Zaposleni();
        zaposleni.setId(id);
        zaposleni.setIme(ime);
        zaposleni.setPrezime(prezime);
        zaposleni.setEmail(email);
        zaposleni.setDatumZaposlenja(datumZaposlenja);

        if (katedraId != null) {
            Katedra k = new Katedra();
            k.setId(katedraId);
            zaposleni.setKatedra(k);
        }

        zaposleni.setJmbg(jmbg);
        zaposleni.setZvanje(zvanje);

        return zaposleni;
    }
}