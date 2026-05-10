package com.example.habits.api;

import com.example.common.web.security.CurrentUserId;
import com.example.habits.api.dto.CreateHabitRequest;
import com.example.habits.api.dto.HabitResponse;
import com.example.habits.api.dto.UpdateHabitRequest;
import com.example.habits.domain.habit.Habit;
import com.example.habits.domain.habit.HabitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/habits")
public class HabitController {

    private final HabitService service;

    public HabitController(HabitService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HabitResponse create(@CurrentUserId UUID userId,
                                @Valid @RequestBody CreateHabitRequest request) {
        Habit created = service.create(userId, request.name(), request.frequency());
        return HabitResponse.from(created);
    }

    @GetMapping
    public List<HabitResponse> list(@CurrentUserId UUID userId) {
        return service.listOwned(userId).stream()
                .filter(h -> !h.isDeleted())
                .map(HabitResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public HabitResponse get(@CurrentUserId UUID userId, @PathVariable UUID id) {
        return HabitResponse.from(service.getOwned(id, userId));
    }

    @PatchMapping("/{id}")
    public HabitResponse update(@CurrentUserId UUID userId,
                                @PathVariable UUID id,
                                @Valid @RequestBody UpdateHabitRequest request) {
        Habit updated = service.rename(id, userId, request.name());
        return HabitResponse.from(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@CurrentUserId UUID userId, @PathVariable UUID id) {
        service.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}
