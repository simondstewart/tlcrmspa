package org.crm.tlcrmspa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.crm.tlcrmspa.domain.Authority;
import org.crm.tlcrmspa.domain.User;
import org.crm.tlcrmspa.repository.AuthorityRepository;
import org.crm.tlcrmspa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BootstrapDataLoad {

	private final UserRepository userRepository;
	
	private final AuthorityRepository authorityRepository;
	
	@Autowired
	public BootstrapDataLoad(UserRepository userRepository, AuthorityRepository authorityRepository) {
		super();
		this.userRepository = userRepository;
		this.authorityRepository = authorityRepository;
	}
	
	@PostConstruct
	public void dataLoad() {
		authorityRepository.save(createAuthorities());
		userRepository.save(createUsers());
	}
/**
 *
id;login;password_hash;first_name;last_name;email;activated;lang_key;created_by
1;system;$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG;System;System;system@localhost;true;en;system
2;anonymousUser;$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO;Anonymous;User;anonymous@localhost;true;en;system
3;admin;$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC;Administrator;Administrator;admin@localhost;true;en;system
4;user;$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K;User;User;user@localhost;true;en;system

 * @return
 */
	private User createUserFromDataString(String dataString, Authority... authority) {
		User user = new User();
		String[] columns = dataString.split(";");
		user.setId(Long.parseLong(columns[0]));
		user.setLogin(columns[1]);
		user.setPassword(columns[2]);
		user.setFirstName(columns[3]);
		user.setLastName(columns[4]);
		user.setEmail(columns[5]);
		user.setActivated(Boolean.parseBoolean(columns[6]));
		user.setLangKey(columns[7]);
		user.setCreatedBy(columns[8]);
		user.getAuthorities().addAll(Arrays.asList(authority));
		return user;
	}
	
	private Iterable<User> createUsers() {
		Authority user = authorityRepository.findOne("ROLE_USER");
		Authority admin = authorityRepository.findOne("ROLE_ADMIN");
		List<User> users = new ArrayList<>();
		users.add(createUserFromDataString(
				"1;system;$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG;System;System;system@localhost;true;en;system", 
				user, admin));
		users.add(createUserFromDataString(
				"2;anonymousUser;$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO;Anonymous;User;anonymous@localhost;true;en;system"
				));
		users.add(createUserFromDataString(
				"3;admin;$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC;Administrator;Administrator;admin@localhost;true;en;system", 
				user, admin));
		users.add(createUserFromDataString(
				"4;user;$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K;User;User;user@localhost;true;en;system", 
				user));
		return users;
	}

	private Iterable<Authority> createAuthorities() {
		List<Authority> authorities = new ArrayList<>();
		Authority admin = new Authority();
		admin.setName("ROLE_ADMIN");
		authorities.add(admin);
		
		Authority user = new Authority();
		user.setName("ROLE_USER");
		authorities.add(user);
		
		return authorities;
	}
	
}
