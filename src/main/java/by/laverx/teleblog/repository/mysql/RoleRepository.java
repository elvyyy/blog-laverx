package by.laverx.teleblog.repository.mysql;

import by.laverx.teleblog.domain.mysql.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String roleName);
}
