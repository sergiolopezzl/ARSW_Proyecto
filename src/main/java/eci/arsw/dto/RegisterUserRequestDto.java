package eci.arsw.dto;

import org.springframework.beans.BeanUtils;

import eci.arsw.model.User;
import lombok.Data;

@Data
public class RegisterUserRequestDto {
	
	private String firstName;

	private String lastName;

	private String emailId;

	private String password;

	private String phoneNo;

	private String role;
	
	private String street;

	private String city;

	private int pincode;
	
	private int sellerId;   // seller id for delivery person
	
	public static User toUserEntity(RegisterUserRequestDto registerUserRequestDto) {
		User user =new User();
		BeanUtils.copyProperties(registerUserRequestDto, user);		
		return user;
	}
	
}
