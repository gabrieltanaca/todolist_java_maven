package br.com.gabrieltanaca.todolist.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabrieltanaca.todolist.interfaces.ITaskRepository;
import br.com.gabrieltanaca.todolist.interfaces.IUserRepository;
import br.com.gabrieltanaca.todolist.models.TaskModel;

@RestController
@RequestMapping("/Tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("")
  public ResponseEntity CreateTask(@RequestBody TaskModel taskmodel) {
    var task = this.taskRepository.save(taskmodel);

    return ResponseEntity.status(HttpStatus.CREATED).body(taskmodel);
  }
}
