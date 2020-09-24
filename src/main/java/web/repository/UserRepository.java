package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import web.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}