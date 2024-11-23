package ct553.backend.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MessageRepository extends JpaRepository<Message, Long> {
    
    List<Message> findByConversationIdOrderByTimeStampAsc(Long conversationId);
}
