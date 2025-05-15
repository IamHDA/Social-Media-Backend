package com.example.backend.service;

import com.example.backend.dto.MessageDTO;
import com.example.backend.dto.NewMessage;

public interface MessageService {
    MessageDTO sendMessage(NewMessage message);
}
