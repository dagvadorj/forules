package org.ubdynamics.rulesapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.xml.bind.annotation.XmlRootElement;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;

public class DroolsUtil {

	public static List<String> runRules(Object s, ServletContext servletContext) {

		try {

			// load up the knowledge base
			KnowledgeBase kbase = readKnowledgeBase(servletContext);

			StatefulKnowledgeSession ksession = kbase
					.newStatefulKnowledgeSession();

			KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
					.newFileLogger(ksession, "test");

			List<String> errors = new ArrayList<String>();

			// List<Command> cmds = new ArrayList<Command>();
			// cmds.add(CommandFactory.newSetGlobal("errors", errors));
			// cmds.add(CommandFactory.newInsert(s));
			//
			// ksession.execute(CommandFactory.newBatchExecution(cmds));

			// Add global errors

			ksession.insert(errors);
			ksession.insert(s);

			ksession.fireAllRules();

			logger.close();

			System.out.println(errors);

			return errors;

		} catch (Throwable t) {
			System.out.println("Error in send()");
			t.printStackTrace();
			return null;
		}

	}

	private static KnowledgeBase readKnowledgeBase(ServletContext servletContext)
			throws FileNotFoundException, MalformedURLException {

		InputStream inputStream = new FileInputStream(
				servletContext.getRealPath("changeset.xml"));

		// load package
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();

		kbuilder.add(ResourceFactory.newInputStreamResource(inputStream),
				ResourceType.CHANGE_SET);

		System.out.println("v");

		System.out.println("vo");

		// kbuilder.add(ResourceFactory.newClassPathResource("Test.drl", clazz),
		// ResourceType.DRL);

		// create the knowledge base
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

		// add the package to the kbase
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

		return kbase;
	}

}
