package eci.arsw.controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eci.arsw.dto.CommonApiResponse;
import eci.arsw.dto.UserLoginRequest;
import eci.arsw.dto.UserResponseDto;
import eci.arsw.model.User;
import eci.arsw.repository.UsuarioRepository;

@RestController
@RequestMapping("api/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

	@Autowired
	private UsuarioRepository userDao;

	@PostMapping("register")
	public ResponseEntity<CommonApiResponse> registerUser(@RequestBody User user) {

		CommonApiResponse response = new CommonApiResponse();

		if (user == null) {
			response.setResponseMessage("bad request - missing request body");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (user.getUsername() == null || user.getPassword() == null) {
			response.setResponseMessage("bad request - missing input");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User existingUser = this.userDao.findByUsername(user.getUsername());

		if (existingUser != null) {
			response.setResponseMessage("User already registered with this Username!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		user.setWalletAmount(BigDecimal.ZERO);
		User savedUser = this.userDao.save(user);

		if (savedUser == null) {
			response.setResponseMessage("failed to register the user");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			response.setResponseMessage("User Registered successful!!!");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
		}

	}

	@PostMapping("login")
	public ResponseEntity<UserResponseDto> login(@RequestBody UserLoginRequest userLoginRequest) {
		UserResponseDto response = new UserResponseDto();

		if (userLoginRequest == null) {
			response.setResponseMessage("bad request - missing request body");
			response.setSuccess(false);

			return new ResponseEntity<UserResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		if (userLoginRequest.getUsername() == null || userLoginRequest.getPassword() == null) {
			response.setResponseMessage("bad request - missing input");
			response.setSuccess(false);

			return new ResponseEntity<UserResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		User existingUser = this.userDao.findByUsername(userLoginRequest.getUsername());

		if (existingUser == null) {
			response.setResponseMessage("User not registed with this Username!!!");
			response.setSuccess(false);

			return new ResponseEntity<UserResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		existingUser = this.userDao.findByUsernameAndPassword(userLoginRequest.getUsername(),
				userLoginRequest.getPassword());

		if (existingUser == null) {
			response.setResponseMessage("Invalid username or password!!!");
			response.setSuccess(false);

			return new ResponseEntity<UserResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			response.setUser(existingUser);
			response.setResponseMessage("User Logged in successful!!!");
			response.setSuccess(true);

			return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
		}
	}

	@GetMapping("/fetch/user-id")
	public ResponseEntity<UserResponseDto> fetchUserById(@RequestParam("userId") int userId) {
		UserResponseDto response = new UserResponseDto();

		if (userId == 0) {
			response.setResponseMessage("missing user id");
			response.setSuccess(false);

			return new ResponseEntity<UserResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Optional<User> optional = userDao.findById(userId);

		if (optional.isEmpty()) {
			response.setResponseMessage("user not found");
			response.setSuccess(false);

			return new ResponseEntity<UserResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		User user = optional.get();

		if (user == null) {
			response.setResponseMessage("user not found");
			response.setSuccess(false);

			return new ResponseEntity<UserResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		response.setUser(user);
		response.setResponseMessage("User fetched successful");
		response.setSuccess(true);

		return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);

	}

	// in request body send id (user id) and walletAmount
	// id, walletAmount
	@PutMapping("update/wallet")
	public ResponseEntity<UserResponseDto> updateUserWallet(@RequestBody User user) {
		UserResponseDto response = new UserResponseDto();

		if (user == null || user.getId() == 0) {
			response.setResponseMessage("missing user id");
			response.setSuccess(false);

			return new ResponseEntity<UserResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Optional<User> optional = userDao.findById(user.getId());

		if (optional.isEmpty()) {
			response.setResponseMessage("user not found");
			response.setSuccess(false);

			return new ResponseEntity<UserResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		User userDetail = optional.get();

		if (userDetail == null) {
			response.setResponseMessage("user not found");
			response.setSuccess(false);

			return new ResponseEntity<UserResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		userDetail.setWalletAmount(userDetail.getWalletAmount().add(user.getWalletAmount()));
		User updatedUser = this.userDao.save(userDetail);

		if (updatedUser == null) {
			response.setResponseMessage("Failed to update the Wallet!!!");
			response.setSuccess(false);

			return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
		} else {
			response.setUser(updatedUser);
			response.setResponseMessage("User Wallet updated successful");
			response.setSuccess(true);

			return new ResponseEntity<UserResponseDto>(response, HttpStatus.OK);
		}

	}

}
