package es.brasatech.flex.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody Map<String, Object> data) {
        User saved = service.save(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(
            @PathVariable String id,
            @RequestBody Map<String, Object> data) {
        User updated = service.update(id, data);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<User>> search(@RequestBody Map<String, Object> searchCriteria) {
        List<User> results = service.simpleSearch(searchCriteria);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/advanced-search")
    public ResponseEntity<List<User>> advancedSearch(@RequestBody Map<String, SearchCriteria> searchCriteria) {
        List<User> results = service.advancedSearch(searchCriteria);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/search/paginated")
    public ResponseEntity<Page<User>> basicSearchWithPaging(
            @RequestBody Map<String, Object> searchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy) {

        Page<User> results = service.simpleSearchWithPaging(searchCriteria, page, size, sortBy);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/advanced-search/paginated")
    public ResponseEntity<Page<User>> advancedSearchWithPaging(
            @RequestBody Map<String, SearchCriteria> searchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy) {

        Page<User> results = service.advancedSearchWithPaging(searchCriteria, page, size, sortBy);
        return ResponseEntity.ok(results);
    }
}
