package org.ubdynamics.rulesapp.web;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class DroolsUtil {

	public static List<String> send2(String input, String className) {

		System.out.println("className: " + className);

		Class<?> clazz;

		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
			return null;
		}

		try {

			// load up the knowledge base
			KnowledgeBase kbase = readKnowledgeBase();

			StatefulKnowledgeSession ksession = kbase
					.newStatefulKnowledgeSession();

			KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
					.newFileLogger(ksession, "test");

			Unmarshaller unmarshaller;

			try {
				JAXBContext context = JAXBContext.newInstance(clazz);
				unmarshaller = context.createUnmarshaller();
			} catch (JAXBException e) {
				System.out.println(e);
				return null;
			}

			System.out.println("Boo: " + input);

			Object s = unmarshaller.unmarshal(IOUtils.toInputStream(input));

			System.out.println("object: " + s);

			List<String> errors = new ArrayList<String>();

			ksession.setGlobal("errors", errors);

			ksession.insert(s);
			ksession.insert(errors);

			ksession.fireAllRules();

			logger.close();

			return errors;

		} catch (Throwable t) {
			System.out.println("Error in send()");
			t.printStackTrace();
			return null;
		}

	}

	public static void send() {

		try {

			// load up the knowledge base
			KnowledgeBase kbase = readKnowledgeBase();

			StatefulKnowledgeSession ksession = kbase
					.newStatefulKnowledgeSession();

			KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
					.newFileLogger(ksession, "test");

			Message message = new Message();
			message.setMessage("Hello World");
			message.setStatus(Message.HELLO);

			ksession.insert(message);
			ksession.fireAllRules();

			logger.close();

		} catch (Throwable t) {
			System.out.println("Error in send()");
			t.printStackTrace();
		}

	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("Test.drl"),
				ResourceType.DRL);

		KnowledgeBuilderErrors errors = kbuilder.getErrors();

		System.out.println(errors);

		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.out.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}

		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

		return kbase;
	}

	public static class Message {

		public static final int HELLO = 0;
		public static final int GOODBYE = 1;

		private String message;

		private int status;

		public String getMessage() {
			return this.message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public int getStatus() {
			return this.status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

	}
}
