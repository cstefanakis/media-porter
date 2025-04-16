package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.dtos.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
