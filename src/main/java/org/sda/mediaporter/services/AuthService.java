package org.sda.mediaporter.services;

import org.sda.mediaporter.dtos.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
