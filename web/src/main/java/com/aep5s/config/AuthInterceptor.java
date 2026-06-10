package com.aep5s.config;

import com.aep5s.models.UsuarioSessao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
            throws Exception {

        UsuarioSessao usuario = (UsuarioSessao) req.getSession().getAttribute("usuarioLogado");

        // Rotas que exigem login de qualquer tipo
        String uri = req.getRequestURI();
        boolean rotaProtegidaAdmin = uri.startsWith("/servidor") || uri.startsWith("/sla");

        if (rotaProtegidaAdmin) {
            if (usuario == null) {
                res.sendRedirect("/login?next=" + uri + "&erro=acesso");
                return false;
            }
            if (!usuario.isAdmin()) {
                res.sendRedirect("/login?erro=permissao");
                return false;
            }
        }

        return true;
    }
}
