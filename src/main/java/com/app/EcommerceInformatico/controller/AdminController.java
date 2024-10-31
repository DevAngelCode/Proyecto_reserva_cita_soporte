package com.app.EcommerceInformatico.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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

import com.app.EcommerceInformatico.model.Categoria;
import com.app.EcommerceInformatico.model.Producto;
import com.app.EcommerceInformatico.service.CategoriaService;
import com.app.EcommerceInformatico.service.ProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private CategoriaService categoriaService;
	@Autowired
	private ProductoService productoService;

	@GetMapping
	public String home() {
		return "admin/home";
	}

	@GetMapping("/productos")
	public String productos(Model model) {
		model.addAttribute("productos", productoService.getAllProducto());
		model.addAttribute("categoria", categoriaService.getAllCategoria());
		return "admin/productos";
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
		Boolean deleteCategory = categoriaService.deleteCategoria(id);
		if (deleteCategory) {
			session.setAttribute("succMsg", "Eliminado con éxito");
		} else {
			session.setAttribute("errorMsg", "No eliminado! error interno del servidor");
		}
		return "redirect:/admin/categorias";
	}

	@PostMapping("/saveProducto")
	public String saveProducto(@ModelAttribute Producto producto, @RequestParam MultipartFile file, HttpSession session)
			throws IOException {
		String imageName = file != null && !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";
		producto.setImagen(imageName);
		producto.setDescuento(0);
		producto.setPrecioDescuento(producto.getPrecio());
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
	public String updateProducto(@ModelAttribute Producto producto, @RequestParam MultipartFile file,
			HttpSession session) throws IOException {
		if (producto.getDescuento() < 0 || producto.getDescuento() > 100) {
			session.setAttribute("errorMsg", "El descuento debe estar entre 0 y 100");
		} else {
			Producto updateProducto = productoService.updateProducto(producto, file);
			if (!ObjectUtils.isEmpty(updateProducto)) {
				session.setAttribute("succMsg", "Actualizado con éxito");
			} else {
				session.setAttribute("errorMsg", "no actualizado! error interno del servidor");
			}

		}

		return "redirect:/admin/editarProducto/" + producto.getId();
	}

}
