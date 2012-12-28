package org.ubdynamics.rulesapp.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.servlet.ServletContext;
import javax.xml.bind.annotation.XmlRootElement;

public class ClassUtil {

	public static Class<?> loadClass(ServletContext servletContext,
			String className) throws MalformedURLException {

		URL url = new URL("file:"
				+ servletContext.getRealPath("forules-models.jar"));

		ClassLoader classLoader = new URLClassLoader(new URL[] { url }, Thread
				.currentThread().getContextClassLoader());

		Class<?> clazz;

		try {
			clazz = Class.forName(className, true, classLoader);
			System.out.println("annots: "
					+ clazz.isAnnotationPresent(XmlRootElement.class));
		} catch (ClassNotFoundException e) {
			System.out.println(e);
			return null;
		}

		return clazz;
	}
}
