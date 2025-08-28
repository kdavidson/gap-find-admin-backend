package gov.cabinetoffice.gap.adminbackend.security;

import gov.cabinetoffice.gap.adminbackend.config.JwtTokenFilterConfig;
import gov.cabinetoffice.gap.adminbackend.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableAspectJAutoProxy(proxyTargetClass = true)
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private static final String SUBMISSIONS_SUBMISSION_ID = "/submissions/{submissionId:";
    private static final String GRANT_EXPORT_EXPORT_ID = "/grant-export/{exportId:";
    private static final String EXPORT_BATCH_BATCH_EXPORT_ID = "}/export-batch/{batchExportId:";
    private static final String GRANT_ADVERT_LAMBDA_GRANT_ADVERT_ID = "/grant-advert/lambda/{grantAdvertId:";

    private final JwtTokenFilter jwtTokenFilter;

    private static final String UUID_REGEX_STRING = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";

    public WebSecurityConfig(final UserService userService, final JwtTokenFilterConfig jwtTokenFilterConfig) {
        this.jwtTokenFilter = new JwtTokenFilter(userService, jwtTokenFilterConfig);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //if you add a path which is hit by the lambda, remember to update also the paths in gov/cabinetoffice/gap/adminbackend/config/LambdasInterceptor.java
        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login",
                                "/health",
                                "/emails/sendLambdaConfirmationEmail",
                                "/users/validateAdminSession",
                                SUBMISSIONS_SUBMISSION_ID + UUID_REGEX_STRING + EXPORT_BATCH_BATCH_EXPORT_ID + UUID_REGEX_STRING + "}/submission",
                                "/submissions/*/export-batch/*/status",
                                SUBMISSIONS_SUBMISSION_ID + UUID_REGEX_STRING + EXPORT_BATCH_BATCH_EXPORT_ID + UUID_REGEX_STRING + "}/s3-object-key",
                                GRANT_EXPORT_EXPORT_ID + UUID_REGEX_STRING + "}/outstandingCount",
                                GRANT_EXPORT_EXPORT_ID + UUID_REGEX_STRING + "}/failedCount",
                                GRANT_EXPORT_EXPORT_ID + UUID_REGEX_STRING + "}/remainingCount",
                                GRANT_EXPORT_EXPORT_ID + UUID_REGEX_STRING + "}/completed",
                                GRANT_EXPORT_EXPORT_ID + UUID_REGEX_STRING + "}/batch/status",
                                GRANT_EXPORT_EXPORT_ID + UUID_REGEX_STRING + "}/batch/s3-object-key",
                                GRANT_ADVERT_LAMBDA_GRANT_ADVERT_ID + UUID_REGEX_STRING + "}/publish",
                                GRANT_ADVERT_LAMBDA_GRANT_ADVERT_ID + UUID_REGEX_STRING + "}/unpublish",
                                "/users/migrate",
                                "/users/delete",
                                "/users/tech-support-user/**",
                                "/users/admin-user/**",
                                "/users/funding-organisation",
                                "/application-forms/lambda/**",
                                "/feedback/add"
                        )
                        .permitAll()
                        .requestMatchers("/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/webjars/**")
                        .permitAll()
                        .requestMatchers("/spotlight-submissions/{spotlightSubmissionId:" + UUID_REGEX_STRING + "}")
                        .permitAll()
                        .requestMatchers("/spotlight-batch/status/**",
                                "/spotlight-batch",
                                "/spotlight-batch/{spotlightBatchId" + UUID_REGEX_STRING
                                        + "}/add-spotlight-submission/**",
                                "/spotlight-batch/send-to-spotlight")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> 
                        exceptionHandling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

        http.addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
