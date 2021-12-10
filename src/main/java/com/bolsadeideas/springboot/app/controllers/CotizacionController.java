package com.bolsadeideas.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import com.bolsadeideas.springboot.app.models.entity.*;
import com.bolsadeideas.springboot.app.models.service.ICotizacionService;
import com.bolsadeideas.springboot.app.models.service.IGestorService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.*;

import javax.validation.Valid;

@Secured("ROLE_USER")
@Controller
@RequestMapping("/cotizacion")
@SessionAttributes("cotizacion")
public class CotizacionController {


	@Autowired
	@Qualifier("gestorServiceJPA")
	private IGestorService gestorService;

	@Autowired
	@Qualifier("cotizacionServiceJPA")
	private ICotizacionService cotizacionService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id,
					  Model model,
					  Authentication authentication,
					  RedirectAttributes flash){
		/* Esta llamada hace muchas consultas a las tablas de la base de datos (Gestores -> Cotizacions -> Cotizacions_Items -> Productos) */
		//Cotizacion cotizacion = gestorService.findCotizacionById(id);

		/* Optimizando la consulta usando join fetch a las tablas de la base de datos
		(select f from Cotizacion f join fetch f.cliente c join fetch f.items l join fetch l.producto where f.id=?1)

		################################################################################################
		select

			cotizacion0_.id as id1_1_0_,
			cliente1_.id as id1_0_1_,
			items2_.id as id1_2_2_,
			producto3_.id as id1_3_3_,

				cotizacion0_.cliente_id as cliente_5_1_0_,
				cotizacion0_.create_at as create_a2_1_0_,
				cotizacion0_.descripcion as descripc3_1_0_,
				cotizacion0_.observacion as observac4_1_0_,

				cliente1_.apellido as apellido2_0_1_,
				cliente1_.create_at as create_a3_0_1_,
				cliente1_.email as email4_0_1_,
				cliente1_.foto as foto5_0_1_,
				cliente1_.nombre as nombre6_0_1_,

				items2_.cantidad as cantidad2_2_2_,
				items2_.producto_id as producto3_2_2_,
				items2_.cotizacion_id as cotizacion_4_2_0__,
				items2_.id as id1_2_0__,

				producto3_.create_at as create_a2_3_3_,
				producto3_.nombre as nombre3_3_3_,
				producto3_.precio as precio4_3_3_

			from cotizacions cotizacion0_

				inner join clientes cliente1_ on cotizacion0_.cliente_id=cliente1_.id
				inner join cotizacions_items items2_ on cotizacion0_.id=items2_.cotizacion_id
				inner join productos producto3_ on items2_.producto_id=producto3_.id

			where cotizacion0_.id=?

		################################################################################################
		*/

		Cotizacion cotizacion = gestorService.fetchCotizacionByIdWithGestorWithItemCotizacionWithProducto(id);

		User user = gestorService.findByUsername(authentication.getName());
		Gestor gestor = gestorService.findOne(user.getId());

		if(cotizacion == null){
			flash.addFlashAttribute("error", "La cotizacion no existe en la base de datos!");
			return "redirect:/listarGestores";
		}
		model.addAttribute("cotizacion", cotizacion);
		model.addAttribute("titulo", "Cotizacion Nº " + cotizacion.getId());
		model.addAttribute("gestor", gestor);
		return "cotizacion/ver" ;
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page,
						 Model model,
						 Authentication authentication ){

		User user = gestorService.findByUsername(authentication.getName());
		Gestor gestor = gestorService.findOne(user.getId());

		Pageable pageRequest = PageRequest.of(page, 10);

		//Page<Cotizacion> cotizaciones = cotizacionService.findAll(pageRequest);
		Page<Cotizacion> cotizaciones = cotizacionService.findAll(pageRequest);


		PageRender<Cotizacion> pageRender = new PageRender<>("/listar", cotizaciones);


		model.addAttribute("titulo", "Listado Total Cotizaciones");
		model.addAttribute("cotizaciones", cotizaciones);
		model.addAttribute("page", pageRender);
		model.addAttribute("gestor", gestor);

		return "cotizacion/listar";
	}

	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value = "clienteId") Long clienteId,
						Map<String, Object> model,
						Authentication authentication,
						RedirectAttributes flash) {

		Gestor gestor = gestorService.findOne(clienteId);
		System.out.println("CotizacionController.crear");

		if (gestor == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listarGestores";
		}

		Cotizacion cotizacion = new Cotizacion();
		cotizacion.setGestor(gestor);

		model.put("cotizacion", cotizacion);
		model.put("titulo", "Generar Cotización");
		model.put("gestor", gestor);

		return "cotizacion/form";
	}

	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		System.out.println("ProductoController.cargarProductos: " + term);
		List<Producto> lista = gestorService.findByNombre(term);
		return gestorService.findByNombre(term);
	}
	
	@PostMapping("/form")
	public String guardar(@Valid Cotizacion cotizacion,
			BindingResult result,
			Model model,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
			RedirectAttributes flash,
			SessionStatus status) {

		if(result.hasErrors()){
			model.addAttribute("titulo", "Crear Cotizacion");
			return "cotizacion/form";
		}
		if(itemId == null || itemId.length == 0){
			model.addAttribute("titulo", "Crear Cotizacion");
			model.addAttribute("error", "Error: La cotizacion NO puede no tener lineas!");
			return "cotizacion/form";
		}

		for (int i = 0; i < itemId.length; i++) {
			Producto producto = gestorService.findProductoById(itemId[i]);

			ItemCotizacion linea = new ItemCotizacion();
			linea.setDescripcion(producto.getNombre());
			linea.setPrecio_unit(producto.getPrecio());
			linea.setCantidad(cantidad[i]);
			linea.setSubtotal(producto.getPrecio() * cantidad[i]);
			linea.setProducto(producto);
			cotizacion.addItemCotizacion(linea);

			log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
		}
		cotizacion.calculaIGV();
		cotizacion.calculaSubTotal();
		cotizacion.calculaTotal();

		gestorService.saveCotizacion(cotizacion);
		status.setComplete();

		flash.addFlashAttribute("success", "Cotizacion creada con éxito!");

		return "redirect:/ver/gestor/" + cotizacion.getGestor().getId();
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash){

		Cotizacion cotizacion = gestorService.findCotizacionById(id);

		if(cotizacion != null){
			gestorService.deleteCotizacion(id);
			flash.addFlashAttribute("success", "Cotizacion eliminada con éxito!");
			return "redirect:/ver/gestor/" + cotizacion.getGestor().getId();
		}
		flash.addFlashAttribute("error", "La Cotizacion no existe en la base de datos, no se pudo eliminar");

		return "redirect:/listar";
	}

}
