package br.com.gabrieltanaca.todolist.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabrieltanaca.todolist.interfaces.ITaskRepository;
import br.com.gabrieltanaca.todolist.interfaces.IUserRepository;
import br.com.gabrieltanaca.todolist.models.TaskModel;
import br.com.gabrieltanaca.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("")
  public ResponseEntity CreateTask(@RequestBody TaskModel taskmodel, HttpServletRequest servletRequest) {
    var idUser = servletRequest.getAttribute("idUser");
    taskmodel.setIdUser((UUID) idUser);

    var currentDate = LocalDateTime.now();

    if (currentDate.isAfter(taskmodel.getStartAt()) || currentDate.isAfter(taskmodel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("A data de início/término deve ser maior do que a data atual.");
    }

    if (taskmodel.getStartAt().isAfter(taskmodel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("A data de início deve ser menor do que a data de término.");
    }

    var task = this.taskRepository.save(taskmodel);
    return ResponseEntity.status(HttpStatus.CREATED).body(task);
  }

  @GetMapping("")
  public List<TaskModel> list(HttpServletRequest servletRequest) {
    var idUser = servletRequest.getAttribute("idUser");
    var tasks = this.taskRepository.findByIdUser((UUID) idUser);

    return tasks;
  }

  @PutMapping("/{id}")
  public ResponseEntity list(@RequestBody TaskModel taskModel, HttpServletRequest servletRequest,
      @PathVariable UUID id) {
    var task = this.taskRepository.findById(id).orElse(null);

    if (task == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada.");
    }

    var idUser = servletRequest.getAttribute("idUser");

    if (!task.getIdUser().equals(idUser)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para alterar essa tarefa.");
    }

    Utils.copyNonNullProperties(taskModel, task);
    var taskUpdated = this.taskRepository.save(task);

    taskModel.setIdUser((UUID) idUser);
    taskModel.setId(id);

    return ResponseEntity.ok().body(this.taskRepository.save(taskUpdated));
  }
}
