package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.dtos.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
