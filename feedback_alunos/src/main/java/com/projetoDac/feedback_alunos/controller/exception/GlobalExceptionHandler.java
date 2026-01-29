package com.projetoDac.feedback_alunos.controller.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.projetoDac.feedback_alunos.exception.AdminJaExisteException;
import com.projetoDac.feedback_alunos.exception.AdministradorNotFoundException;
import com.projetoDac.feedback_alunos.exception.AlunoNotFoundException;
import com.projetoDac.feedback_alunos.exception.ApenasAlunosPodeAvaliarException;
import com.projetoDac.feedback_alunos.exception.AvaliacaoNotFoundException;
import com.projetoDac.feedback_alunos.exception.CursoJaExisteException;
import com.projetoDac.feedback_alunos.exception.CursoNotFoundException;
import com.projetoDac.feedback_alunos.exception.DisciplinaNotFoundException;
import com.projetoDac.feedback_alunos.exception.PerfilNotFoundException;
import com.projetoDac.feedback_alunos.exception.UsuarioNotFoundException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AvaliacaoNotFoundException.class)
    public ResponseEntity<String> handleAvaliacaoNotFoundException(AvaliacaoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<String> handleUsuarioNotFoundException(UsuarioNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DisciplinaNotFoundException.class)
    public ResponseEntity<String> handleDisciplinaNotFoundException(DisciplinaNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AdministradorNotFoundException.class)
    public ResponseEntity<String> handleAdministradorNotFoundException(AdministradorNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AlunoNotFoundException.class)
    public ResponseEntity<String> handleAlunoNotFoundException(AlunoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CursoNotFoundException.class)
    public ResponseEntity<String> handleCursoNotFoundException(CursoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(PerfilNotFoundException.class)
    public ResponseEntity<String> handlePerfilNotFoundException(PerfilNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AdminJaExisteException.class)
    public ResponseEntity<String> handleAdminJaExisteException(AdminJaExisteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(CursoJaExisteException.class)
    public ResponseEntity<String> handleCursoJaExisteException(CursoJaExisteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ApenasAlunosPodeAvaliarException.class)
    public ResponseEntity<String> handleApenasAlunosPodeAvaliarException(ApenasAlunosPodeAvaliarException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder("Dados inválidos: ");
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errors.append(error.getDefaultMessage()).append(". ");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        StringBuilder errors = new StringBuilder("Dados inválidos: ");
        ex.getConstraintViolations().forEach(violation -> {
            errors.append(violation.getMessage()).append(". ");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
               .body("Formato de dados inválido. Verifique os tipos e valores dos campos.");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
               .body("Operação não permitida. Verifique se você está usando o método correto (GET, POST, PUT, DELETE).");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
               .body("Campo obrigatório não informado: " + ex.getParameterName() + ". Por favor, preencha este campo.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        String rootMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() : "";
        
        if ((message != null && (message.contains("matricula") || message.contains("tb_usuario_matricula_key"))) ||
            (rootMessage != null && (rootMessage.contains("matricula") || rootMessage.contains("tb_usuario_matricula_key")))) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                   .body("Esta matrícula já está em uso. Por favor, escolha uma matrícula diferente.");
        }
        
        if (message != null && (message.contains("foreign key") || message.contains("constraint"))) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                   .body("Não é possível excluir este item pois ele está sendo usado por outros registros. Remova as dependências primeiro.");
        }
        
        return ResponseEntity.status(HttpStatus.CONFLICT)
               .body("Dados duplicados encontrados. Verifique se as informações já não existem no sistema.");
    }


}