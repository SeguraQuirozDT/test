package com.mifel.test.entity.auth;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "username", nullable = false, unique = true, length = 500)
	private String username;

	@Column(name = "email", nullable = false, unique = true, length = 500)
	private String email;

	@Column(name = "password", nullable = false, length = 500)
	private String password;

	@Column(name = "first_name", nullable = true, length = 500)
	private String firstName;

	@Column(name = "last_name", nullable = true, length = 500)
	private String lastName;

	@Default
	@OneToMany(mappedBy = "user", targetEntity = Role.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Role> roles = new HashSet<>();

	@CreatedDate
	@Column(name = "created", nullable = true)
	private LocalDateTime created;

	@LastModifiedDate
	@Column(name = "modified", nullable = true)
	private LocalDateTime modified;
}
