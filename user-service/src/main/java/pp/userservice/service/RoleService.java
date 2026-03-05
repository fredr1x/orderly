package pp.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pp.userservice.entity.Role;
import pp.userservice.entity.enums.Value;
import pp.userservice.exception.NotFoundException;
import pp.userservice.repository.RoleRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    @Cacheable(value = "roles", key = "#roleValue")
    public Role findRoleByValue(Value roleValue) {
        return roleRepository.findByValue(roleValue)
                .orElseThrow(() -> new NotFoundException("Role with value " + roleValue + " not found"));
    }

    public void assignRole(Long userId, Long roleId) {
        roleRepository.assignRole(userId, roleId);
    }
}
