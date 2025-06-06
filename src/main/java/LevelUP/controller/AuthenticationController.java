package LevelUP.controller;

import LevelUP.Request.user.AuthenticationDTO;
import LevelUP.Request.user.LoginResponseDTO;
import LevelUP.Request.CreateCouponRequest;
import LevelUP.Request.user.register.RegisterDTO;
import LevelUP.Security.TokenService;
import LevelUP.entity.Coupon;
import LevelUP.entity.User;
import LevelUP.enums.Role;
import LevelUP.repository.UserRepository;
import LevelUP.service.CouponService;
import UserDTO.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:8081")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CouponService couponService;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data, Role User) {
        if (this.repository.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.name(), data.email(), encryptedPassword, User);

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/coupons")
    public ResponseEntity<?> createCoupon(@RequestBody @Valid CreateCouponRequest request) {
        try {
            Coupon coupon = couponService.createCouponForStreamerByEmail(request.getCode(), request.getEmail());
            return ResponseEntity.ok(coupon);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/user")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            UserDTO dto = new UserDTO(user.getName(), user.getEmail(), user.getElo());
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    }



