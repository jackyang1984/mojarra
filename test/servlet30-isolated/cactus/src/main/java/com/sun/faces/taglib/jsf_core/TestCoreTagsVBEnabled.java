/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.taglib.jsf_core;

import java.util.Iterator;
import java.util.Locale;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;

import com.sun.faces.cactus.JspFacesTestCase;
import com.sun.faces.lifecycle.ApplyRequestValuesPhase;
import com.sun.faces.lifecycle.Phase;
import com.sun.faces.lifecycle.ProcessValidationsPhase;
import com.sun.faces.lifecycle.RenderResponsePhase;
import com.sun.faces.util.Util;

import org.apache.cactus.WebRequest;

/**
 * <B>TestValidatorTags</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 */

public class TestCoreTagsVBEnabled extends JspFacesTestCase {

//
// Protected Constants
//

    public static final String TEST_URI = "/TestCoreTagVBEnabled.jsp";
    public static final String LONGRANGE_ID = "validatorForm" +
        NamingContainer.SEPARATOR_CHAR +
        "longRange";
    public static final String LONGRANGE_VALUE = "115";
    public static final String INTRANGE_ID = "validatorForm" +
        NamingContainer.SEPARATOR_CHAR +
        "intRange";
    public static final String INTRANGE_VALUE = "NorthAmerica";
    public static final String DOUBLERANGE_ID = "validatorForm" +
        NamingContainer.SEPARATOR_CHAR +
        "doubleRange";
    public static final String DOUBLERANGE_VALUE = "1500";


    public boolean sendResponseToFile() {
        return false;
    }

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestCoreTagsVBEnabled() {
        super("TestValidatorTags");
	initLocalHostPath();
    }


    public TestCoreTagsVBEnabled(String name) {
        super(name);
	initLocalHostPath();
    }

    private String localHostPath = "localhost:8080";

    private void initLocalHostPath() {
	String containerPort = System.getProperty("container.port");
	if (null == containerPort || 0 == containerPort.length()) {
	    containerPort = "8080";
	}
	localHostPath = "localhost:" + containerPort;
    }

//
// Class methods
//

//
// General Methods
//


    public void beginValidators(WebRequest theRequest) {
        theRequest.setURL(localHostPath, "/test", "/faces", TEST_URI, null);
        theRequest.addParameter(LONGRANGE_ID, LONGRANGE_VALUE);
        theRequest.addParameter(INTRANGE_ID, INTRANGE_VALUE);
        theRequest.addParameter(DOUBLERANGE_ID, DOUBLERANGE_VALUE);
        theRequest.addParameter("validatorForm", "validatorForm");

    }


    public void setUp() {

        super.setUp();
        (getFacesContext().getExternalContext().getRequestMap()).put("intMin",
                                                                     new Integer(
                                                                         1));
        (getFacesContext().getExternalContext().getRequestMap()).put("intMax",
                                                                     new Integer(
                                                                         10));
        (getFacesContext().getExternalContext().getRequestMap()).put("longMin",
                                                                     new Long(
                                                                         100));
        (getFacesContext().getExternalContext().getRequestMap()).put("longMax",
                                                                     new Long(
                                                                         110));
        (getFacesContext().getExternalContext().getRequestMap()).put(
            "doubleMin", new Double(1.0));
        (getFacesContext().getExternalContext().getRequestMap()).put(
            "doubleMax", new Double(10.0));
    }


    public void testValidators() {
        System.out.println("Test VBEnabled attributes on core Validator tags");
        // Verify the parmeters are as expected
        String paramVal = (String) (getFacesContext().getExternalContext()
            .getRequestParameterMap()).get(LONGRANGE_ID);
        assertTrue(LONGRANGE_VALUE.equals(paramVal));

        String paramVal1 = (String) (getFacesContext().getExternalContext()
            .getRequestParameterMap()).get(DOUBLERANGE_ID);
        assertTrue(DOUBLERANGE_VALUE.equals(paramVal1));

        String paramVal2 = (String) (getFacesContext().getExternalContext()
            .getRequestParameterMap()).get(INTRANGE_ID);
        assertTrue(INTRANGE_VALUE.equals(paramVal2));

        boolean result = false;
        String value = null;
        Phase
            renderResponse = new RenderResponsePhase(),
            processValidations = new ProcessValidationsPhase(),
            applyRequestValues = new ApplyRequestValuesPhase();

        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        page.setLocale(Locale.US);
        page.setViewId(TEST_URI);
        getFacesContext().setViewRoot(page);

        // This builds the tree, and usefaces saves it in the session
        renderResponse.execute(getFacesContext());
        assertTrue(!(getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

        // This causes the components to be set to valid
        applyRequestValues.execute(getFacesContext());
        assertTrue(!(getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

        // process the validations
        processValidations.execute(getFacesContext());
        // We know there are validation errors on the page
        assertTrue(getFacesContext().getRenderResponse());

        System.out.println("Verifying results...");
        // verify the messages have been added correctly.
        UIComponent comp = null;
        Iterator messages = null;

        assertTrue(null != (messages = getFacesContext().getMessages()));
        assertTrue(messages.hasNext());

        // check the messages for each component in the page
        assertTrue(null !=
                   (comp =
                    getFacesContext().getViewRoot().findComponent(LONGRANGE_ID)));
        assertTrue(
            null !=
            (messages =
             getFacesContext().getMessages(comp.getClientId(getFacesContext()))));
        assertTrue(messages.hasNext());

        assertTrue(null !=
                   (comp =
                    getFacesContext().getViewRoot().findComponent(INTRANGE_ID)));
        assertTrue(
            null !=
            (messages =
             getFacesContext().getMessages(comp.getClientId(getFacesContext()))));
        assertTrue(messages.hasNext());

        assertTrue(null !=
                   (comp =
                    getFacesContext().getViewRoot().findComponent(
                        DOUBLERANGE_ID)));
        assertTrue(
            null !=
            (messages =
             getFacesContext().getMessages(comp.getClientId(getFacesContext()))));
        assertTrue(messages.hasNext());

    }


} // end of class TestValidatorTags
