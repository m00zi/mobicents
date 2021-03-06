/*
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.servlet.sip.startup.loading.rules;

import javax.servlet.sip.SipServletRequest;

/**
 * @author Thomas Leseney
 */
public class EqualsRule extends RequestRule implements MatchingRule {
	private String value;
	private boolean ignoreCase;
	
	public EqualsRule(String var, String value, boolean ignoreCase) {
		super(var);
		this.value = value;
		this.ignoreCase = ignoreCase;
	}

	public boolean matches(SipServletRequest request) {
		if (!ignoreCase) {
			return value.equals(getValue(request));
		}
		return value.equalsIgnoreCase(getValue(request));
	}

	public String getExpression() {
		return "(" + getVarName() + " == " + value + ")";
	}
}
