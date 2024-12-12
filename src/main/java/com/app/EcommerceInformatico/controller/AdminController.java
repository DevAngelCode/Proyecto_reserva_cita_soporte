package com.app.EcommerceInformatico.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.EcommerceInformatico.excel.CategoriasExcel;
import com.app.EcommerceInformatico.excel.ProductosExcel;
import com.app.EcommerceInformatico.model.Categoria;
import com.app.EcommerceInformatico.model.Producto;
import com.app.EcommerceInformatico.model.Soporte;
import com.app.EcommerceInformatico.model.User;
import com.app.EcommerceInformatico.pdf.CategoriasPdf;
import com.app.EcommerceInformatico.pdf.ProductosPdf;
import com.app.EcommerceInformatico.service.CategoriaService;
import com.app.EcommerceInformatico.service.ProductoService;
import com.app.EcommerceInformatico.service.SoporteService;
import com.app.EcommerceInformatico.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private CategoriaService categoriaService;
	@Autowired
	private ProductoService productoService;
	@Autowired
	private UserService userService;
	@Autowired
	private SoporteService soporteService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@ModelAttribute
	public void getUserDetails(Principal p, Model model) {
		if (p != null) {
			String email = p.getName();
			User userDtls = userService.getUserByEmail(email);
			model.addAttribute("user", userDtls);
			userDtls.setIntentosFallidos(0);

		}

	}

	@GetMapping("/")
	public String home() {
		return "admin/home";
	}

	@GetMapping("/categorias")
	public String categorias(Model model) {
		model.addAttribute("categorias", categoriaService.getAllCategoria());
		return "admin/categorias";
	}

	@PostMapping("/saveCategoria")
	public String saveCategoria(@ModelAttribute Categoria categoria, @RequestParam MultipartFile file,
			HttpSession session) throws IOException {
		String imageName = file != null && !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";
		categoria.setImagenNombre(imageName);
		Boolean existCategory = categoriaService.existCategoria(categoria.getNombre());

		if (existCategory) {
			session.setAttribute("errorMsg", "La categoria ya existe");
		} else {
			Categoria saveCategory = categoriaService.saveCategoria(categoria);

			if (ObjectUtils.isEmpty(saveCategory)) {
				session.setAttribute("errorMsg", "Error al guardar la categoria");
			} else {
				// Solo intenta copiar el archivo si no está vacío
				if (file != null && !file.isEmpty()) {
					File saveFile = new ClassPathResource("static/img").getFile();
					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "categoria_img" + File.separator
							+ file.getOriginalFilename());

					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				}
				session.setAttribute("succMsg", "Categoria guardada con éxito");
			}
		}
		return "redirect:/admin/categorias";
	}

	@GetMapping("/editarCategoria/{id}")
	public String editarCategoria(@PathVariable Long id, Model model) {
		Categoria categoria = categoriaService.getCategoriaById(id);
		model.addAttribute("categoria", categoria);
		return "admin/editar_categoria";
	}

	@PostMapping("/updateCategoria")
	public String updateCategory(@ModelAttribute Categoria categoria, @RequestParam MultipartFile file,
			HttpSession session) throws IOException {
		Categoria oldCategoria = categoriaService.getCategoriaById(categoria.getId());
		String imageName = file.isEmpty() ? oldCategoria.getImagenNombre() : file.getOriginalFilename();

		if (!ObjectUtils.isEmpty(oldCategoria)) {
			// Validar si existe otra categoría con el mismo nombre
			Boolean existCategory = categoriaService.existCategoria(categoria.getNombre());
			if (existCategory && !oldCategoria.getNombre().equals(categoria.getNombre())) {
				session.setAttribute("errorMsg", "La categoría con este nombre ya existe");
				return "redirect:/admin/editarCategoria/" + categoria.getId();
			}

			oldCategoria.setNombre(categoria.getNombre());
			oldCategoria.setEstado(categoria.getEstado());
			oldCategoria.setImagenNombre(imageName);

			Categoria actualizarCategoria = categoriaService.saveCategoria(oldCategoria);
			if (!ObjectUtils.isEmpty(actualizarCategoria)) {
				if (!file.isEmpty()) {
					File saveFile = new ClassPathResource("static/img").getFile();
					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "categoria_img" + File.separator
							+ file.getOriginalFilename());
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				}
				session.setAttribute("succMsg", "Updated successfully");
			} else {
				session.setAttribute("errorMsg", "not updated! internal server error");
			}
		} else {
			session.setAttribute("errorMsg", "Categoría no encontrada");
		}
		return "redirect:/admin/editarCategoria/" + categoria.getId();
	}

	@GetMapping("/eliminarCategoria/{id}")
	public String eliminarCategoria(@PathVariable Long id, HttpSession session) {
		try {
			categoriaService.deleteCategoria(id);
			session.setAttribute("succMsg", "Eliminado con éxito");
		} catch (Exception e) {
			session.setAttribute("errorMsg", "No se puede eliminar, esta categoría está asociada con productos");
		}
		return "redirect:/admin/categorias";

	}

	@GetMapping("/ExportarCategoriasPdf")
	public ResponseEntity<InputStreamResource> exportarCategoriasPdf() {
		List<Categoria> categorias = categoriaService.getAllCategoria();
		ByteArrayInputStream bis = CategoriasPdf.categoriesReport(categorias);

		if (bis == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=categorias.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@GetMapping("/ExportarCategoriasExcel")
	public ResponseEntity<InputStreamResource> exportarCategoriasExcel() {
		List<Categoria> categorias = categoriaService.getAllCategoria();
		ByteArrayInputStream bis = CategoriasExcel.categoriesReport(categorias);

		if (bis == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=categorias.xlsx");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(bis));
	}

	@GetMapping("/productos")
	public String productos(Model model) {
		model.addAttribute("productos", productoService.getAllProducto());
		model.addAttribute("categoria", categoriaService.getAllCategoria());
		return "admin/productos";
	}

	@PostMapping("/saveProducto")
	public String saveProducto(@RequestParam Long categoriaId, @ModelAttribute Producto producto,
			@RequestParam MultipartFile file, HttpSession session) throws IOException {
		String imageName = file != null && !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";
		producto.setImagen(imageName);
		producto.setDescuento(0);
		producto.setPrecioDescuento(producto.getPrecio());
		Categoria categoria = categoriaService.getCategoriaById(categoriaId);
		producto.setCategoria(categoria);

		Boolean existProduct = productoService.existProducto(producto.getNombre());
		if (existProduct) {

			session.setAttribute("errorMsg", "El producto ya existe");
		} else {
			Producto saveProduct = productoService.saveProducto(producto);
			if (ObjectUtils.isEmpty(saveProduct)) {
				session.setAttribute("errorMsg", "Error al guardar el producto");
			} else {
				if (file != null && !file.isEmpty()) {
					File saveFile = new ClassPathResource("static/img").getFile();
					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "producto_img" + File.separator
							+ file.getOriginalFilename());
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				}
				session.setAttribute("succMsg", "Producto guardado con éxito");
			}

		}

		return "redirect:/admin/productos";
	}

	@GetMapping("/eliminarProducto/{id}")
	public String eliminarProducto(@PathVariable Long id, HttpSession session) {
		Boolean deleteProduct = productoService.deleteProducto(id);
		if (deleteProduct) {
			session.setAttribute("succMsg", "Eliminado con éxito");
		} else {
			session.setAttribute("errorMsg", "No eliminado! error interno del servidor");
		}
		return "redirect:/admin/productos";
	}

	@GetMapping("/editarProducto/{id}")
	public String editarProducto(@PathVariable Long id, Model model) {
		Producto producto = productoService.getProductoById(id);
		model.addAttribute("producto", producto);
		model.addAttribute("categoria", categoriaService.getAllCategoria());
		return "admin/editar_producto";
	}

	@PostMapping("/updateProducto")
	public String updateProducto(@RequestParam Long categoriaId, @ModelAttribute Producto producto,
			@RequestParam MultipartFile file, HttpSession session) throws IOException {

		Producto oldProducto = productoService.getProductoById(producto.getId());
		Boolean existProduct = productoService.existProducto(producto.getNombre());
		Categoria categoria = categoriaService.getCategoriaById(categoriaId);
		if (existProduct && !oldProducto.getNombre().equals(producto.getNombre())) {
			session.setAttribute("errorMsg", "El producto ya existe");
		} else {

			if (producto.getDescuento() < 0 || producto.getDescuento() > 100) {
				session.setAttribute("errorMsg", "El descuento debe estar entre 0 y 100");
			} else {
				Producto updateProducto = productoService.updateProducto(producto, file, categoria);
				if (!ObjectUtils.isEmpty(updateProducto)) {
					session.setAttribute("succMsg", "Actualizado con éxito");
				} else {
					session.setAttribute("errorMsg", "no actualizado! error interno del servidor");
				}

			}
		}

		return "redirect:/admin/editarProducto/" + producto.getId();
	}

	@GetMapping("/ExportarProductosPdf")
	public ResponseEntity<InputStreamResource> exportarProductosPdf() {
		List<Producto> productos = productoService.getAllProducto();
		ByteArrayInputStream bis = ProductosPdf.productsReport(productos);

		if (bis == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=productos.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));

	}

	@GetMapping("/ExportarProductosExcel")
	public ResponseEntity<InputStreamResource> exportarProductosExcel() {
		List<Producto> productos = productoService.getAllProducto();
		ByteArrayInputStream bis = ProductosExcel.productosToExcel(productos);

		if (bis == null) {
			return ResponseEntity.internalServerError().build();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=productos.xlsx");

		return ResponseEntity.ok().headers(headers)
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new InputStreamResource(bis));
	}

	@GetMapping("/empleados")
	public String empleados(Model model) {
		List<User> empleados = userService.getAllEmpleado();
		List<Soporte> soportes = soporteService.getAllSoporte();
		model.addAttribute("empleados", empleados);
		model.addAttribute("soportes", soportes);

		return "admin/empleados";
	}

	@PostMapping("/saveEmpleado")
	public String saveEmpleado(@RequestParam String nombre, @RequestParam String celular, @RequestParam String email,
			@RequestParam String password, @RequestParam String direccion, @RequestParam String ciudad,
			@RequestParam Long soporteId, MultipartFile file, HttpSession session) throws IOException {

		// Verifica si el email ya existe, si es así, evitar la creación de un nuevo
		// usuario con el mismo email
		if (userService.getUserByEmail(email) != null) {
			session.setAttribute("errorMsg", "El correo electrónico ya está registrado.");
			return "redirect:/admin/empleados";
		}

		// Crea un nuevo usuario
		User nuevoEmpleado = new User();
		nuevoEmpleado.setNombre(nombre);
		nuevoEmpleado.setCelular(celular);
		nuevoEmpleado.setEmail(email);

		// Codificar la contraseña
		String encode = passwordEncoder.encode(password);
		nuevoEmpleado.setPassword(encode);

		nuevoEmpleado.setDireccion(direccion);
		nuevoEmpleado.setCiudad(ciudad);

		// Establecer el soporte relacionado
		Soporte soporte = soporteService.getSoporteById(soporteId);
		nuevoEmpleado.setSoporte(soporte);

		// Establecer el rol como ROLE_EMPLOYEE
		nuevoEmpleado.setRol("ROLE_EMPLOYEE");

		// Establecer el estado (activo) y otros campos iniciales
		nuevoEmpleado.setIsEnable(true);
		nuevoEmpleado.setCuentaNoBloqueada(true);
		nuevoEmpleado.setIntentosFallidos(0);

		// Asignar una imagen por defecto si no se carga una nueva
		String imageName = file != null && !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";
		nuevoEmpleado.setImagenPerfil(imageName);

		// Guardar el nuevo empleado en la base de datos
		User savedEmpleado = userService.updateUser(nuevoEmpleado); // Método para crear el usuario

		if (savedEmpleado == null) {
			session.setAttribute("errorMsg", "Error al guardar el nuevo empleado.");
		} else {
			// Si se cargó una imagen, guardarla en el servidor
			if (file != null && !file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "usuario_img" + File.separator
						+ file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			session.setAttribute("succMsg", "Empleado creado con éxito.");
		}

		return "redirect:/admin/empleados"; // Redirige a la lista de empleados
	}

	// cambiar estado de empleado
	@GetMapping("/cambiarEstadoEmpleado/{id}")
	public String cambiarEstadoEmpleado(@PathVariable Long id, HttpSession session) {
		User empleado = userService.getUserById(id);
		if (empleado.getIsEnable()) {
			empleado.setIsEnable(false);
		} else {
			empleado.setIsEnable(true);
		}
		userService.updateUser(empleado);
		session.setAttribute("succMsg", "Estado actualizado con éxito");
		return "redirect:/admin/empleados";
	}

	// eliminar empleado
	@GetMapping("/eliminarEmpleado/{id}")
	public String eliminarEmpleado(@PathVariable Long id, HttpSession session) {
		try {
			userService.deleteUser(id);
			session.setAttribute("succMsg", "Empleado eliminado con éxito");
		} catch (Exception e) {
			session.setAttribute("errorMsg", "No se puede eliminar, este empleado está asociado con soporte");
		}
		return "redirect:/admin/empleados";
	}

	// servicio
	@GetMapping("/servicios")
	public String servicios(Model model) {
		List<Soporte> soportes = soporteService.getAllSoporte();
		model.addAttribute("soportes", soportes);
		return "admin/servicios";
	}

	@GetMapping("/editarServicio/{id}")
	public String editarServicio(@PathVariable Long id, Model model) {
		Soporte soporte = soporteService.getSoporteById(id);
		model.addAttribute("soporte", soporte);
		return "admin/editar_servicio";
	}

	@PostMapping("/updateServicio")
	public String updateServicio(@ModelAttribute Soporte soporte, HttpSession session) {

		Soporte updatedSoporte = soporteService.saveSoporte(soporte);
		if (updatedSoporte != null) {
			session.setAttribute("succMsg", "Actualizado con éxito");
		} else {
			session.setAttribute("errorMsg", "no actualizado! error interno del servidor");
		}

		return "redirect:/admin/editarServicio/" + soporte.getId();
	}

	// editar perfil
	@GetMapping("/editarPerfil")
	public String editarPerfil() {

		return "admin/editarPerfil";
	}

	@PostMapping("/update_perfil")
	public String updatePerfil(@ModelAttribute User user, MultipartFile file, HttpSession session) throws IOException {
		User userDtls = userService.getUserById(user.getId());
		String imageName = file.isEmpty() ? userDtls.getImagenPerfil() : file.getOriginalFilename();
		user.setImagenPerfil(imageName);
		User updatedUser = userService.updateUser(user);
		if (updatedUser != null) {

			if (!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "usuario_img" + File.separator
						+ file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			session.setAttribute("succMsg", "Perfil actualizado correctamente");
		} else {
			session.setAttribute("errorMsg", "Error al actualizar el perfil");
		}

		return "redirect:/admin/editarPerfil";
	}

	// cambiarContrasena
	@PostMapping("/cambiarContrasena")
	public String cambiarContrasena(@RequestParam Long userId, @RequestParam String password,
			@RequestParam String newPassword, @RequestParam String confirmNewPassword, HttpSession session) {

		User userU = userService.getUserById(userId);
		if (!passwordEncoder.matches(password, userU.getPassword())) {
			session.setAttribute("errorMsg", "La contraseña actual no coincide");
			return "redirect:/admin/editarPerfil";
		}
		if (!newPassword.equals(confirmNewPassword)) {
			session.setAttribute("errorMsg", "La nueva contraseña y la confirmación de la contraseña no coinciden");
			return "redirect:/admin/editarPerfil";
		}
		userU.setPassword(passwordEncoder.encode(newPassword));
		User updatedUser = userService.updateUser(userU);
		if (updatedUser != null) {
			session.setAttribute("succMsg", "Contraseña actualizada correctamente");
		} else {
			session.setAttribute("errorMsg", "Error al actualizar la contraseña");
		}

		return "redirect:/admin/editarPerfil";

	}

}
