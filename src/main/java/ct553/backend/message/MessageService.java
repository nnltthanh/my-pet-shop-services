package ct553.backend.message;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ct553.backend.conversation.Conversation;
import ct553.backend.conversation.ConversationRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;
    
    void addMessage(Message message) {
        if (message.getConversation() == null || message.getConversation().getId() == null) {
            Conversation conversation = new Conversation();
            conversation.setStartTime(LocalDateTime.now());
            message.setConversation(conversation);
        } else {
            message.setConversation(this.conversationRepository.findById(message.getConversation().getId()).get());
        }
        this.messageRepository.save(message);
    }

    ArrayList<Message> getAllMessages() {
        return (ArrayList<Message>) this.messageRepository.findAll();
    }

    ArrayList<Message> getAllMessagesByConversation(Long id) {
        return (ArrayList<Message>) this.messageRepository.findByConversationIdOrderByTimeStampAsc(id);
    }

    public Message findMessageById(Long id) {
        return  this.messageRepository.findById(id).orElse(null);
    }

    void deleteMessageById(Long id) {
        this.messageRepository.deleteById(id);
    }

}
