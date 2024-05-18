package eci.arsw.dto;

import eci.arsw.model.User;
import lombok.Data;

@Data
public class UserResponseDto extends CommonApiResponse {

	private User user;

}
