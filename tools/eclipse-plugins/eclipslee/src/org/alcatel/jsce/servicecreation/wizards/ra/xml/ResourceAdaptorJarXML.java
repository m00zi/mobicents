
/**
 *   Copyright 2006 Alcatel, OSP.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.alcatel.jsce.servicecreation.wizards.ra.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *  Description:
 * <p>
 * This class represents the resource adaptor-jar.xml file.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ResourceAdaptorJarXML extends DTDXML {

	public static final String QUALIFIED_NAME = "resource-adaptor-jar";
	public static final String PUBLIC_ID = "-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.0//EN";
	public static final String SYSTEM_ID ="http://java.sun.com/dtd/slee-resource-adaptor-jar_1_0.dtd";

	public ResourceAdaptorJarXML(EntityResolver resolver, InputSource dummyXML) throws ParserConfigurationException {
		super(QUALIFIED_NAME, PUBLIC_ID, SYSTEM_ID, resolver);
		readDTDVia(resolver, dummyXML);
	}
	
	/**
	 * Parse the provided InputStream as though it contains JAIN SLEE Profile Specification XML Data.
	 * @param stream
	 */
	
	public ResourceAdaptorJarXML(InputStream stream, EntityResolver resolver, InputSource dummyXML) throws SAXException, IOException, ParserConfigurationException {
		super(stream, resolver);			

		// Verify that this is really an ratype-jar XML file.
		if (!getRoot().getNodeName().equals(QUALIFIED_NAME))
			throw new SAXException("This was not a resource adaptor jar XML file.");

		readDTDVia(resolver, dummyXML);
	}

	
	public ResourceAdaptorXML[] getResourceAdaptors() {
		Element elements[] = getNodes("resource-adaptor-jar/resource-adaptor");
		ResourceAdaptorXML ratypes[] = new ResourceAdaptorXML[elements.length];
		for (int i = 0; i < elements.length; i++)
			ratypes[i] = new ResourceAdaptorXML(document, elements[i], dtd);
		
		return ratypes;		
	}
	
	public ResourceAdaptorXML getResourceAdaptorType(String name, String vendor, String version) throws ComponentNotFoundException {
		ResourceAdaptorXML ratypes[] = getResourceAdaptors();
		for (int i = 0; i < ratypes.length; i++) {
			ResourceAdaptorXML ratype = ratypes[i];
			
			if (name.equals(ratype.getName())
					&& vendor.equals(ratype.getVendor())
					&& version.equals(ratype.getVersion()))
					return ratype;
		}

		throw new ComponentNotFoundException("Unable to find specified RA.");

	}
	
	public ResourceAdaptorXML addResourceAdaptor() {	
		Element child = addElement(getRoot(), "resource-adaptor");
		return new ResourceAdaptorXML(document, child, dtd);
	}
	
	public void removeResourceAdaptor(ResourceAdaptorXML ra) {
		ra.getRoot().getParentNode().removeChild(ra.getRoot());
	}
	
	public String toString() {
		String output = "";
		ResourceAdaptorXML ras[] = getResourceAdaptors();
		for (int i = 0; i < ras.length; i++) {
			if (i > 0)
				output += ", ";
			output += "[" + ras[i].getName() +", "+ras[i].getVendor()+", "+ras[i].getVersion() + "]";
		}
		return output;
	}


}
