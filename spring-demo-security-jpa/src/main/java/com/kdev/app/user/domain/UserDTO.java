package com.kdev.app.user.domain;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

public class UserDTO {
	@Data
	public static class Create{
		@Email(message="이메일 형식이 올바르지 않습니다.")
		private String email;
		@NotBlank
		private String nickname;
		@Pattern(regexp="[A-Za-z]{1}[A-Za-z0-9]{7,19}$", message="비밀번호는 8자리 이상 20자리 이하의 영문, 숫자로 구성되어야 합니다.")
		private String password;
		private boolean accountNonExpired;
		private boolean accountNonLocked;
		private boolean credentialsNonExpired;
		private boolean enabled;
		@NotNull
		private List<String> role;
	}
	
	@Data
	public static class Update{
		@NotNull
		private Long id;
		@Email(message="이메일 형식이 올바르지 않습니다.")
		private String email;
		@NotBlank
		private String nickname;
		@Pattern(regexp="[A-Za-z]{1}[A-Za-z0-9]{7,19}$", message="비밀번호는 8자리 이상 20자리 이하의 영문, 숫자로 구성되어야 합니다.")
		private String password;
		private boolean accountNonExpired;
		private boolean accountNonLocked;
		private boolean credentialsNonExpired;
		private boolean enabled;
		@NotNull
		private List<String> role;
	}
}