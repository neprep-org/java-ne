package rw.bnr.javane.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.bnr.javane.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
