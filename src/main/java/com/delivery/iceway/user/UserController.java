package com.delivery.iceway.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.delivery.iceway.domain.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @GetMapping("/users/loginform")
    public String loginform() {
        return "loginform";
    }

    @PostMapping("/users/login")
    public String login(User dto, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getUserName(),
                    dto.getPassword());
            authenticationManager.authenticate(token);
        } catch (BadCredentialsException e) {
            redirectAttributes.addFlashAttribute("error", "아이디 혹은 비밀번호가 틀려요");
            return "redirect:/users/loginform";
        }
        UserDetails userDetails = userService.loadUserByUsername(dto.getUserName());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        return "redirect:/admin/status";
    }
}
