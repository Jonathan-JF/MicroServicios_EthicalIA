package Texto.Texto.repository;

import Texto.Texto.model.Texto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextoRepository extends JpaRepository<Texto, Long> {
}
