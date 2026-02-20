package pp.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pp.userservice.entity.Role;
import pp.userservice.repository.RoleRepository;
import pp.userservice.service.RoleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {

    private final RoleService roleService;

    @GetMapping
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/{roleValue}")
    public ResponseEntity<Role> getRole(@PathVariable String roleValue) {
        return ResponseEntity.ok().body(roleService.findRoleByValue(roleValue));
    }
}
