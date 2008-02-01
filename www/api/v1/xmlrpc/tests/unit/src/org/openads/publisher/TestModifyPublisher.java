/*
+---------------------------------------------------------------------------+
| Openads v2.5                                                              |
| ============                                                              |
|                                                                           |
| Copyright (c) 2003-2007 Openads Limited                                   |
| For contact details, see: http://www.openads.org/                         |
|                                                                           |
| This program is free software; you can redistribute it and/or modify      |
| it under the terms of the GNU General Public License as published by      |
| the Free Software Foundation; either version 2 of the License, or         |
| (at your option) any later version.                                       |
|                                                                           |
| This program is distributed in the hope that it will be useful,           |
| but WITHOUT ANY WARRANTY; without even the implied warranty of            |
| MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             |
| GNU General Public License for more details.                              |
|                                                                           |
| You should have received a copy of the GNU General Public License         |
| along with this program; if not, write to the Free Software               |
| Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA |
+---------------------------------------------------------------------------+
|  Copyright 2003-2007 Openads Limited                                      |
|                                                                           |
|  Licensed under the Apache License, Version 2.0 (the "License");          |
|  you may not use this file except in compliance with the License.         |
|  You may obtain a copy of the License at                                  |
|                                                                           |
|    http://www.apache.org/licenses/LICENSE-2.0                             |
|                                                                           |
|  Unless required by applicable law or agreed to in writing, software      |
|  distributed under the License is distributed on an "AS IS" BASIS,        |
|  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. |
|  See the License for the specific language governing permissions and      |
|  limitations under the License.                                           |
+---------------------------------------------------------------------------+
$Id:$
*/

package org.openads.publisher;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.openads.config.GlobalSettings;
import org.openads.utils.ErrorMessage;
import org.openads.utils.TextUtils;

/**
 * Verify Modify Publisher method
 * 
 * @author <a href="mailto:apetlyovanyy@lohika.com">Andriy Petlyovanyy</a>
 */

public class TestModifyPublisher extends PublisherTestCase {
	private Integer publisherId;

	protected void setUp() throws Exception {
		super.setUp();
		publisherId = createPublisher();
	}

	protected void tearDown() throws Exception {
		deletePublisher(publisherId);
		super.tearDown();
	}

	/**
	 * Test method with all required fields and some optional.
	 * 
	 * @throws XmlRpcException
	 * @throws MalformedURLException
	 */
	public void testModifyPublisherAllReqAndSomeOptionalFields()
			throws XmlRpcException, MalformedURLException {

		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(PUBLISHER_ID, publisherId);
		struct.put(AGENCY_ID, agencyId);
		struct.put(PUBLISHER_NAME, "test Publisher");
		struct.put(CONTACT_NAME, "contactPublisher");
		Object[] params = new Object[] { sessionId, struct };
		final boolean result = (Boolean) execute(MODIFY_PUBLISHER_METHOD,
				params);
		assertTrue(result);
	}

	/**
	 * Test method without some required fields.
	 * 
	 * @throws MalformedURLException
	 */
	public void testModifyPublisherWithoutSomeRequiredFields()
			throws MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(PUBLISHER_NAME, "test Publisher");
		struct.put(CONTACT_NAME, "contactPublisher");
		Object[] params = new Object[] { sessionId, struct };

		try {
			execute(MODIFY_PUBLISHER_METHOD, params);
		} catch (XmlRpcException e) {
			assertEquals(ErrorMessage.WRONG_ERROR_MESSAGE, ErrorMessage
					.getMessage(
							ErrorMessage.FIELD_IN_STRUCTURE_DOES_NOT_EXISTS,
							PUBLISHER_ID), e.getMessage());
		}
	}

	/**
	 * Execute test method with error
	 * 
	 * @param params -
	 *            parameters for test method
	 * @param errorMsg -
	 *            true error messages
	 * @throws MalformedURLException
	 */
	private void executeModifyPublisherWithError(Object[] params,
			String errorMsg) throws MalformedURLException {
		try {
			execute(MODIFY_PUBLISHER_METHOD, params);
			fail(ErrorMessage.METHOD_EXECUTED_SUCCESSFULLY_BUT_SHOULD_NOT_HAVE);
		} catch (XmlRpcException e) {
			assertEquals(ErrorMessage.WRONG_ERROR_MESSAGE, errorMsg, e
					.getMessage());
		}

	}

	/**
	 * Test method with fields that has value greater than max.
	 * 
	 * @throws MalformedURLException
	 * @throws XmlRpcException
	 */
	public void testModifyPublisherGreaterThanMaxFieldValueError()
			throws MalformedURLException, XmlRpcException {
		final String strGreaterThan64 = TextUtils.getString(65);
		final String strGreaterThan255 = TextUtils.getString(256);

		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(PUBLISHER_ID, publisherId);
		Object[] params = new Object[] { sessionId, struct };

		struct.put(PUBLISHER_NAME, strGreaterThan255);
		executeModifyPublisherWithError(params, ErrorMessage.getMessage(
				ErrorMessage.EXCEED_MAXIMUM_LENGTH_OF_FIELD, PUBLISHER_NAME));
	}

	/**
	 * Test method with fields that has min. allowed values.
	 * 
	 * @throws XmlRpcException
	 * @throws MalformedURLException
	 */
	public void testModifyPublisherMinValues() throws XmlRpcException,
			MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(PUBLISHER_ID, publisherId);
		struct.put(PUBLISHER_NAME, "");
		struct.put(CONTACT_NAME, "");
		struct.put(EMAIL_ADDRESS, TextUtils.MIN_ALLOWED_EMAIL);
		Object[] params = new Object[] { sessionId, struct };
		final Boolean result = (Boolean) execute(MODIFY_PUBLISHER_METHOD,
				params);
		assertNotNull(result);
	}

	/**
	 * Test method with fields that has max. allowed values.
	 * 
	 * @throws XmlRpcException
	 * @throws MalformedURLException
	 */
	public void testModifyPublisherMaxValues() throws XmlRpcException,
			MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();

		struct.put(PUBLISHER_ID, publisherId);
		struct.put(PUBLISHER_NAME, TextUtils.getString(255));
		struct.put(CONTACT_NAME, TextUtils.getString(255));
		struct.put(EMAIL_ADDRESS, TextUtils.getString(55) + "@mail.com");
		Object[] params = new Object[] { sessionId, struct };
		final Boolean result = (Boolean) execute(MODIFY_PUBLISHER_METHOD,
				params);
		assertTrue(result);
	}

	/**
	 * Test methods for Unknown ID Error, described in API
	 * 
	 * @throws MalformedURLException
	 * @throws XmlRpcException
	 */
	public void testModifyPublisherUnknownIdError()
			throws MalformedURLException, XmlRpcException {
		Map<String, Object> struct = new HashMap<String, Object>();
		Object[] params = new Object[] { sessionId, struct };

		// If the publisherId is not a defined publisher ID.
		int publisherId = createPublisher();
		assertNotNull(publisherId);
		deletePublisher(publisherId);

		struct.put(PUBLISHER_ID, publisherId);

		executeModifyPublisherWithError(params, ErrorMessage.getMessage(
				ErrorMessage.UNKNOWN_ID_ERROR, PUBLISHER_ID));

		// If the agencyId is not a defined agency ID.
		publisherId = createPublisher();
		int agencyId = createAgency();
		deleteAgency(agencyId);

		struct.put(PUBLISHER_ID, publisherId);
		struct.put(AGENCY_ID, agencyId);

		executeModifyPublisherWithError(params, ErrorMessage.getMessage(
				ErrorMessage.UNKNOWN_ID_ERROR, AGENCY_ID));

		deletePublisher(publisherId);
	}

	/**
	 * Test method with fields that has value of wrong type (error).
	 * 
	 * @throws MalformedURLException
	 */
	public void testModifyPublisherWrongTypeError()
			throws MalformedURLException {
		Map<String, Object> struct = new HashMap<String, Object>();
		struct.put(PUBLISHER_ID, publisherId);
		Object[] params = new Object[] { sessionId, struct };

		struct.put(PUBLISHER_NAME, TextUtils.NOT_STRING);
		executeModifyPublisherWithError(params, ErrorMessage.getMessage(
				ErrorMessage.FIELD_IS_NOT_STRING, PUBLISHER_NAME));
	}

}
