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
package com.liferay.faces.bridge.event;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;


/**
 * Section 5.2.6 of the Spec indicates that the bridge must proactively ensure that only the RESTORE_VIEW phase
 * executes, and Section 6.4 indicates that a PhaseListener must be used.
 *
 * @author  Neil Griffin
 */
public class RenderRequestPhaseListener implements PhaseListener {

	// serialVersionUID
	private static final long serialVersionUID = 8470095938465172618L;

	public void afterPhase(PhaseEvent phaseEvent) {
		phaseEvent.getFacesContext().renderResponse();
	}

	public void beforePhase(PhaseEvent phaseEvent) {
		// This method is required by the PhaseListener interfaces but is not used.
	}

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
