package com.fashionstore.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;

    @Override
    public void init() {
        productDAO = new ProductDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer categoryId = null;
        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;

        String categoryParam = request.getParameter("categoryId");
        String keyword = request.getParameter("keyword");
        String minParam = request.getParameter("minPrice");
        String maxParam = request.getParameter("maxPrice");

        try {
            if (categoryParam != null && !categoryParam.trim().isEmpty()) {
                categoryId = Integer.parseInt(categoryParam);
            }

            if (minParam != null && !minParam.trim().isEmpty()) {
                minPrice = new BigDecimal(minParam);
            }

            if (maxParam != null && !maxParam.trim().isEmpty()) {
                maxPrice = new BigDecimal(maxParam);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Product> products = productDAO.getFilteredProducts(
                categoryId,
                keyword,
                minPrice,
                maxPrice
        );

        request.setAttribute("products", products);
        request.setAttribute("keyword", keyword);
        request.setAttribute("categoryId", categoryParam);
        request.setAttribute("minPrice", minParam);
        request.setAttribute("maxPrice", maxParam);

        request.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(request, response);
    }
}