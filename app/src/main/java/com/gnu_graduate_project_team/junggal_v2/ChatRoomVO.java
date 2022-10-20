package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

public class ChatRoomVO {

	@SerializedName("chatRoom_id")
	private Integer chatRoom_id;

	@SerializedName("share_post_id")
	private Integer share_post_id;

	@SerializedName("share_post_name")
	private String share_post_name;

	@SerializedName("owner_id")
	private String owner_id;

	@SerializedName("owner_name")
	private String owner_name;

	@SerializedName("client_id")
	private String client_id;

	@SerializedName("client_name")
	private String client_name;

	private String user_id;
	private String last_time;

	public ChatRoomVO() {
	}

	public Integer getChatRoom_id() {
		return chatRoom_id;
	}
	public void setChatRoom_id(Integer chatRoom_id) {
		this.chatRoom_id = chatRoom_id;
	}
	public Integer getShare_post_id() {
		return share_post_id;
	}
	public void setShare_post_id(Integer share_post_id) {
		this.share_post_id = share_post_id;
	}
	public String getShare_post_name() {
		return share_post_name;
	}
	public void setShare_post_name(String share_post_name) {
		this.share_post_name = share_post_name;
	}
	public String getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getLast_time() {
		return last_time;
	}

	public void setLast_time(String last_time) {
		this.last_time = last_time;
	}

	public String getOwner_name() {
		return owner_name;
	}

	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	@Override
	public String toString() {
		return "ChatRoomVO{" +
				"chatRoom_id=" + chatRoom_id +
				", share_post_id=" + share_post_id +
				", share_post_name='" + share_post_name + '\'' +
				", owner_id='" + owner_id + '\'' +
				", owner_name='" + owner_name + '\'' +
				", client_id='" + client_id + '\'' +
				", client_name='" + client_name + '\'' +
				", user_id='" + user_id + '\'' +
				", last_time='" + last_time + '\'' +
				'}';
	}
}
