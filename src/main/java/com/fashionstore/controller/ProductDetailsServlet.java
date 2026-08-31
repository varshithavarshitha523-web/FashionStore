package com.fashionstore.controller;

import java.io.IOException;
import java.util.List;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.ProductVariantDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.dao.impl.ProductVariantDAOImpl;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductVariant;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/product-details")
public class ProductDetailsServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ProductDAO productDAO = new ProductDAOImpl();

    private ProductVariantDAO variantDAO =
            new ProductVariantDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {

            int productId =
                    Integer.parseInt(request.getParameter("productId"));

            Product product =
                    productDAO.getProductById(productId);

            if (product == null) {
                response.sendRedirect("products");
                return;
            }

            // LOAD VARIANTS
            List<ProductVariant> variants =
                    variantDAO.getVariantByProductId(productId);

            request.setAttribute("product", product);
            request.setAttribute("variants", variants);

            request.setAttribute("relatedProducts",
                    productDAO.getRelatedProducts(
                            product.getCategoryId(),
                            productId,
                            4));

            request.getRequestDispatcher(
                    "/WEB-INF/views/product-details.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("products");
        }
    }
}