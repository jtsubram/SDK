package com.cisco.activiti.delegate;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.pvm.runtime.ExecutionImpl;

import junit.framework.TestCase;

public class MsgClubTest extends TestCase {

	public void testExecute() {
		//fail("Not yet implemented");
		MsgClub fixture = new MsgClub();
		DelegateExecution execution = new ExecutionImpl();
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("incidentName", "");
		variables.put("ownerName", "jt");
		variables.put("severity", "L4");
		variables.put("description", "bad");
		variables.put("tenantId", "jt.com");
		variables.put("PhoneNumbers", "9886406256");
		variables.put("location", "in");
		execution.setVariables(variables);
		try {
			fixture.execute(execution);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(true);
	}
	
	

}
