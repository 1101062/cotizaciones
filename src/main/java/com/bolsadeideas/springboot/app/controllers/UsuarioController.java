package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.models.entity.Authority;
import com.bolsadeideas.springboot.app.models.entity.Gestor;
import com.bolsadeideas.springboot.app.models.entity.User;
import com.bolsadeideas.springboot.app.models.service.IAuthorityService;
import com.bolsadeideas.springboot.app.models.service.IGestorService;
import com.bolsadeideas.springboot.app.models.service.IUsuarioService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@SessionAttributes("usuario")
public class UsuarioController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    @Qualifier("usuarioServiceJPA")
    private IUsuarioService usuarioService;

    @Autowired
    @Qualifier("authorityServiceJPA")
    private IAuthorityService authorityService;

    @Autowired
    @Qualifier("gestorServiceJPA")
    private IGestorService gestorService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @RequestMapping(value = "/listar/usuarios", method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page,
                         Model model,
                         Authentication authentication) {

        System.out.println("authentication: " + authentication);

        User usuario = gestorService.findByUsername(authentication.getName());
        Gestor gestor = gestorService.findOne(usuario.getId());

        System.out.println("UsuarioController.listar - gestor: " + gestor.getId());

        Pageable pageRequest = PageRequest.of(page, 10);

        Page<User> usuarios = usuarioService.findAll(pageRequest);

        PageRender<User> pageRender = new PageRender<>("/listar/usuarios", usuarios);
        model.addAttribute("titulo", "Listado de usuarios");
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("page", pageRender);
        model.addAttribute("gestor", gestor);


        return "listarUsuarios";
    }

    @RequestMapping(value = "/form/registro")
    public String crearUsuario(Map<String, Object> model) {

        System.out.println("UsuarioController.crearUsuario :"  + 1);

        User user = new User();


        //Por defecto al insertarse en la BD se inserta como habilitado
//        user.setEnabled(1);
//        user.setCliente(gestor);

        model.put("user", user);
//        model.put("gestor", gestor);
        model.put("tituloGestor", "Datos del gestor");
        model.put("tituloUsuario", "Datos del usuario");

        System.out.println("UsuarioController.crearUsuario :" + 2 );
        return "formRegistro";
    }

    @RequestMapping(value = "/form/registro", method = RequestMethod.POST)
    public String guardar(@Valid User user,
                          BindingResult result,
                          Model model,
                          RedirectAttributes flash,
                          SessionStatus status) {
        System.out.println("UsuarioController.guardar :" + 1);

        if (result.hasErrors()) {
//            model.addAttribute("titulo", "Creación de cuenta de usuario");
            model.addAttribute("tituloGestor", "Datos del Gestor");
            model.addAttribute("tituloUsuario", "Datos del Usuario");
            String mensajeFlash = (user.getId() != null) ? "Gestor editado con éxito!" : "Gestor creado con éxito!";
            return "formRegistro";
        }
        System.out.println("UsuarioController.guardar :" + 2);

        String mensajeFlash = (user.getId() != null) ? "Usuario editado con éxito!" : "Usuario creado con éxito!";

        System.out.println("UsuarioController.guardar :" + 3);

        String bcryptPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(bcryptPassword);
        user.setEnabled(1);

        Authority authority = new Authority();
        authority.setAuthority("ROLE_USER");
        List<Authority> listaAuthorities = new ArrayList<>();
        listaAuthorities.add(authority);

        user.setRoles(listaAuthorities);


        try{
            usuarioService.save(user);

        }catch (Exception e){
            System.out.println("UsuarioController.guardar :" + e.getMessage());
            model.addAttribute("tituloGestor", "Datos del Gestor");
            model.addAttribute("tituloUsuario", "Datos del Usuario");
            flash.addFlashAttribute("error", "El DNI o el USERNAME ya se encuentran registrados!");
            return "redirect:/form/registro";
        }

        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
//        System.out.println("UsuarioController.guardar :" + 5);


        return "redirect:/login";
    }

}