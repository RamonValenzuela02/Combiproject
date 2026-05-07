package com.RamonVale.booking_service.Service.Impl;

import com.RamonVale.booking_service.Dto.Usuario.UsuarioRequest;
import com.RamonVale.booking_service.Dto.Usuario.UsuarioResponse;
import com.RamonVale.booking_service.Dto.Usuario.UsuarioUpdate;
import com.RamonVale.booking_service.Repository.UsuarioRepository;
import com.RamonVale.booking_service.Service.IUsuarioService;
import java.util.List;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements IUsuarioService {
  private UsuarioRepository usuarioRepository;

  @Override
  public List<UsuarioResponse> findAll() {
    return List.of();
  }

  @Override
  public UsuarioResponse findById(Long id) {
    return null;
  }

  @Override
  public UsuarioResponse update(Long id, UsuarioUpdate usuario) {
    return null;
  }

  @Override
  public UsuarioResponse create(UsuarioRequest usuarioRequest) {
    return null;
  }
}
