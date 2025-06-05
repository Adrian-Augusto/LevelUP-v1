package LevelUP.Request.user.register;

import LevelUP.enums.Role;

public record RegisterDTO( String name, String email, String password, Role role) {
}