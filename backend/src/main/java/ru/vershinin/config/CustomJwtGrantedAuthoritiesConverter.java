package ru.vershinin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final Logger logger = LoggerFactory.getLogger(CustomJwtGrantedAuthoritiesConverter.class);
    private static final String ROLES_CLAIM = "realm_access";
    private static final String ROLES_KEY = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        Map<String, Object> realmAccess = jwt.getClaim(ROLES_CLAIM);
        if (realmAccess != null && realmAccess.containsKey(ROLES_KEY)) {
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get(ROLES_KEY);
            if (roles != null) {
                for (String role : roles) {
                    String roleWithPrefix = ROLE_PREFIX + role;
                    authorities.add(new SimpleGrantedAuthority(roleWithPrefix));
                    logger.debug("Added authority: {}", roleWithPrefix);
                }
            }
        } else {
            logger.debug("No roles found in JWT under '{}.{}'", ROLES_CLAIM, ROLES_KEY);
        }

        logger.debug("Extracted authorities: {}", authorities);
        return authorities;
    }
}
