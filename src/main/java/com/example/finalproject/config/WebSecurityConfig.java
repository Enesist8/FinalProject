package com.example.finalproject.config;

import com.example.finalproject.models.ModelUser;
import com.example.finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            ModelUser user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("Такой пользователь не существует");
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.isActive(),
                    true,
                    true,
                    true,
                    user.getRoles()
            );
        }).passwordEncoder(passwordEncoder());
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/login", "/registration", "/error").permitAll()

                        .anyRequest().authenticated() //This is crucial for proper auth
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/") // Or your desired default URL
                        .permitAll()

                )
                .logout(logout -> logout.permitAll())
        // .csrf().disable(); <---  REMOVE this line in production; CSRF is essential for security.
        ;
        return http.build();
    }
}