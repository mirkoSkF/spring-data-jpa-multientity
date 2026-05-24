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

    // Injection tramite costruttore
    public DipendenteController(DipendenteRepository repository) {
        this.repository = repository;
    }

    // READ ALL
    @GetMapping
    public Iterable<Dipendente> getAll() {
        return repository.findAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Dipendente> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Dipendente> create(@RequestBody Dipendente dipendente) {
        // Garantiamo la INSERT azzerando l'ID primario dell'aggregato
        dipendente.setId(null);
        Dipendente saved = repository.save(dipendente);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Dipendente> update(@PathVariable Long id, @RequestBody Dipendente dettagliDipendente) {
        return repository.findById(id).map(dipendenteEsistente -> {
            
            // 1. Aggiornamento attributi anagrafici diretti
            dipendenteEsistente.setNome(dettagliDipendente.getNome());
            dipendenteEsistente.setCognome(dettagliDipendente.getCognome());
            dipendenteEsistente.setCodiceFiscale(dettagliDipendente.getCodiceFiscale());
            dipendenteEsistente.setGenere(dettagliDipendente.getGenere());
            dipendenteEsistente.setDataDiNascita(dettagliDipendente.getDataDiNascita());
            dipendenteEsistente.setLuogoNascita(dettagliDipendente.getLuogoNascita());
            
            // 2. Aggiornamento relazione ManyToOne (Ruolo)
            dipendenteEsistente.setRuolo(dettagliDipendente.getRuolo());
            
            // 3. Allineamento OneToOne (Account) - Evita il salto dell'auto-increment su DB
            if (dettagliDipendente.getAccount() != null) {
                if (dipendenteEsistente.getAccount() != null) {
                    dettagliDipendente.getAccount().setId(dipendenteEsistente.getAccount().getId());
                }
                dipendenteEsistente.setAccount(dettagliDipendente.getAccount());
            } else {
                dipendenteEsistente.setAccount(null);
            }
            
            // 4. Aggiornamento delle liste orfane (Contatti)
            dipendenteEsistente.getContatti().clear();
            if (dettagliDipendente.getContatti() != null) {
                dipendenteEsistente.getContatti().addAll(dettagliDipendente.getContatti());
            }
            
            // 5. Aggiornamento ManyToMany diretto (Titoli di Studio)
            // Svuotando e ripopolando, Hibernate si occupa di fare le giuste INSERT/DELETE nella tabella 'dipendenti_titoli'
            dipendenteEsistente.getTitoliStudio().clear();
            if (dettagliDipendente.getTitoliStudio() != null) {
                dipendenteEsistente.getTitoliStudio().addAll(dettagliDipendente.getTitoliStudio());
            }
            
            // Salva lo stato sincronizzato dell'entità gestita
            Dipendente updated = repository.save(dipendenteEsistente);
            return ResponseEntity.ok(updated);
            
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
