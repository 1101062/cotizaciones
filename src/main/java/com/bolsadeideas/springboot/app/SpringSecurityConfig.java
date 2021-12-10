package com.bolsadeideas.springboot.app;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccesHandler;
import com.bolsadeideas.springboot.app.models.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccesHandler succesHandler;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Autowired
    public  void configureGlobal(AuthenticationManagerBuilder builder) throws Exception{

        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/form/registro", "/css/**", "/js/**","/images/**", "/listar**", "/locale", "/api/clientes/**").permitAll()
        /*.antMatchers("/ver/**").hasAnyRole("USER")*/
        /*.antMatchers("/uploads/**").hasAnyRole("USER")*/
        /*.antMatchers("/form/**").hasAnyRole("ADMIN")*/
        /*.antMatchers("/eliminar/**").hasAnyRole("ADMIN")*/
        /*.antMatchers("/factura/**").hasAnyRole("ADMIN")*/
        .anyRequest().authenticated()
        .and()
            .formLogin()
                .successHandler(succesHandler)
                .loginPage("/login")
            .permitAll()
        .and()
        .logout().permitAll()
        .and()
        .exceptionHandling().accessDeniedPage("/error_403");
    }
}
