/**
 * Start time:13:20:34 2009-01-19<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.slee.management.DeploymentException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

/**
 * Start time:13:20:34 2009-01-19<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SuperTestCase extends TestCase {
	protected JAXBBaseUtilityClass testSubject = null;
	protected EntityResolver resolver = null;
	protected DocumentBuilderFactory factory = null;
	protected DocumentBuilder builder = null;

	protected Document parseDocument(File f) throws SAXException, IOException {
		return builder.parse(f);
	}

	
	protected Document parseDocument(String name) throws SAXException, IOException, URISyntaxException {
		
		URL url = Thread.currentThread().getContextClassLoader().getResource(
				name);
		return builder.parse(new File(url.toURI()));
	}
	@Override
	protected void setUp() throws Exception {

		super.setUp();
		this.resolver = new DefaultEntityResolver(Thread.currentThread()
				.getContextClassLoader());
		this.factory = DocumentBuilderFactory.newInstance();
		this.factory.setValidating(true);
		this.builder = this.factory.newDocumentBuilder();
		this.builder.setEntityResolver(this.resolver);
		this.testSubject = new JAXBBaseUtilityClass() {

			@Override
			public void buildDescriptionMap() {
				// TODO Auto-generated method stub

			}

			@Override
			public Object getJAXBDescriptor() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		this.resolver = null;
		this.factory = null;
		this.builder = null;
		this.testSubject = null;
	}
	
	public void testFake()
	{}
}
