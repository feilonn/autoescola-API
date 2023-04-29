package com.rc.autoescola.controller;

import com.rc.autoescola.DTO.AlunoDTO;
import com.rc.autoescola.DTO.AlunoUpdateDTO;
import com.rc.autoescola.exception.ErrorResponseDetails;
import com.rc.autoescola.exception.NotFoundException;
import com.rc.autoescola.models.Aluno;
import com.rc.autoescola.service.AlunoService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/aluno")
public class AlunoController {
    private final AlunoService alunoService;

    @GetMapping
    public ResponseEntity<List<Aluno>> findAll() {
        return ResponseEntity.ok(alunoService.findAll());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Aluno>> findAllPaginated(Pageable pageable) {
        return ResponseEntity.ok(alunoService.findAllPaginated(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> findById(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.findById(id));
    }

    @GetMapping("/nome")
    public ResponseEntity<List<Aluno>> findById(@RequestParam String nome) {
        return ResponseEntity.ok(alunoService.findByName(nome));
    }

    @PostMapping
    public ResponseEntity<Aluno> save(@Valid @RequestBody AlunoDTO alunoDTO) {
        return new ResponseEntity<>(alunoService.save(alunoDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        alunoService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping
    public ResponseEntity<Void> update(@RequestBody AlunoUpdateDTO alunoUpdateDTO) {
        alunoService.update(alunoUpdateDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    //    @ExceptionHandler(NotFoundException.class)
    //    public ResponseEntity<ErrorResponseDetails> handleAlunoNaotFoundException(NotFoundException ex) {
    //        ErrorResponseDetails error = ErrorResponseDetails.builder()
    //                .status(HttpStatus.NOT_FOUND.value())
    //                .message(ex.getMessage())
    //                .timestamp(LocalDateTime.now())
    //                .build();
    //        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    //    }
}
