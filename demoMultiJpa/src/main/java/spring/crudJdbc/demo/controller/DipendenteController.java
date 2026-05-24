package spring.crudJdbc.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.crudJdbc.demo.model.Dipendente;
import spring.crudJdbc.demo.repository.DipendenteRepository;

@RestController
@RequestMapping("/api/dipendenti")
public class DipendenteController {

    private final DipendenteRepository repository;

    public DipendenteController(DipendenteRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Iterable<Dipendente> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dipendente> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Dipendente> create(@RequestBody Dipendente dipendente) {
        dipendente.setId(null);
        Dipendente saved = repository.save(dipendente);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dipendente> update(@PathVariable Long id, @RequestBody Dipendente dettagliDipendente) {
        return repository.findById(id).map(dipendenteEsistente -> {
            
            // 1. Aggiornamento dati anagrafici base
            dipendenteEsistente.setNome(dettagliDipendente.getNome());
            dipendenteEsistente.setCognome(dettagliDipendente.getCognome());
            dipendenteEsistente.setCodiceFiscale(dettagliDipendente.getCodiceFiscale());
            dipendenteEsistente.setGenere(dettagliDipendente.getGenere());
            dipendenteEsistente.setDataDiNascita(dettagliDipendente.getDataDiNascita());
            dipendenteEsistente.setLuogoNascita(dettagliDipendente.getLuogoNascita());
            
            // 2. Aggiornamento relazione Ruolo
            dipendenteEsistente.setRuolo(dettagliDipendente.getRuolo());
            
            // 3. Gestione Account per BLOCCARE l'auto-incremento dell'ID
            if (dettagliDipendente.getAccount() != null) {
                if (dipendenteEsistente.getAccount() != null) {
                    // Mantieni fermo lo stesso identico ID di prima sul DB
                    dettagliDipendente.getAccount().setId(dipendenteEsistente.getAccount().getId());
                }
                dipendenteEsistente.setAccount(dettagliDipendente.getAccount());
            } else {
                dipendenteEsistente.setAccount(null);
            }
            
            // 4. Aggiornamento Contatti (Svuota e ripopola per far lavorare il cascade orfani)
            dipendenteEsistente.getContatti().clear();
            if (dettagliDipendente.getContatti() != null) {
                dipendenteEsistente.getContatti().addAll(dettagliDipendente.getContatti());
            }
            
            // 5. Aggiornamento Titoli Studio
            dipendenteEsistente.getTitoliStudio().clear();
            if (dettagliDipendente.getTitoliStudio() != null) {
                dipendenteEsistente.getTitoliStudio().addAll(dettagliDipendente.getTitoliStudio());
            }
            
            Dipendente updated = repository.save(dipendenteEsistente);
            return ResponseEntity.ok(updated);
            
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}