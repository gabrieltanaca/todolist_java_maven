package br.com.gabrieltanaca.todolist.interfaces;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gabrieltanaca.todolist.models.TaskModel;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {

}
