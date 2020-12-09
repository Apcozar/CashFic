package es.udc.fi.dc.fd.model.persistence;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import es.udc.fi.dc.fd.model.ChatMessageEntity;

@Entity(name = "ChatMessageEntity")
@Table(name = "chat_messages")
public class DefaultChatMessageEntity implements ChatMessageEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "chatId", nullable = false, unique = true)
	private Integer chatId;

	@OneToOne
	@JoinColumn(name = "authorUserId")
	private DefaultUserEntity authorUser;

	@OneToOne
	@JoinColumn(name = "recipientUserId")
	private DefaultUserEntity recipientUser;

	@Column(name = "timeSent", nullable = false, unique = false)
	private Date timeSent;

	@Column(name = "content", nullable = false, unique = false)
	private String contents;

	public DefaultChatMessageEntity() {
		super();
	}

	public DefaultChatMessageEntity(Integer chatId, DefaultUserEntity authorUser, DefaultUserEntity recipientUser,
			String contents) {
		super();
		this.chatId = chatId;
		this.authorUser = authorUser;
		this.recipientUser = recipientUser;
		this.contents = contents;
		this.timeSent = new Date();
	}

	public DefaultChatMessageEntity(Integer chatId, DefaultUserEntity authorUser, DefaultUserEntity recipientUser,
			Date timeSent, String contents) {
		super();
		this.chatId = chatId;
		this.authorUser = authorUser;
		this.recipientUser = recipientUser;
		this.timeSent = timeSent;
		this.contents = contents;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public DefaultUserEntity getAuthorUser() {
		return this.authorUser;
	}

	@Override
	public DefaultUserEntity getRecipientUser() {
		return this.recipientUser;
	}

	@Override
	public void setAuthorUser(DefaultUserEntity user) {
		this.authorUser = user;
	}

	@Override
	public void setRecipientUser(DefaultUserEntity user) {
		this.recipientUser = user;
	}

	@Override
	public Date getTimeSent() {
		return this.timeSent;
	}

	@Override
	public String getContents() {
		return this.contents;
	}

	@Override
	public Integer getChatId() {
		return chatId;
	}

	@Override
	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(chatId, authorUser, recipientUser, timeSent, contents);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof DefaultChatMessageEntity)) {
			return false;
		}
		DefaultChatMessageEntity other = (DefaultChatMessageEntity) obj;
		return Objects.equals(chatId, other.chatId) && Objects.equals(authorUser, other.authorUser)
				&& Objects.equals(recipientUser, other.recipientUser) && Objects.equals(timeSent, other.timeSent)
				&& Objects.equals(contents, other.contents);
	}

	@Override
	public String toString() {
		return "DefaultChatMessageEntity [id=" + id + ", chatId=" + chatId + ", authorUser=" + authorUser
				+ ", recipientUser=" + recipientUser + ", timeSent=" + timeSent + ", contents=" + contents + "]";
	}

}
