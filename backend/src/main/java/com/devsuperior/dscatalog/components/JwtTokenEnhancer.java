package com.devsuperior.dscatalog.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;

@Component
public class JwtTokenEnhancer implements TokenEnhancer {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		User user = userRepository.findByEmail(authentication.getName());
		
		Map<String, Object> map = new HashMap<>();
		map.put("userFirstName", user.getFirstName());
		map.put("userId", user.getId());
		
		//fazendo downcast para inserir no token
		//preciso do tipo que é subclasse do OAuth2AccessToken porque tem o metodo inserir
		// o tipo OAuth2AccessToken não tem.
		
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
		token.setAdditionalInformation(map);
		
		// poderia retornar o token ( subtipo );
		// mas como é subtipo do outro posso retornar o tipo.
		return accessToken;
	}

}
