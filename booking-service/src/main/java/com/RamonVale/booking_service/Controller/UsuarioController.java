package com.RamonVale.booking_service.Controller;

import com.RamonVale.booking_service.Dto.Usuario.UsuarioRequest;
import com.RamonVale.booking_service.Dto.Usuario.UsuarioResponse;
import com.RamonVale.booking_service.Dto.Usuario.UsuarioUpdate;
import com.RamonVale.booking_service.Service.IUsuarioService;
import java.util.List;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UsuarioController {
  @Autowired
  private IUsuarioService usuarioServie;

  @GetMapping
  public Page<UsuarioResponse> getUsuarios(@PageableDefault(page=0, size= 10) Pageable pageable){
    return usuarioServie.findAll(pageable);
  }

  @GetMapping("/{id}")
  public UsuarioResponse getUsuario(@PathVariable Long id){
    return usuarioServie.findById(id);
  }

  @PutMapping("/{id}")
  public UsuarioResponse update(@PathVariable Long id, @RequestBody UsuarioUpdate usuario) {
    return usuarioServie.update(id, usuario);
  }

  @PostMapping("/new")
  public UsuarioResponse create(@RequestBody UsuarioRequest usuarioRequest) {
    return usuarioServie.create(usuarioRequest);
  }
}
