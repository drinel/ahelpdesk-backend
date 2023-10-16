package com.william.ahelpdesk.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.william.ahelpdesk.domain.Pessoa;
import com.william.ahelpdesk.domain.Tecnico;
import com.william.ahelpdesk.domain.dtos.TecnicoDTO;
import com.william.ahelpdesk.repositories.PessoaRepository;
import com.william.ahelpdesk.repositories.TecnicoRepository;
import com.william.ahelpdesk.services.exceptions.DataIntegrityViolationException;
import com.william.ahelpdesk.services.exceptions.ObjectNotFoundException;

import javax.validation.Valid;

@Service
public class TecnicoService {
	
	@Autowired
	private TecnicoRepository repository;
	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	public Tecnico findById(Integer id) {
		Optional<Tecnico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
	}

	public List<Tecnico> findAll() {
		
		return repository.findAll();
	}

	public Tecnico create(TecnicoDTO objDTO) {
		objDTO.setId(null);
		objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		validaPorCpfEEMail(objDTO);
		Tecnico	newObj = new Tecnico(objDTO);
		return repository.save(newObj);
	}
	
	public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {
		objDTO.setId(id);
		Tecnico oldObj = findById(id);
		
		if(!objDTO.getSenha().equals(oldObj.getSenha())) {
			objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		}
		validaPorCpfEEMail(objDTO);
		oldObj = new Tecnico(objDTO);
		
		return repository.save(oldObj);
	}

	
	public void delete(Integer id) {
       Tecnico obj = findById(id);
       if(obj.getChamados().size() > 0) {
    	   throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser deletado!");
       }
    	   repository.deleteById(id);
  
	}


	private void validaPorCpfEEMail(TecnicoDTO objDTO) {
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
