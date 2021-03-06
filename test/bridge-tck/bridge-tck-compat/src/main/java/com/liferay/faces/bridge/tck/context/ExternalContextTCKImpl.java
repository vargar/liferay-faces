/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.liferay.faces.bridge.tck.context;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;
import javax.servlet.http.HttpServletResponse;

import com.liferay.faces.bridge.context.BridgeContext;
import com.liferay.faces.bridge.context.flash.FlashHttpServletResponse;


/**
 * This class is intended to be used as a {@link ExternalContextWrapper} around the bridge default ExternalContext
 * implementation. Its purpose is to handle special cases of the TCK.
 *
 * @author  Neil Griffin
 */
public class ExternalContextTCKImpl extends ExternalContextWrapper {

	// Private Constants
	private static final String PATH_CHAPTER6_5_TEST = "/tests/chapter6_5.jsp";
	private static final String PATH_IMPLEMENTS_BRIDGE_WRITE_BEHIND_RESPONSE_TEST =
		"/tests/ImplementsBridgeWriteBehindResponseTest.jsp";
	private static final String PATH_USES_CONFIGURED_RENDER_RESPONSE_WRAPPER_TEST =
		"/tests/UsesConfiguredRenderResponseWrapperTest.jsp";
	private static final String PATH_USES_CONFIGURED_RESOURCE_RESPONSE_WRAPPER_TEST =
		"/tests/UsesConfiguredResourceResponseWrapperTest.jsp";

	// Private Data Members
	private boolean expectingPortletResponse;
	private ExternalContext wrappedExternalContext;

	public ExternalContextTCKImpl(ExternalContext externalContext) {
		this.wrappedExternalContext = externalContext;
	}

	@Override
	public void dispatch(String path) throws IOException {

		if (PATH_CHAPTER6_5_TEST.equals(path) || PATH_IMPLEMENTS_BRIDGE_WRITE_BEHIND_RESPONSE_TEST.equals(path) ||
				PATH_USES_CONFIGURED_RENDER_RESPONSE_WRAPPER_TEST.equals(path) ||
				(PATH_USES_CONFIGURED_RESOURCE_RESPONSE_WRAPPER_TEST.equals(path))) {
			expectingPortletResponse = true;
		}

		super.dispatch(path);
	}

	@Override
	public Object getResponse() {
		Object response = super.getResponse();

		// TCK TestPage202: implementsBridgeWriteBehindResponseTest
		// TCK TestPage204: JSP_ELTest
		// TCK Response Wrapper Portlet
		if ((expectingPortletResponse) && (response instanceof HttpServletResponse) &&
				!(response instanceof FlashHttpServletResponse)) {

			// Note that some of the TCK tests are designed such that they expect the bridge to implement the JSP {@link
			// Bridge#AFTER_VIEW_CONTENT} feature (ability to interleave native HTML markup with the markup generated by
			// JSF component). However, as documented in the {@link BridgeWriteBehindResponseMimeImpl} class-level
			// JavaDoc, it is possible to have the JSF implementation (Mojarra or MyFaces) handle the entire
			// interleaving process by itself. In order to get the implementsBridgeWriteBehindResponseTest to pass, it
			// is necessary for this method to return a PortletResponse that implements the {@link
			// BridgeWriteBehindResponse} interface. At this point in the JSF lifecycle, the value of {@link
			// BridgeContext#getPortletResponse()} will contain just such a value.
			BridgeContext bridgeContext = BridgeContext.getCurrentInstance();
			response = bridgeContext.getPortletResponse();
		}

		return response;
	}

	@Override
	public ExternalContext getWrapped() {
		return wrappedExternalContext;
	}

}
