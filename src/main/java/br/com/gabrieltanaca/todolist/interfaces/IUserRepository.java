package br.com.gabrieltanaca.todolist.interfaces;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gabrieltanaca.todolist.models.UserModel;
import java.util.List;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {
  UserModel findByUsername(String username);
}
