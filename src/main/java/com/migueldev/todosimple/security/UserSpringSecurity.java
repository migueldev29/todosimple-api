package com.migueldev.todosimple.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.migueldev.todosimple.models.enums.ProfileEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
//Objeto de usuário logado.
public class UserSpringSecurity implements UserDetails {
    
    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    
    public UserSpringSecurity(Long id, String username, String password, Set<ProfileEnum> profileEnums) {
        this.id = id;
        this.username = username;
        this.password = password;

        /*
         * A collection de authorities irá receber itens de SimpleGrantedAuthority Contendo a descrição do ProfileEnum por ex: ROLE_USER e ROLE_ADMIN
        */
        this.authorities = profileEnums.stream().
                                map(x -> new SimpleGrantedAuthority(x.getDescription())).
                                collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //Identifica a existência de Perfil associado ao usuário "hasRole", Existe ROLE?
    public boolean hasRole(ProfileEnum profileEnum) {
        //Verifica nos itens da lista de Authorities se contém um perfil ROLE_USER ou ROLE_ADMIN
        return getAuthorities().contains(new SimpleGrantedAuthority(profileEnum.getDescription()));
    }
    
}