package com.migueldev.todosimple.configs;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.migueldev.todosimple.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;
    
    private static final String[] PUBLIC_MATCHERS = { //Rotas publicas padrão
        "/"
    };

    private static final String[] PUBLIC_MATCHERS_POST = { //Rotas publicas para realizar o POST(Acesso ao post permitido pra todos)
        "/user", //Para criar usuário
        "/login" //Para logar no sistema
    };

    @Bean //Utilizar a anotação @Bean para que o Spring consiga injetar uma classe de fora do projeto, sendo de uma biblioteca que eu importei
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // FILTROS DE AUTENTICAÇÃO

        http.cors().and().csrf().disable(); //Desativando proteção de CORS para evitar complexidades para os testes.

        //AuthenticationManager
        AuthenticationManagerBuilder authenticationManagerBuilder = http
            .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(this.userDetailsService)
            .passwordEncoder(bCryptPasswordEncoder());
        this.authenticationManager = authenticationManagerBuilder.build();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll() //Autoriza quaqluer requisição de POST informada no PUBLIC_MATCHERS_POST
                .antMatchers(PUBLIC_MATCHERS).permitAll() //Autoriza quaqluer requisição informada no PUBLIC_MATCHERS
                .anyRequest().authenticated(); //Para qualquer outra requisição dos endpoints só permitirá se tiver autenticado o usuário, feito o login.

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //Não cria sessão do lado do servidor.

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
            configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));

            final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);

            return source;
    }
     
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
