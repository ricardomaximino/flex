package es.brasatech.flex.data;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
public class DataController {

    private final DataService<Data> service;

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(RawMapper.map(service.save(data)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable String id,
            @RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(RawMapper.map(service.update(id, data)));
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        return ResponseEntity.ok(service.findAll().stream().map(RawMapper::map).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable String id) {
        return service.findById(id)
                .map(RawMapper::map)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> search(@RequestBody Map<String, Object> searchCriteria) {
        return ResponseEntity.ok(service.simpleSearch(searchCriteria).stream().map(RawMapper::map).toList());
    }

    @PostMapping("/advanced-search")
    public ResponseEntity<List<Map<String, Object>>> advancedSearch(@RequestBody Map<String, SearchCriteria> searchCriteria) {
        return ResponseEntity.ok(service.advancedSearch(searchCriteria).stream().map(RawMapper::map).toList());
    }

    @PostMapping("/search/paginated")
    public ResponseEntity<Page<Map<String, Object>>> basicSearchWithPaging(
            @RequestBody Map<String, Object> searchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy) {

        Page<Map<String, Object>> results = service.simpleSearchWithPaging(searchCriteria, page, size, sortBy).map(RawMapper::map);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/advanced-search/paginated")
    public ResponseEntity<Page<Map<String, Object>>> advancedSearchWithPaging(
            @RequestBody Map<String, SearchCriteria> searchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy) {

        Page<Map<String, Object>> results = service.advancedSearchWithPaging(searchCriteria, page, size, sortBy).map(RawMapper::map);
        return ResponseEntity.ok(results);
    }
}
