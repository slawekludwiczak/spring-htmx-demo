package pl.javastart.movieclub.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.javastart.movieclub.web.htmx.Htmx;

import java.io.IOException;

@Configuration
public class CustomSecurityConfig {
    public static final String USER_ROLE = "USER";
    public static final String EDITOR_ROLE = "EDITOR";
    public static final String ADMIN_ROLE = "ADMIN";
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/ocen-film").authenticated()
                        .requestMatchers("/admin/**").hasAnyRole(EDITOR_ROLE, ADMIN_ROLE)
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/login").permitAll()
                        .failureHandler(new Enhanced401AuthenticationFailuereHandler())
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout/**", HttpMethod.GET.name()))
                        .logoutSuccessUrl("/login?logout").permitAll()
                );
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(config -> config.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/img/**",
                "/scripts/**",
                "/styles/**"
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private static class Enhanced401AuthenticationFailuereHandler extends SimpleUrlAuthenticationFailureHandler {
        public Enhanced401AuthenticationFailuereHandler() {
            super("/login?error");
        }

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            boolean isHtmxRequest = request.getHeader(Htmx.HEADER_HX_REQUEST) != null;
            if (isHtmxRequest) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            } else {
                super.onAuthenticationFailure(request, response, exception);
            }
        }
    }
}
