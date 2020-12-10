package es.udc.fi.dc.fd.model.persistence;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.udc.fi.dc.fd.model.ChatRoomEntity;

@Entity(name = "ChatRoomEntity")
@Table(name = "chat_room")
public class DefaultChatRoomEntity implements ChatRoomEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "userIdOne")
	private DefaultUserEntity userOne;

	@ManyToOne
	@JoinColumn(name = "userIdTwo")
	private DefaultUserEntity userTwo;

	public DefaultChatRoomEntity(DefaultUserEntity userOne, DefaultUserEntity userTwo) {
		super();
		this.userOne = userOne;
		this.userTwo = userTwo;
	}

	public DefaultChatRoomEntity() {
		super();
	}

	@Override
	public void setUserTwo(DefaultUserEntity user) {
		this.userTwo = user;
	}

	@Override
	public void setUserOne(DefaultUserEntity user) {
		this.userOne = user;
	}

	@Override
	public DefaultUserEntity getUserOne() {
		return this.userOne;
	}

	@Override
	public DefaultUserEntity getUserTwo() {
		return this.userTwo;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(userOne, userTwo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof DefaultChatRoomEntity)) {
			return false;
		}
		DefaultChatRoomEntity other = (DefaultChatRoomEntity) obj;
		return Objects.equals(userOne, other.userOne) && Objects.equals(userTwo, other.userTwo);
	}

	@Override
	public String toString() {
		return "DefaultChatRoomEntity [id=" + id + ", userOne=" + userOne + ", userTwo=" + userTwo + "]";
	}
}
