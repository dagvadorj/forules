package org.ubdynamics.rulesapp.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Servlet implementation class JaxbServlet
 */
@WebServlet("/JaxbServlet")
public class JaxbServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String className = request.getParameter("className");

		System.out.println("className: " + className);

		Class<?> clazz;

		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
			return;
		}

		Marshaller marshaller;

		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			marshaller = context.createMarshaller();
		} catch (JAXBException e) {
			System.out.println(e);
			return;
		}

		Object object;

		try {
			object = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			System.out.println(e);
			return;
		}

		try {
			marshaller.marshal(object, response.getOutputStream());
		} catch (JAXBException e) {
			System.out.println(e);
			return;
		}
	}
}
