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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.ubdynamics.rulesapp.util.DroolsUtil;
import org.ubdynamics.rulesapp.util.ClassUtil;

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

		Class<?> clazz = ClassUtil.loadClass(getServletContext(), className);

		Unmarshaller unmarshaller;

		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			System.out.println(e);
			return;
		}

		Object s;
		try {
			System.out.println("className: " + className);
			System.out.println("input: " + input);
			s = unmarshaller.unmarshal(IOUtils.toInputStream(input));
		} catch (JAXBException e) {
			e.printStackTrace();
			return;
		}

		System.out.println("a");

		System.out.println(s);

		System.out.println("b");

		try {

			List<String> errors = DroolsUtil.runRules(s, getServletContext());

			System.out.println("# of errors: " + errors);

			PrintWriter out = response.getWriter();

			out.print("<errors>");
			for (String error : errors) {
				out.print("<error text=\"" + error + "\" />");
			}
			out.print("</errors>");

		} catch (Exception e) {
			System.out.println("Bad drooling...");
		}
	}

}
