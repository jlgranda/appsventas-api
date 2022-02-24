package org.jlgranda.appsventas.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static java.util.Arrays.asList;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.h2.console.enabled:false}")
    private boolean h2ConsoleEnabled;

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if (h2ConsoleEnabled)
            http.authorizeRequests()
                .antMatchers("/h2-console", "/h2-console/**").permitAll()
                .and()
                .headers().frameOptions().sameOrigin();

//        http.csrf().disable()
//            .cors()
//            .and()
//            .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//            .and()
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//            .authorizeRequests()
//            .antMatchers(HttpMethod.OPTIONS).permitAll()
//            .antMatchers(HttpMethod.GET, "/ping").permitAll()
//                
//            .antMatchers(HttpMethod.GET, "/media").permitAll()
//            .antMatchers(HttpMethod.GET, "/media/**").permitAll()
//            
//            .antMatchers(HttpMethod.POST, "/users/login").permitAll()
//            .antMatchers(HttpMethod.GET, "/profile/**").permitAll()
//                
//                
//            .antMatchers(HttpMethod.GET, "/perfiles").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.GET, "/perfiles/**").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.GET, "/tareas").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.GET, "/tareas/**").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.GET, "/users").hasAnyAuthority( "ROL_ADMINISTRADOR_DE_DATOS", "ROL_DISENADOR_DE_FORMULARIO", "ROL_SUPERVISOR_DE_FORMULARIO", "ROL_SUPERVISOR" )
//            .antMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority( "ROL_ADMINISTRADOR_DE_DATOS", "ROL_DISENADOR_DE_FORMULARIO", "ROL_SUPERVISOR_DE_FORMULARIO", "ROL_SUPERVISOR" )
//            .antMatchers(HttpMethod.GET, "/auditoria").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL", "ROL_ADMINISTRADOR_DE_DATOS", "ROL_SUPERVISOR" )
//            .antMatchers(HttpMethod.GET, "/auditoria/**").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL", "ROL_ADMINISTRADOR_DE_DATOS", "ROL_SUPERVISOR" )
//                
//                
//                
//            .antMatchers(HttpMethod.POST, "/profile/**").permitAll()
//            .antMatchers(HttpMethod.POST, "/perfiles").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.POST, "/perfiles/**").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.POST, "/tareas").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.POST, "/tareas/**").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.POST, "/users").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.POST, "/users/**").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            
//                
//            .antMatchers(HttpMethod.PUT, "/profile/**").permitAll()
//            .antMatchers(HttpMethod.PUT, "/perfiles").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.PUT, "/perfiles/**").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.PUT, "/tareas").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.PUT, "/tareas/**").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.PUT, "/users").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.PUT, "/users/**").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            
//            .antMatchers(HttpMethod.DELETE, "/profile/**").permitAll()
//            .antMatchers(HttpMethod.DELETE, "/perfiles").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.DELETE, "/perfiles/**").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.DELETE, "/tareas").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.DELETE, "/tareas/**").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.DELETE, "/users").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
//            .antMatchers(HttpMethod.DELETE, "/users/**").hasAnyAuthority( "ROL_ADMINISTRADOR_FUNCIONAL" )
            
//            .anyRequest().authenticated();
            
        //Sin control de acceso por el filtro
        http.csrf().disable()
            .cors()
            .and()
            .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            .anyRequest().permitAll();
        
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(asList("*"));
        configuration.setAllowedMethods(asList("HEAD",
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
//        configuration.setAllowCredentials(true);
        configuration.setAllowCredentials(false);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        //configuration.setAllowedHeaders(asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowedHeaders(asList("Content-Type", "X-Requested-With", "Authorization", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
