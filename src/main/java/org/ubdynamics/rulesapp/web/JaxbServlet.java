package org.ubdynamics.rulesapp.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

/**
 * Servlet implementation class JaxbServlet
 */
@WebServlet("/JaxbServlet")
public class JaxbServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "rawtypes", "unchecked", "restriction" })
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String className = request.getParameter("className");

		Class<?> clazz = JaxbUtil.loadClass(getServletContext(), className);

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
			// marshaller.marshal(new JAXBElement(new QName("student"), clazz,
			// object), response.getOutputStream());
			marshaller.marshal(object, response.getOutputStream());
		} catch (JAXBException e) {
			System.out.println(e);
			return;
		}
	}
}
