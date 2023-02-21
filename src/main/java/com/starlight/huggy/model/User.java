package com.starlight.huggy.model;


import java.sql.Timestamp;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

// ORM - Object Relation Mapping

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id // primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int memberId;
	private String email;
	private String password;

	private String username;
	private String role;

	// OAuth 로그인한 사용자
	private String provider; //"GOOGLE", "KAKAO"
	private String providerId; // {google_id}
	@CreationTimestamp
	private Timestamp createDate;
}
