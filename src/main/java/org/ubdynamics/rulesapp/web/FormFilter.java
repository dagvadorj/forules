package org.ubdynamics.rulesapp.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.apache.commons.io.IOUtils;

import com.sun.xml.registry.uddi.bindings_v2_2.GetServiceDetail;

/**
 * Servlet Filter implementation class FormFilter
 */
public class FormFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public FormFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		System.out.println("Filter for form...");

		InputStream inputStream = new FileInputStream(request
				.getServletContext().getRealPath("xforms-repo/sample.xhtml"));

		OutputStream outputStream = response.getOutputStream();

		// IOUtils.copy(inputStream, outputStream);
		
		RequestDispatcher view = request.getRequestDispatcher("xforms-repo/sample.xhtml");
		view.forward(request, response);

		// pass the request along the filter chain
		// chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
