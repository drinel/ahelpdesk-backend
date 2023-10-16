package com.william.ahelpdesk.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.william.ahelpdesk.domain.Cliente;
import com.william.ahelpdesk.domain.Pessoa;
import com.william.ahelpdesk.domain.dtos.ClienteDTO;
import com.william.ahelpdesk.repositories.ClienteRepository;
import com.william.ahelpdesk.repositories.PessoaRepository;
import com.william.ahelpdesk.services.exceptions.DataIntegrityViolationException;
import com.william.ahelpdesk.services.exceptions.ObjectNotFoundException;

import javax.validation.Valid;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repository;
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
	}

	public List<Cliente> findAll() {
		
		return repository.findAll();
	}

	public Cliente create(ClienteDTO objDTO) {
		objDTO.setId(null);
		objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		validaPorCpfEEMail(objDTO);
		Cliente	newObj = new Cliente(objDTO);
		return repository.save(newObj);
	}
	
	public Cliente update(Integer id, @Valid ClienteDTO objDTO) {
		objDTO.setId(id);
		Cliente oldObj = findById(id);
		if(!objDTO.getSenha().equals(oldObj.getSenha())) {
			objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		}
		validaPorCpfEEMail(objDTO);
		oldObj = new Cliente(objDTO);
		
		return repository.save(oldObj);
	}

	
	public void delete(Integer id) {
       Cliente obj = findById(id);
       if(obj.getChamados().size() > 0) {
    	   throw new DataIntegrityViolationException("Cliente possui ordens de serviço e não pode ser deletado!");
       }
    	   repository.deleteById(id);
  
	}


	private void validaPorCpfEEMail(ClienteDTO objDTO) {
      Optional<Pessoa>	obj = pessoaRepository.findByCpf(objDTO.getCpf());
      if(obj.isPresent() && obj.get().getId() != objDTO.getId()) {
    	  throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
      }
      
      obj = pessoaRepository.findByEmail(objDTO.getEmail());
      if(obj.isPresent() && obj.get().getId() != objDTO.getId()) {
    	  throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
      } 
	}

	
	

}
