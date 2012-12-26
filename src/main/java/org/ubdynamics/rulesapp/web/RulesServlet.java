package org.ubdynamics.rulesapp.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

/**
 * Servlet implementation class RulesServlet
 */
@WebServlet("/RulesServlet")
public class RulesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String className = request.getParameter("className");

		System.out.println("in RulesServlet...");

		InputStream inputStream = request.getInputStream();

		String input = IOUtils.toString(inputStream);

		System.out.println("input: " + input);

		try {

			List<String> errors = DroolsUtil.send2(input, className);

			System.out.println("# of errors: " + errors);

			PrintWriter out = response.getWriter();

			// out.println("<error text=\"boo\" />");

			out.print("<errors>");

			for (String error : errors) {
				out.print("<error text=\"" + error + "\" />");
				// break;
			}

			out.print("</errors>");

		} catch (Exception e) {
			System.out.println("Bad drooling...");
		}
	}

}
