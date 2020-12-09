package es.udc.fi.dc.fd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.udc.fi.dc.fd.model.persistence.DefaultChatMessageEntity;

public interface ChatMessageRepository extends JpaRepository<DefaultChatMessageEntity, Integer> {

	@Query("SELECT s from ChatMessageEntity s WHERE s.chatId  = :chatId ORDER BY s.timeSent DESC")
	List<DefaultChatMessageEntity> findByChatId(@Param("chatId") Integer chatId);
}
