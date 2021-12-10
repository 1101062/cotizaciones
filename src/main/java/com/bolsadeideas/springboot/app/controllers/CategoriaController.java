package com.bolsadeideas.springboot.app.controllers;


import com.bolsadeideas.springboot.app.models.entity.Categoria;
import com.bolsadeideas.springboot.app.models.entity.Gestor;
import com.bolsadeideas.springboot.app.models.entity.User;
import com.bolsadeideas.springboot.app.models.service.ICategoriaService;
import com.bolsadeideas.springboot.app.models.service.IGestorService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@Controller
@SessionAttributes("categoria")
public class CategoriaController {

    @Autowired
    @Qualifier("categoriaServiceJPA")
    private ICategoriaService categoriaService;

    @Autowired
    @Qualifier("gestorServiceJPA")
    private IGestorService gestorService;

    @GetMapping(value = "/ver/categoria/{id}")
    public String ver(@PathVariable(value = "id") Long id,
                      Map<String, Object> model,
                      Authentication authentication,
                      RedirectAttributes flash) {

        Categoria categoria = categoriaService.fetchByIdWithProductos(id);

        if (categoria == null) {
            flash.addFlashAttribute("error", "La Categoría no existe en la base de datos");
            return "redirect:/listarCategorias";
        }

        User user = gestorService.findByUsername(authentication.getName());
        Gestor gestor = gestorService.findOne(user.getId());

        model.put("categoria", categoria);
        model.put("titulo", "Detalle Categoria: " + categoria.getNombre());
        model.put("gestor", gestor);

        return "verCategoria";
    }

    @RequestMapping(value = "/listar/categorias", method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page,
                         Model model,
                         Authentication authentication ){

        User user = gestorService.findByUsername(authentication.getName());
        Gestor gestor = gestorService.findOne(user.getId());

        Pageable pageRequest = PageRequest.of(page, 10);

        Page<Categoria> categorias = categoriaService.findAll(pageRequest);

        PageRender<Categoria> pageRender = new PageRender<>("/listar/categorias", categorias);
        model.addAttribute("titulo", "Listado de Categorías");
        model.addAttribute("categorias", categorias);
        model.addAttribute("page", pageRender);
        model.addAttribute("gestor", gestor);

        return "listarCategorias";
    }

    /* Metodo para agregar un nueva CATEGORIA */
    //primera fase : mostrar el formulario
    // ("/form/categoria") Hace referencia  <a th:href="@{/form/categoria}">Crear Categoria</a> de listarCategorias.html
    @RequestMapping(value = "/form/categoria")
    public String crear(Map<String, Object> model,
                        Authentication authentication ){

        User user = gestorService.findByUsername(authentication.getName());
        Gestor gestor = gestorService.findOne(user.getId());

        Categoria categoria = new Categoria();
        model.put("categoria", categoria);
        model.put("titulo", "Formulario de Categoría");
        model.put("gestor", gestor);

        return "formCategoria"; //Retorna el nombre de la vista, tal cual como la URL del RequestMapping

    }

    /* Metodo para editar una Categoria */
    @RequestMapping(value = "/form/categoria/{id}")
    public String editar(@PathVariable(value = "id") Long id,
                         Map<String, Object> model,
                         Authentication authentication,
                         RedirectAttributes flash){

        Categoria categoria = null;

        if(id > 0){ //Si hay algun id a buscar
            categoria = categoriaService.findOne(id);
            if(categoria == null){
                flash.addFlashAttribute("error", "El ID de la Categoría no existe en le BBDD!");
                return "redirect:/listar/categorias";
            }
        }else{
            flash.addFlashAttribute("error", "El ID de la Categoría no puede ser cero!");
            return "redirect:/listar/categorias";
        }

        User user = gestorService.findByUsername(authentication.getName());
        Gestor gestor = gestorService.findOne(user.getId());

        //Pasamos los valores a la vista
        model.put("categoria", categoria);
        model.put("titulo", "Editar una Categoría ");
        model.put("gestor", gestor);

        return "formCategoria";
    }

    //Segunda fase : enviar el formulario con los datos
    // (value = "/form/categoria", method = RequestMethod.POST)  Hace referencia <form th:action="@{/form/categoria}" th:object="${categoria}" method="post">  de formCategorias.html
    @RequestMapping(value = "/form/categoria", method = RequestMethod.POST)
    public String guardar(@Valid Categoria categoria,
                          BindingResult result,
                          Model model,
                          Authentication authentication,
                          RedirectAttributes flash,
                          SessionStatus status){

        User user = gestorService.findByUsername(authentication.getName());
        Gestor gestor = gestorService.findOne(user.getId());

        if(result.hasErrors()){
            model.addAttribute("titulo", "Formulario de Categoría");
            model.addAttribute("gestor", gestor);
            return "formCategoria";
        }

        String mensajeFlash = (categoria.getId() != null)? "Categoría editada con éxito!" : "Categoría creada con éxito!";

        try{
            categoriaService.save(categoria);
        }catch (Exception e){
            model.addAttribute("titulo", "Formulario de Categoría");
            model.addAttribute("gestor", gestor);
            return "redirect:/form/categoria";
        }

        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:/listar/categorias";//Redirigimos a la vista del listar (el nombre del archivo listarCategorias.html)
    }

    @RequestMapping(value = "/eliminar/categoria/{id}")
    public String eliminar(@PathVariable(value = "id") Long id,
                           RedirectAttributes flash) {

        if (id > 0) {
            categoriaService.delete(id);
            flash.addFlashAttribute("success", "Categoría eliminada con éxito!");
        }
        //Redirigimos a la vista del listar (el nombre del archivo listarCategorias.html)
        return "redirect:/listar/categorias";

    }
}
