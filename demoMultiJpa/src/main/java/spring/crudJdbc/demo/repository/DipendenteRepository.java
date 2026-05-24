package spring.crudJdbc.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.crudJdbc.demo.model.Dipendente;

@Repository
public interface DipendenteRepository extends JpaRepository<Dipendente, Long> {
}