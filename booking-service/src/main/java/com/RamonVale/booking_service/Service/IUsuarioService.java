package com.RamonVale.booking_service.Service;

import com.RamonVale.booking_service.Dto.Usuario.UsuarioRequest;
import com.RamonVale.booking_service.Dto.Usuario.UsuarioResponse;
import com.RamonVale.booking_service.Dto.Usuario.UsuarioUpdate;
import java.util.List;


public interface IUsuarioService {
  List<UsuarioResponse> findAll();
  UsuarioResponse findById(Long id);
  UsuarioResponse update(Long id, UsuarioUpdate usuario);
  UsuarioResponse create(UsuarioRequest usuarioRequest);

}
