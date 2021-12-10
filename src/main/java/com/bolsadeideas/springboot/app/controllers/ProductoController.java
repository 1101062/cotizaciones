package com.bolsadeideas.springboot.app.controllers;


import com.bolsadeideas.springboot.app.models.entity.Categoria;
import com.bolsadeideas.springboot.app.models.entity.Gestor;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.entity.User;
import com.bolsadeideas.springboot.app.models.service.ICategoriaService;
import com.bolsadeideas.springboot.app.models.service.IGestorService;
import com.bolsadeideas.springboot.app.models.service.IProductoService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("producto")
public class ProductoController {

    @Autowired
    @Qualifier("productoServiceJPA")
    private IProductoService productoService;

    @Autowired
    @Qualifier("categoriaServiceJPA")
    private ICategoriaService categoriaService;

    @Autowired
    @Qualifier("gestorServiceJPA")
    private IGestorService gestorService;

    @RequestMapping(value = {"/listar/productos", "/"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page,
                         Model model,
                         Authentication authentication,
                         @Param("keyword") String keyword ){

        Pageable pageRequest = PageRequest.of(page, 10);

        Page<Producto> productos = productoService.findAll(pageRequest);

        if(keyword == null || keyword.length() == 0 ){
            productos = productoService.findAll(pageRequest);
        }else{
            List<Producto> productoList = productoService.findAll(keyword.toUpperCase());
            productos = new PageImpl<>(productoList);
        }

        if(authentication != null){
            User user = gestorService.findByUsername(authentication.getName());
            Gestor gestor = gestorService.findOne(user.getId());
            model.addAttribute("gestor", gestor);
        }



        PageRender<Producto> pageRender = new PageRender<Producto>("/listar/productos", productos);
        model.addAttribute("titulo", "Listado de Productos");
        model.addAttribute("productos", productos);
        model.addAttribute("page", pageRender);
        model.addAttribute("keyword", keyword);


        return "listarProductos";
    }

    /* Metodo para agregar un nueva PRODUCTO */
    //primera fase : mostrar el formulario
    // ("/form/producto") Hace referencia  <a th:href="@{/form/producto}">Crear Producto</a> de listarProductos.html
    @RequestMapping(value = "/form/producto")
    public String crear(Map<String, Object> model,
                        Authentication authentication ){

        User user = gestorService.findByUsername(authentication.getName());
        Gestor gestor = gestorService.findOne(user.getId());

        Producto producto = new Producto();

        //Listas para mostrar en los select
        List<Categoria> categorias = categoriaService.findAll();

        model.put("producto", producto);
        model.put("titulo", "Formulario de Producto");
        model.put("gestor", gestor);

        //Mandamos las listas para mostrar en los select
        model.put("categorias", categorias);

        return "formProducto"; //Retorna el nombre de la vista, tal cual como la URL del RequestMapping

    }

    /* Metodo para editar un Gestor */
    @RequestMapping(value = "/form/producto/{id}")
    public String editar(@PathVariable(value = "id") Long id,
                         Map<String, Object> model,
                         Authentication authentication,
                         RedirectAttributes flash){

        Producto producto = null;

        if(id > 0){ //Si hay algun id a buscar
            producto = productoService.findOne(id);
            if(producto == null){
                flash.addFlashAttribute("error", "El ID del Producto no existe en le BBDD!");
                return "redirect:/listar/productos";
            }
        }else{
            flash.addFlashAttribute("error", "El ID del Producto no puede ser cero!");
            return "redirect:/listar/productos";
        }

        //Listas para mostrar en los select
        List<Categoria> categorias = categoriaService.findAll();

        User user = gestorService.findByUsername(authentication.getName());
        Gestor gestor = gestorService.findOne(user.getId());

        //Pasamos los valores a la vista
        model.put("producto", producto);
        model.put("titulo", "Editar un Producto ");

        //Mandamos las listas para mostrar en los select
        model.put("categorias", categorias);

        model.put("gestor", gestor);

        return "formProducto";
    }

    //Segunda fase : enviar el formulario con los datos
    // (value = "/form/producto", method = RequestMethod.POST)  Hace referencia <form th:action="@{/form/producto}" th:object="${producto}" method="post">  de formProductos.html
    @RequestMapping(value = "/form/producto", method = RequestMethod.POST)
    public String guardar(@Valid Producto producto,
                          BindingResult result,
                          Model model,
                          Authentication authentication,
                          RedirectAttributes flash,
                          SessionStatus status){

        User user = gestorService.findByUsername(authentication.getName());
        Gestor gestor = gestorService.findOne(user.getId());


        if(result.hasErrors()){
            List<Categoria> categorias = categoriaService.findAll();
            model.addAttribute("titulo", "Formulario de Producto");
            model.addAttribute("gestor", gestor);
            model.addAttribute("categorias", categorias);
            return "formProducto";
        }

        String mensajeFlash = (producto.getId() != null)? "Producto editado con éxito!" : "Producto creado con éxito!";


        try{
            productoService.save(producto);
        }catch (Exception e){
            model.addAttribute("titulo", "Formulario de Producto");
            model.addAttribute("gestor", gestor);
            return "redirect:/form/producto";
        }

        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:/listar/productos";//Redirigimos a la vista del listar (el nombre del archivo listarProdcutos.html)
    }

    @RequestMapping(value = "/eliminar/producto/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

        if (id > 0) {
            productoService.delete(id);
            flash.addFlashAttribute("success", "Producto eliminado con éxito!");
        }
        //Redirigimos a la vista del listar (el nombre del archivo listarGestores.html)
        return "redirect:/listar/productos";

    }
}
