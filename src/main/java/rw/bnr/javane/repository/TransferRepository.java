package rw.bnr.javane.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.bnr.javane.model.Transfer;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
