package com.easy.eats.endereco.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easy.eats.endereco.model.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

    List<Endereco> findAllByCliente_Empresa_Id(Integer empresaId);

    Optional<Endereco> findByIdAndCliente_Empresa_Id(Integer id, Integer empresaId);
}
