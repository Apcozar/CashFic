package es.udc.fi.dc.fd.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.udc.fi.dc.fd.model.persistence.DefaultChatRoomEntity;

public interface ChatRoomRepository extends JpaRepository<DefaultChatRoomEntity, Integer> {

	@Query("SELECT s from ChatRoomEntity s WHERE s.userOne.id IN (:userOneId, :userTwoId) "
			+ "AND s.userTwo.id IN (:userOneId, :userTwoId)")
	Optional<DefaultChatRoomEntity> findBySenderIdAndRecipientId(@Param("userOneId") Integer userOneId,
			@Param("userTwoId") Integer userTwoId);

	@Query("SELECT s from ChatRoomEntity s WHERE s.userOne.id IN (:userId) " + "OR s.userTwo.id IN (:userId)")
	Set<DefaultChatRoomEntity> findByUserId(@Param("userId") Integer userId);
}
