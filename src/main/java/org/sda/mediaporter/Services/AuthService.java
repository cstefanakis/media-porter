package org.sda.mediaporter.Services;

import org.sda.mediaporter.dtos.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
