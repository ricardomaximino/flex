package es.brasatech.flex.data;

import es.brasatech.flex.shared.DataService;
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
public class DataController {

    private final DataService service;

    @PostMapping
    public ResponseEntity<Data> create(@RequestBody Map<String, Object> data) {
        Data saved = service.save(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Data> update(
            @PathVariable String id,
            @RequestBody Map<String, Object> data) {
        Data updated = service.update(id, data);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<Data>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Data> getById(@PathVariable String id) {
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
    public ResponseEntity<List<Data>> search(@RequestBody Map<String, Object> searchCriteria) {
        List<Data> results = service.simpleSearch(searchCriteria);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/advanced-search")
    public ResponseEntity<List<Data>> advancedSearch(@RequestBody Map<String, SearchCriteria> searchCriteria) {
        List<Data> results = service.advancedSearch(searchCriteria);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/search/paginated")
    public ResponseEntity<Page<Data>> basicSearchWithPaging(
            @RequestBody Map<String, Object> searchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy) {

        Page<Data> results = service.simpleSearchWithPaging(searchCriteria, page, size, sortBy);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/advanced-search/paginated")
    public ResponseEntity<Page<Data>> advancedSearchWithPaging(
            @RequestBody Map<String, SearchCriteria> searchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy) {

        Page<Data> results = service.advancedSearchWithPaging(searchCriteria, page, size, sortBy);
        return ResponseEntity.ok(results);
    }
}
