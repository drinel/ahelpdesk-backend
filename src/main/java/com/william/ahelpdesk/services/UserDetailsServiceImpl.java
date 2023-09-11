package com.william.ahelpdesk.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.william.ahelpdesk.domain.Pessoa;
import com.william.ahelpdesk.repositories.PessoaRepository;
import com.william.ahelpdesk.security.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private PessoaRepository repo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Pessoa> user = repo.findByEmail(email);
		if(user.isPresent()) {
			return new UserSS(user.get().getId(), user.get().getEmail(), user.get().getSenha(), user.get().getPerfis());
		}
		throw new UsernameNotFoundException(email);
		
	}

}
