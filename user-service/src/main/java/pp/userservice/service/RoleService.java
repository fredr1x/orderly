package pp.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pp.userservice.entity.Role;
import pp.userservice.exception.NotFoundException;
import pp.userservice.repository.RoleRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    @Cacheable(value = "roles", key = "#roleValue")
    public Role findRoleByValue(String roleValue) {
        return roleRepository.findByValue(roleValue)
                .orElseThrow(() -> new NotFoundException("Role with value " + roleValue + " not found"));
    }
}
