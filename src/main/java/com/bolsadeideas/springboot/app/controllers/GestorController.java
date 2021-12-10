package com.bolsadeideas.springboot.app.controllers;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.bolsadeideas.springboot.app.models.entity.User;
import com.bolsadeideas.springboot.app.models.service.IGestorService;
import com.bolsadeideas.springboot.app.models.service.IUsuarioService;
import com.bolsadeideas.springboot.app.view.xml.GestorList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Gestor;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("gestor")
public class GestorController {

    protected final Log logger = LogFactory.getLog(this.getClass());


	@Autowired
	@Qualifier("gestorServiceJPA")
	private IGestorService gestorService;

	@Autowired
	@Qualifier("usuarioServiceJPA")
	private IUsuarioService usuarioService;

	@Autowired
	private IUploadFileService uploadFileService;

	@Autowired
	private MessageSource messageSource;

	@Secured({"ROLE_USER", "ROLE_AMIN"})
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;

		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@GetMapping(value = "/ver/gestor/{id}")
	public String ver(@PathVariable(value = "id") Long id,
					  Map<String, Object> model,
					  RedirectAttributes flash) {

		/* Esta llamada hace muchas consultas a las tablas de la base de datos (Gestores -> Cotizaciones s) */
		//Gestor gestor = gestorService.findOne(id);

		/* Optimizando la consulta usando join fetch a las tablas de la base de datos
		(select c from Gestor c left join fetch c.cotizaciones f where c.id=?1)
		###########################################################################################
		select

			gestor0_.id as id1_0_0_,
			cotizacones1_.id as id1_1_1_,

				gestor0_.apellido as apellido2_0_0_,
				gestor0_.create_at as create_a3_0_0_,
				gestor0_.email as email4_0_0_,
				gestor0_.foto as foto5_0_0_,
				gestor0_.nombre as nombre6_0_0_,

				cotizacones1_.gestor_id as gestor_5_1_1_,
				cotizacones1_.create_at as create_a2_1_1_,
				cotizacones1_.descripcion as descripc3_1_1_,
				cotizacones1_.observacion as observac4_1_1_,
				cotizacones1_.gestor_id as gestor_5_1_0__,
				cotizacones1_.id as id1_1_0__

			from gestores gestor0_

				left outer inner join cotizacones cotizacones1_ on gestor0_.id=cotizacones1_.gestor_id

			where gestor0_.id=?
		###########################################################################################
		*/

		Gestor gestor = gestorService.fetchByIdWithCotizaciones(id);
		if (gestor == null) {
			flash.addFlashAttribute("error", "El gestor no existe en la base de datos");
			return "redirect:/listarGestores";
		}

		model.put("gestor", gestor);
		model.put("titulo", "Perfil del Gestor ");
		return "verGestor";
	}

	// Reporte Docente - inicio
	@RequestMapping(value = "/reporte/gestores", method = RequestMethod.GET)
	public String reporte(@RequestParam(name = "page", defaultValue = "0") int page,
						  Model model,
						  Authentication authentication,
						  @Param("keyword") String keyword){



		User user = gestorService.findByUsername(authentication.getName());
		Gestor gestor = gestorService.findOne(user.getId());

		System.out.println("GestorController.listar - gestor: " + gestor.getId());

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<Gestor> gestores = gestorService.findAll(pageRequest);

		if(keyword == null || keyword.length() == 0 ){
			gestores = gestorService.findAll(pageRequest); //traer por defecto si no hay filtro
		}else {
			List<Gestor> docenteList =  gestorService.findAll(keyword);
			gestores = new PageImpl<>(docenteList);
		}



		PageRender<Gestor> pageRender = new PageRender<>("/reporte/docentes", gestores);
		model.addAttribute("titulo", "Listado de Gestores");
		model.addAttribute("gestores", gestores);
		model.addAttribute("page", pageRender);
		model.addAttribute("gestor", gestor);


		return "reporteGestores";
	}
	// Reporte Docente - fin

	@GetMapping(value = "/listar-rest")
	public @ResponseBody GestorList listarRest() {

		return new GestorList(gestorService.findAll());
	}

	@RequestMapping(value = "/listar/gestores", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page,
						Model model,
						Authentication authentication,
						HttpServletRequest request,
						Locale locale,
						@Param("keyword") String keyword){

		if(authentication != null){
			logger.info("Hola usuario auntenticado, tu username es: ".concat(authentication.getName()));
		}

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if(auth != null){
			logger.info("Utilizando forma estática SecurityContextHandler.getContext().getAuthentication(): Usuario autenticado, username: ".concat(auth.getName()));
		}

	    if(hasRole("ROLE_ADMIN")){
            logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
        }else{
            logger.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
        }

		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");

	    if(securityContext.isUserInRole("ROLE_ADMIN")){
	    	logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" tienes acceso."));
		}else{
			logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}

		if(request.isUserInRole("ROLE_ADMIN")){
			logger.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" tienes acceso."));
		}else{
			logger.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}

		User user = gestorService.findByUsername(authentication.getName());
		Gestor gestor = gestorService.findOne(user.getId());

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<Gestor> gestores = gestorService.findAll(pageRequest);

		if(keyword == null || keyword.length() == 0 ){
			gestores = gestorService.findAll(pageRequest); //traer por defecto si no hay filtro
		}else {
			List<Gestor> gestorList =  gestorService.findAll(keyword);
			gestores = new PageImpl<>(gestorList);
		}

		PageRender<Gestor> pageRender = new PageRender<Gestor>("/listar/gestores", gestores);
		model.addAttribute("titulo", messageSource.getMessage("text.gestor.listar.titulo", null, locale));
		model.addAttribute("gestores", gestores);
		model.addAttribute("page", pageRender);
		model.addAttribute("gestor", gestor);

		return "listarGestores";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form/gestor")
	public String crear(Map<String, Object> model) {

		Gestor gestor = new Gestor();
		model.put("gestor", gestor);
		model.put("titulo", "Formulario de Gestor");
		return "formGestor";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/form/gestor/{id}")
	public String editar(@PathVariable(value = "id") Long id,
						 Map<String, Object> model,
						 RedirectAttributes flash) {

		Gestor gestor = null;

		if (id > 0) {
			gestor = gestorService.findOne(id);
			if (gestor == null) {
				flash.addFlashAttribute("error", "El ID del gestor no existe en la BBDD!");
				return "redirect:/listar/gestores";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del gestor no puede ser cero!");
			return "redirect:/listar/gestores";
		}
		model.put("gestor", gestor);
		model.put("titulo", "Editar Gestor");
		return "formGestor";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form/gestor", method = RequestMethod.POST)
	public String guardar(@Valid Gestor gestor,
						  BindingResult result,
						  Model model,
						  RedirectAttributes flash,
						  SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Gestor");
			return "formGestor";
		}

		String mensajeFlash = (gestor.getId() != null) ? "Gestor editado con éxito!" : "Gestor creado con éxito!";

		gestorService.save(gestor);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/listar/gestores";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/gestor/{id}")
	public String eliminar(@PathVariable(value = "id") Long id,
						   RedirectAttributes flash) {

		if (id > 0) {

			try{

				gestorService.delete(id);
				flash.addFlashAttribute("success", "Gestor eliminado con éxito!");

			}catch (Exception e){
				Gestor gestor = gestorService.findOne(id);
				User user =  usuarioService.findOne(gestor.getId());

				gestorService.delete(id);
				usuarioService.delete(user.getId());

				flash.addFlashAttribute("warning", "Gestor y sus accesos eliminado con éxito!");
			}

		}
		return "redirect:/listar/gestores";
	}

	private boolean hasRole(String role){

        SecurityContext context = SecurityContextHolder.getContext();
        if( context == null){
            return false;
        }
        Authentication auth = context.getAuthentication();

        if( auth == null){
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        /*
        // Este no trae el nombre del role
        return authorities.contains(new SimpleGrantedAuthority(role));
		*/


        for(GrantedAuthority authority: authorities){
            if (role.equals(authority.getAuthority())){
                logger.info("Hola ".concat(auth.getName()).concat(" tu roles es: ".concat(authority.getAuthority())));
                return true;
            }
        }
	    return false;

    }
}